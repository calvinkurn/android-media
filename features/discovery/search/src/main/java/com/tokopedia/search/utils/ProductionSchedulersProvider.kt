package com.tokopedia.search.utils

import dagger.Module
import dagger.Provides
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ProductionSchedulersProvider: SchedulersProvider {

    override fun io(): Scheduler = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler = Schedulers.computation()
}

@Module
class ProductionSchedulersProviderModule {

    @Provides
    fun providesProductionSchedulersProvider(): SchedulersProvider {
        return ProductionSchedulersProvider()
    }
}