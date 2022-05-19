package com.tokopedia.shop.flash_sale.presentation.campaign_list

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flash_sale.domain.entity.TabMeta
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class CampaignListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _campaignListMeta = SingleLiveEvent<Result<List<TabMeta>>>()
    val campaignListMeta: LiveData<Result<List<TabMeta>>>
        get() = _campaignListMeta



}