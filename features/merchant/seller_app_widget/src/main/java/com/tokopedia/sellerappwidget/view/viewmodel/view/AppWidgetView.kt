package com.tokopedia.sellerappwidget.view.viewmodel.view

/**
 * Created By @ilhamsuaib on 17/11/20
 */

interface AppWidgetView<T : Any> {

    fun onSuccess(result: T)

    fun onError(t: Throwable)
}