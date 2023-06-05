package com.example.rxjavapractice

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.reactivex.Observer
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.observable.ObservableFromArray
import io.reactivex.schedulers.Schedulers

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.lang.Thread.sleep
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.rxjavapractice", appContext.packageName)
    }

    @Test
    fun createObservable () {
        val months : Observable <String> = Observable.just (
            "1", "2" , "3" , "4" , "5" , "6" , "7" , "8"
                )

        months.subscribe (object : Observer<String> {
            override fun onSubscribe (d: Disposable) {
                println("onSubscribe" + Thread.currentThread().name + "\n")
            }

            override fun onNext(t: String) {
                println("onNext $t")
            }

            override fun onError (e : Throwable) {
                println ("onError: $e")
            }

            override fun onComplete () {
                println ("\n onComplete")
            }

        })
    }

    @Test
    fun createObservableIterable () {
        val list : List <String>  = listOf("one", "two", "three", "four", "five")
        val numberList: Observable<String> = Observable.fromIterable(list)

        numberList.subscribe(object : Observer<String> {

            override fun onSubscribe (d: Disposable) {
                println("onSubscribe" + Thread.currentThread().name + "\n")
            }

            override fun onNext(t: String) {
                println("onNext $t")
            }

            override fun onError (e : Throwable) {
                println ("onError: $e")
            }

            override fun onComplete () {
                println ("\n onComplete")
            }
        })

    }
    @Test
    fun ObservableFromArray () {
        //Prints straight away
        val list : List <String>  = listOf("one", "two", "three", "four", "five")
        val numberList : Observable<List <String>> = Observable.fromArray(list)

        numberList.subscribe(object : Observer <List <String>> {
            override fun onSubscribe (d: Disposable) {
                println("onSubscribe" + Thread.currentThread().name + "\n")
            }

            override fun onNext(t: List <String>) {
                println("onNext $t")
            }

            override fun onError (e : Throwable) {
                println ("onError: $e")
            }

            override fun onComplete () {
                println ("\n onComplete")
            }
        })

    }




    //create()  example
    @Test fun createDemoMethod () {
        val  obs : Observable <String> = Observable.create(ObservableOnSubscribe <String> () {
            fun subscribe (emitter : ObservableEmitter<String>) {
                emitter.onNext("One value")
                emitter.onNext("second value")
                emitter.onNext("third value")
                emitter.onComplete()
            }
        })
    }


    //Disposable test
    @Test
    fun disposableDEmo () {
        var disposable : Disposable? = null
        val source : Observable <String> = Observable.just("oneitem")
        disposable = source.subscribe {s -> println(" Received") }
        //clear dispose
        disposable.dispose()
    }

    //composite disposable example
    @Test
    fun compositeDisposable () {
        val compdis = CompositeDisposable ()
        var dis : Disposable? = null
        val source : Observable<String> = Observable.just ("one item")
        compdis.addAll(source.subscribe{ s -> println ("received")})
        //clear
        compdis.clear()
    }


    //take example

    @Test
    fun takeOperator () {
        val source = Observable.fromArray(1,2,3)
        source.take(1).subscribe {s -> println(s) }
    }

    //skip example
    @Test
    fun skipOperator () {
        val source = Observable.fromArray(1,2,3)
        source.skip(1).subscribe {s -> println(s) }
    }

    //takewhile example
    @Test
    fun takeWhileOperator () {
        val source = Observable.fromArray(1,2,3,4,0,2,5)
        source.takeWhile{x -> x < 4}.subscribe{s -> println(s) }
    }

    //SkipWhile
    @Test
    fun skipWhileOperator () {
        val source = Observable.fromArray(1,2,3,4,5,5,6,7)
        source.skipWhile{x -> x<4}.subscribe{s -> println(s) }
    }


    //test distinct operator
    @Test
    fun distinctOperator () {
        val source = Observable.fromArray(1,2,2,2,3,4,5,6,8,8,9)
        source.distinct().subscribe{s -> println(s) }
    }

    //element at
    @Test
    fun elementAt () {
        val source = Observable.fromArray(1,2,3,4)
        source.elementAt(2).subscribe{s -> println(s) } //print "3"
    }


    //test flatmap
    @Test
    fun flatmapOperator () {
        val source : Observable<String> = Observable.just("day")

        source.flatMap {
            if (it == "day") {
                return@flatMap Observable.just("Mon","Tue", "Sun")

            } else {
                return@flatMap Observable.just("Mar" , "may")
            }
        }.subscribe{s -> println(s) }
    }


    //test merge operator
    @Test
    fun mergeOperator ()  {
        val source1 = Observable.interval(1, TimeUnit.SECONDS)
            .take(2)
            .map{l -> l+1}
            .map {l -> "source 1: $l"}

        val source2 = Observable.interval(300,TimeUnit.MILLISECONDS)
            .map {l -> (l + 1 ) * 300}
            .map {l -> "source 2 : $l"}


        //merge two sources

        println("result of merge")
        Observable.merge(source1,source2).subscribe{s -> println(s) }
        sleep(5000)
    }

    //test concat method
    @Test
    fun concatOperator ()  {
        val source1 = Observable.interval(1, TimeUnit.SECONDS)
            .take(2)
            .map{l -> l+1}
            .map {l -> "source 1: $l"}

        val source2 = Observable.interval(300,TimeUnit.MILLISECONDS)
            .map {l -> (l + 1 ) * 300}
            .map {l -> "source 2 : $l"}


        //merge two sources

        println("result of merge")
        Observable.concat(source1,source2).subscribe{s -> println(s) }
        sleep(5000)
    }

    //test debounce
    @Test
    fun debounceOperator () {
        val source1 = Observable.interval(50, TimeUnit.MILLISECONDS)
            .map {i: Long -> "RED $i"}
            .take(3)
            .doOnNext{ s -> println(s)}

        val source2 = Observable.interval(260, TimeUnit.MILLISECONDS)
            .map { i: Long -> "GREEN $i" }
            .take(3)
            .doOnNext{s -> println(s)}

        val source3 = Observable.interval(151, TimeUnit.MILLISECONDS)
            .map {i : Long -> "Blue $i"}
            .take (3)
            .doOnNext {s -> println(s) }

        Observable.merge(source1,source2,source3)
            .debounce (100, TimeUnit.MILLISECONDS)
            .subscribe{s -> println(s) }

        sleep(5000)

    }

    data class Book(
        val title: String,
        val author: String,
        val id: Long?,
        val price: Double,
        val location: String,
        val currency: String
    )

    fun getBooksList(): List<Book> {
        return listOf(
            Book("Шантарам", "Грегори Дэвид Робертс", 1, 780.0, "Москва", "₽"),
            Book("Три товарища", "Эрих Мария Ремарк", 2, 480.0, "Москва", "₽"),
            Book("Цветы для Элджернона", "Даниел Киз", 3, 380.0, "Москва", "₽"),
            Book(" Атлант расправил плечи", "Айн Рэнд", 4, 880.0, "Ставрополь", "₽"),
            Book(" Атлант расправил плечи", "Айн Рэнд", 4, 580.0, "Сочи", "₽")
        )
    }

    // Получить список книг
// Отфильтровать только те книги, которые есть в Москве
// Отфильтровать только те книги, которые стоят > 400
// Исключить одинаковые названия книг
// Получить название книги и имя автора
// Вывести название книги + автор в консоль
    @Test
    fun getListOfBook () {
        val source = Observable.fromIterable(getBooksList()) // Получить список книг
        source.map { it.title + it.author } // Получить название книги и имя автора

        source.filter {it.title == "Москва"} // Отфильтровать только те книги, которые есть в Москве

        source.filter {it.price > 400} // Отфильтровать только те книги, которые стоят > 400

        source.distinct() // Исключить одинаковые названия книг

        source.subscribe {s -> println(s) } // Вывести название книги + автор в консоль

    }

    @Test
    fun observeOnOperator () {
        val source = Observable.just(1,2)
        source.subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.newThread())
            .subscribe{
                i -> println("Received " + i + "new thread " + Thread.currentThread().name)

            }
        sleep(5000)
    }
    
}