package com.tokopedia.shop.flash_sale.presentation.campaign_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flash_sale.domain.entity.*
import com.tokopedia.shop.flash_sale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flash_sale.domain.usecase.DoSellerCampaignCreationUseCase
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignAttributeUseCase
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignListMetaUseCase
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import java.util.*
import javax.inject.Inject

class CampaignListContainerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignListMetaUseCase: GetSellerCampaignListMetaUseCase,
    private val getSellerCampaignListUseCase: GetSellerCampaignListUseCase,
    private val getSellerCampaignAttributeUseCase: GetSellerCampaignAttributeUseCase,
    private val doSellerCampaignCreationUseCase: DoSellerCampaignCreationUseCase
) : BaseViewModel(dispatchers.main) {

    private val _tabsMeta = MutableLiveData<Result<List<TabMeta>>>()
    val tabsMeta: LiveData<Result<List<TabMeta>>>
        get() = _tabsMeta

    private val _campaigns = MutableLiveData<Result<CampaignMeta>>()
    val campaigns: LiveData<Result<CampaignMeta>>
        get() = _campaigns


    private val _campaignAttribute = MutableLiveData<Result<CampaignAttribute>>()
    val campaignAttribute: LiveData<Result<CampaignAttribute>>
        get() = _campaignAttribute


    private val _campaignCreation = MutableLiveData<Result<CampaignCreationResult>>()
    val campaignCreation: LiveData<Result<CampaignCreationResult>>
        get() = _campaignCreation

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


    fun getCampaignAttribute(month: Int, year: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val attribute = getSellerCampaignAttributeUseCase.execute(month = month, year = year)
                _campaignAttribute.postValue(Success(attribute))
            },
            onError = { error ->
                _campaignAttribute.postValue(Fail(error))
            }
        )

    }

    fun createCampaign(campaignName: String, startDate : Date, endDate: Date) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param =
                    DoSellerCampaignCreationUseCase.Param(
                        CampaignAction.Create,
                        campaignName,
                        startDate,
                        endDate,
                        isCampaignRuleSubmit = true,
                        firstColor = "#019751",
                        secondColor = "#00AA5B",
                        paymentType = PaymentType.INSTANT
                    )
                val attribute = doSellerCampaignCreationUseCase.execute(param)
                _campaignCreation.postValue(Success(attribute))
            },
            onError = { error ->
                _campaignCreation.postValue(Fail(error))
            }
        )

    }
}