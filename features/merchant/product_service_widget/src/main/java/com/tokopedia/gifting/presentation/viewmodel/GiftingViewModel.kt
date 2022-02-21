package com.tokopedia.gifting.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class GiftingViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
) : BaseViewModel(dispatchers.main) {

    fun dummy() {
        println("subhanallah")
    }
}