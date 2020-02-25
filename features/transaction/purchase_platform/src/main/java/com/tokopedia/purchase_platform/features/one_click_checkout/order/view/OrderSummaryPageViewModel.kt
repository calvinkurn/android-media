package com.tokopedia.purchase_platform.features.one_click_checkout.order.view

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class OrderSummaryPageViewModel @Inject constructor(dispatcher: CoroutineDispatcher) : BaseViewModel(dispatcher) {

    fun updateProduct(product: OrderProduct) {

    }

    fun updatePreference(preference: Preference) {

    }

    fun getAllPreference() {

    }
}