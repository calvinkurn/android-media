package com.tokopedia.weaver

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class RxAsyncWeave : WeaveAsyncProvider {
    override fun executeAsync(weaveInterface: WeaveInterface) {
        Observable.fromCallable {
            weaveInterface.execute()
            true
        }.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).unsubscribeOn(Schedulers.computation()).subscribe()
    }
}