package com.tokopedia.flight.search.data.repository

import org.junit.Test
import rx.Observable
import rx.observers.TestSubscriber
import java.util.concurrent.TimeUnit

/**
 * Created by Rizky on 10/10/18.
 */
class RxJavaTest {

    @Test
    fun testRepeatWhen() {
        val pair1 = Pair(1L, "1")
        val pair2 = Pair(2L, "2")
        val pair3 = Pair(3L, "3")
        val pair4 = Pair(4L, "4")
        val pair5 = Pair(5L, "5")
        val pair6 = Pair(6L, "6")
        val observable = Observable.just(pair1, pair2, pair3, pair4, pair5, pair6)

        val testSubscriber = TestSubscriber<String>()

        observable
                .repeat()
                .delay {
                    Observable.just(it.second)
                            .delay(it.first, TimeUnit.SECONDS)
                }
//                .flatMap {
//                    Observable.just(it).delaySubscription(it.first, TimeUnit.SECONDS)
//                }
//                .repeatWhen {
//                    it.delay(100L, TimeUnit.SECONDS)
//                }
//                .takeUntil { it.second == "4" }
                .takeWhile { it.second == "4" }
//                .filter { it.second == "4" }
                .subscribe {
                    val str = it.second
                    println(str)
                }

//        assert(testSubscriber.valueCount == 1)
    }

//    @Test
//    fun testRetryWhen() {
//        val someInt = listOf(1, 2, 3)
//
//        val observable = Observable.from(someInt)
//
//        observable.flatMap {
//            if (it != 5) {
//                val observableError: Observable<InvalidObjectException> =
//                        Observable.error(InvalidObjectException(""))
//                observableError
//            } else {
//                Observable.just(it)
//            }
//        }.retryWhen { errors ->
//            errors.flatMap { error ->
//                if (error is InvalidObjectException) {
//                    Observable.just("")
//                }
//                // For anything else, don't retry
//                Observable.just(error)
//            }
//        }.subscribe {
//            println(it)
//        }

    //        return flightSearchRepository.getSearchReturnCombined(flightSearchCombinedApiRequestModel)
//                .flatMap {
//                    if (it.isNeedRefresh) {
//                        val observableError: Observable<InvalidObjectException> =
//                                Observable.error(InvalidObjectException(""))
//                        observableError
//                    } else {
//                        Observable.just(it)
//                    }
//                }.retryWhen { errors ->
//                    errors.flatMap { error ->
//                        if (error is InvalidObjectException) {
//                            Observable.timer(5, TimeUnit.SECONDS)
//                        }
//                        // For anything else, don't retry
//                        Observable.error<Exception>(error)
//                    }
//                }.map { Meta() }
//    }

    @Test
    fun testTimer() {
        val pair1 = Pair(1L, "1")
        val pair2 = Pair(2L, "2")
        val pair3 = Pair(3L, "3")
        val pair4 = Pair(4L, "4")
        val pair5 = Pair(5L, "5")
        val pair6 = Pair(6L, "6")
        val observable = Observable.just(pair1, pair2, pair3, pair4, pair5, pair6)

        var del = 0L
//        Observable.defer {
//            Observable.timer(del, TimeUnit.SECONDS) }
//                .flatMap { observable }
//                .repeat()
//                .takeUntil {
//                    del = it.first
//                    it.second ==  "4"
//                }.subscribe {
//                    val str = it
//                    println(str)
//                }
    }

//    @Test
//    fun testTakeUntil() {
////        val someInt = listOf(1, 2, 3)
//
//        val isNeedRefresh = true
//
//        val observable = Observable.just(isNeedRefresh)
//
//        Observable.defer {
//            val pollDelay = intArrayOf(0)
//            observable.doOnNext {
//                pollDelay[0] = 3
//            }.repeatWhen { o ->
//                o.flatMap { Observable.timer(pollDelay[0].toLong(), TimeUnit.MILLISECONDS) }
//            }.takeUntil { it == false }.last()
//        }.subscribe {
//            println(it)
//        }
//    }

    @Test
    fun testRepeat() {
        val isNeedRefresh = true

        Observable.just(isNeedRefresh)
                .repeat()
                .takeUntil { !it }
                .last()
                .subscribe {
                    println(it)
                }
    }



}