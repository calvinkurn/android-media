package com.tokopedia.sellerappwidget.view.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

/**
 * Created By @ilhamsuaib on 17/11/20
 */

abstract class BaseAppWidgetVM<T>(
        private val dispatchers: CoroutineDispatchers
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main

    protected var view: T? = null
        private set

    fun bindView(view: T) {
        this.view = view
    }

    fun unbind() {
        this.view = null
    }
}