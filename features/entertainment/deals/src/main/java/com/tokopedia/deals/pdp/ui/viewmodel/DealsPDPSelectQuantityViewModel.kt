package com.tokopedia.deals.pdp.ui.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

class DealsPDPSelectQuantityViewModel @Inject constructor(dispatcher: CoroutineDispatchers):
    BaseViewModel(dispatcher.main) {

        var currentQuantity: Int = 1
}