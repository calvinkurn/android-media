package com.tokopedia.explore.view.subscriber

import rx.Subscriber

/**
 * Created by jegul on 2019-10-17
 */
class EmptySubscriber<T> : Subscriber<T>() {

    override fun onNext(t: T) {
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
    }
}