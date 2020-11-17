package com.tokopedia.sellerappwidget.view.viewmodel.view

import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

/**
 * Created By @ilhamsuaib on 17/11/20
 */

interface OrderAppWidgetView {

    fun onSuccessGetOrderList(result: Success<List<OrderUiModel>>)

    fun onFailedGetOrderList(fail: Fail)
}