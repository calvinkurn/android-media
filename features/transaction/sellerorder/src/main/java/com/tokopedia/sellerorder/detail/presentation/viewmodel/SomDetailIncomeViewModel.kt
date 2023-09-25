package com.tokopedia.sellerorder.detail.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import javax.inject.Inject

class SomDetailIncomeViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
): BaseViewModel(dispatcher.main) {


    fun fetchDetailIncome(orderId: String) {
        launchCatchError(block = {

        }, onError = {

        })
    }
}
