package com.tokopedia.shop.flash_sale.presentation.campaign_list

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class CampaignListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _areInputValid = SingleLiveEvent<Boolean>()
    val areInputValid: LiveData<Boolean>
        get() = _areInputValid


}