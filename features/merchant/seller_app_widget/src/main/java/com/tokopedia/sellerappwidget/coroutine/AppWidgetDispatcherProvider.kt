package com.tokopedia.sellerappwidget.coroutine

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Created By @ilhamsuaib on 16/11/20
 */

interface AppWidgetDispatcherProvider {

    val io: CoroutineDispatcher

    val main: CoroutineDispatcher
}