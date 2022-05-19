package com.tokopedia.shop.flash_sale.presentation.campaign_list

import androidx.lifecycle.LiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flash_sale.domain.entity.ProductListMeta
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignListMetaUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import javax.inject.Inject

class CampaignListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignListMetaUseCase: GetSellerCampaignListMetaUseCase
) : BaseViewModel(dispatchers.main) {

    private val _campaignListMeta = SingleLiveEvent<Result<List<ProductListMeta>>>()
    val campaignListMeta: LiveData<Result<List<ProductListMeta>>>
        get() = _campaignListMeta

    fun getCampaignMeta() {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaignMeta = getSellerCampaignListMetaUseCase.execute()
                _campaignListMeta.postValue(Success(campaignMeta))
            },
            onError = { error ->
                _campaignListMeta.postValue(Fail(error))
            }
        )

    }


}