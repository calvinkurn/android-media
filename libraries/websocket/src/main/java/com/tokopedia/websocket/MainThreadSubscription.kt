package com.tokopedia.websocket

import android.os.Looper
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author : Steven 07/10/18
 * this class is taken from Jake Wharton's RxAndroid
 * https://github.com/ReactiveX/RxAndroid/blob/1.1.0/rxandroid/src/main/java/rx/android/MainThreadSubscription.java
 */
abstract class MainThreadSubscription : Subscription {

    private val unsubscribed = AtomicBoolean()
    /**
     * Verify that the calling thread is the Android main thread.
     *
     *
     * Calls to this method are usually preconditions for subscription behavior which instances of
     * this class later undo. See the class documentation for an example.
     *
     * @throws IllegalStateException when called from any other thread.
     */
    fun verifyMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw IllegalStateException(
                    "Expected to be called on the main thread but was " + Thread.currentThread().name)
        }
    }

    override fun isUnsubscribed(): Boolean {
        return unsubscribed.get()
    }

    override fun unsubscribe() {
        if (unsubscribed.compareAndSet(false, true)) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                onUnsubscribe()
            } else {
                AndroidSchedulers.mainThread().createWorker().schedule { onUnsubscribe() }
            }
        }
    }

    protected abstract fun onUnsubscribe()
}