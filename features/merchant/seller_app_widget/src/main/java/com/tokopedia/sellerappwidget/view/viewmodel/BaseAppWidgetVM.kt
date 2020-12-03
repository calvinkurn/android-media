package com.tokopedia.sellerappwidget.view.viewmodel

import com.tokopedia.sellerappwidget.coroutine.AppWidgetDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Created By @ilhamsuaib on 17/11/20
 */

abstract class BaseAppWidgetVM<T>(
        protected val dispatcherProvider: AppWidgetDispatcherProvider
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = dispatcherProvider.main

    protected var view: T? = null
        private set

    fun bindView(view: T) {
        this.view = view
    }
}