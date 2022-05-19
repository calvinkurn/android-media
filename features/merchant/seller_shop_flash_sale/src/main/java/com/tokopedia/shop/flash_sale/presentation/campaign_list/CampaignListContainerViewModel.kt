package com.tokopedia.shop.flash_sale.presentation.campaign_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flash_sale.domain.entity.CampaignMeta
import com.tokopedia.shop.flash_sale.domain.entity.TabMeta
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignListMetaUseCase
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class CampaignListContainerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignListMetaUseCase: GetSellerCampaignListMetaUseCase,
    private val getSellerCampaignListUseCase: GetSellerCampaignListUseCase
) : BaseViewModel(dispatchers.main) {

    private val _tabsMeta = MutableLiveData<Result<List<TabMeta>>>()
    val tabsMeta: LiveData<Result<List<TabMeta>>>
        get() = _tabsMeta

    private val _campaigns = MutableLiveData<Result<CampaignMeta>>()
    val campaigns: LiveData<Result<CampaignMeta>>
        get() = _campaigns

    fun getTabsMeta() {
        launchCatchError(
            dispatchers.io,
            block = {
                val tabs = getSellerCampaignListMetaUseCase.execute()
                _tabsMeta.postValue(Success(tabs))
            },
            onError = { error ->
                _tabsMeta.postValue(Fail(error))
            }
        )

    }

    fun getCampaigns(
        rows: Int,
        offset: Int,
        statusId: List<Int>,
        campaignName: String,
        thematicParticipation: Boolean
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaignMeta = getSellerCampaignListUseCase.execute(
                    rows = rows,
                    offset = offset,
                    statusId = statusId,
                    campaignName = campaignName,
                    thematicParticipation = thematicParticipation
                )
                _campaigns.postValue(Success(campaignMeta))
            },
            onError = { error ->
                _campaigns.postValue(Fail(error))
            }
        )

    }


}