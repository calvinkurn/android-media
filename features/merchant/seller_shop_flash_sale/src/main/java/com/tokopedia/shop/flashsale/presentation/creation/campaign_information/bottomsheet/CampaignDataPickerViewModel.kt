package com.tokopedia.shop.flashsale.presentation.creation.campaign_information.bottomsheet

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class CampaignDataPickerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _areInputValid = SingleLiveEvent<Boolean>()
    val areInputValid: LiveData<Boolean>
        get() = _areInputValid


    fun validateInput() {

    }
}