package com.tokopedia.flight.searchV2.data.repository

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
                .takeUntil { it.second == "4" }
                .subscribe {
                    val str = it.second
                    println(str)
                }

//        assert(testSubscriber.valueCount == 1)
    }

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

}