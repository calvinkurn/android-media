package com.tokopedia.sellerappwidget.view.viewmodel.view

import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

/**
 * Created By @ilhamsuaib on 17/11/20
 */

interface AppWidgetView<T : Any> {

    fun onSuccessGetOrderList(result: Success<T>)

    fun onFailedGetOrderList(fail: Fail)
}