package com.tokopedia.shop.flash_sale.presentation.campaign_list.container

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flash_sale.domain.entity.CampaignAttribute
import com.tokopedia.shop.flash_sale.domain.entity.CampaignMeta
import com.tokopedia.shop.flash_sale.domain.entity.TabMeta
import com.tokopedia.shop.flash_sale.domain.entity.enums.CampaignStatus
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignAttributeUseCase
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
    private val getSellerCampaignAttributeUseCase: GetSellerCampaignAttributeUseCase,
    private val getSellerCampaignListUseCase: GetSellerCampaignListUseCase
) : BaseViewModel(dispatchers.main) {

    private var selectedTabPosition = 0

    private val _tabsMeta = MutableLiveData<Result<List<TabMeta>>>()
    val tabsMeta: LiveData<Result<List<TabMeta>>>
        get() = _tabsMeta

    private val _campaignAttribute = MutableLiveData<Result<CampaignAttribute>>()
    val campaignAttribute: LiveData<Result<CampaignAttribute>>
        get() = _campaignAttribute

    private val _campaignDrafts = MutableLiveData<Result<CampaignMeta>>()
    val campaignDrafts: LiveData<Result<CampaignMeta>>
        get() = _campaignDrafts

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

    fun setSelectedTabPosition(selectedTabPosition: Int) {
        this.selectedTabPosition = selectedTabPosition
    }

    fun getSelectedTabPosition(): Int {
        return selectedTabPosition
    }

    fun getCampaignAttribute(month: Int, year: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val attribute =
                    getSellerCampaignAttributeUseCase.execute(month = month, year = year)
                _campaignAttribute.postValue(Success(attribute))
            },
            onError = { error ->
                _campaignAttribute.postValue(Fail(error))
            }
        )

    }


    fun getCampaignDrafts(rows: Int, offset: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaignMeta = getSellerCampaignListUseCase.execute(
                    rows = rows,
                    offset = offset,
                    statusId = listOf(CampaignStatus.DRAFT.id)
                )
                _campaignDrafts.postValue(Success(campaignMeta))
            },
            onError = { error ->
                _campaignDrafts.postValue(Fail(error))
            }
        )

    }
}