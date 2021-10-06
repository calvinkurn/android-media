package com.tokopedia.recommendation_widget_common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

interface RecomWidgetDispatcher {
    fun getMainDispatcher(): CoroutineDispatcher
    fun getIODispatcher(): CoroutineDispatcher
    fun getSchedulerIO(): Scheduler
    fun getSchedulerMain(): Scheduler
}

class RecomWidgetDispatcherImpl : RecomWidgetDispatcher {
    override fun getMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    override fun getIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    override fun getSchedulerIO(): Scheduler = Schedulers.io()

    override fun getSchedulerMain(): Scheduler = AndroidSchedulers.mainThread()
}