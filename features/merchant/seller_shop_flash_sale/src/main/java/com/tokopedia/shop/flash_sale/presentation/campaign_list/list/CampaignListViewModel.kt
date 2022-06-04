package com.tokopedia.shop.flash_sale.presentation.campaign_list.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flash_sale.domain.entity.CampaignAction
import com.tokopedia.shop.flash_sale.domain.entity.CampaignCreationResult
import com.tokopedia.shop.flash_sale.domain.entity.CampaignMeta
import com.tokopedia.shop.flash_sale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flash_sale.domain.entity.aggregate.CampaignPrerequisiteData
import com.tokopedia.shop.flash_sale.domain.entity.aggregate.ShareComponentMetadata
import com.tokopedia.shop.flash_sale.domain.entity.enums.PaymentType
import com.tokopedia.shop.flash_sale.domain.usecase.DoSellerCampaignCreationUseCase
import com.tokopedia.shop.flash_sale.domain.usecase.GetSellerCampaignListUseCase
import com.tokopedia.shop.flash_sale.domain.usecase.aggregate.GetCampaignPrerequisiteDataUseCase
import com.tokopedia.shop.flash_sale.domain.usecase.aggregate.GetShareComponentMetadataUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import java.util.*
import javax.inject.Inject

class CampaignListViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignListUseCase: GetSellerCampaignListUseCase,
    private val doSellerCampaignCreationUseCase: DoSellerCampaignCreationUseCase,
    private val getCampaignPrerequisiteDataUseCase: GetCampaignPrerequisiteDataUseCase,
    private val getShareComponentMetadataUseCase: GetShareComponentMetadataUseCase
) : BaseViewModel(dispatchers.main) {

    private val _campaigns = MutableLiveData<Result<CampaignMeta>>()
    val campaigns: LiveData<Result<CampaignMeta>>
        get() = _campaigns

    private val _campaignPrerequisiteData = MutableLiveData<Result<CampaignPrerequisiteData>>()
    val campaignPrerequisiteData: LiveData<Result<CampaignPrerequisiteData>>
        get() = _campaignPrerequisiteData

    private val _campaignCreation = MutableLiveData<Result<CampaignCreationResult>>()
    val campaignCreation: LiveData<Result<CampaignCreationResult>>
        get() = _campaignCreation

    private val _shareComponentMetadata = MutableLiveData<Result<ShareComponentMetadata>>()
    val shareComponentMetadata: LiveData<Result<ShareComponentMetadata>>
        get() = _shareComponentMetadata

    private var drafts : List<CampaignUiModel> = emptyList()

    fun getCampaigns(
        rows: Int,
        offset: Int,
        statusId: List<Int>,
        campaignName: String
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaigns = getSellerCampaignListUseCase.execute(
                    rows = rows,
                    offset = offset,
                    statusId = statusId,
                    campaignName = campaignName
                )
                _campaigns.postValue(Success(campaigns))
            },
            onError = { error ->
                _campaigns.postValue(Fail(error))
            }
        )

    }

    fun getCampaignPrerequisiteData() {
        launchCatchError(
            dispatchers.io,
            block = {
                val prerequisiteData = getCampaignPrerequisiteDataUseCase.execute()
                _campaignPrerequisiteData.postValue(Success(prerequisiteData))
            },
            onError = { error ->
                _campaignPrerequisiteData.postValue(Fail(error))
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

    fun getShareComponentMetadata(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val metadata = getShareComponentMetadataUseCase.execute(campaignId)
                _shareComponentMetadata.postValue(Success(metadata))
            },
            onError = { error ->
                _shareComponentMetadata.postValue(Fail(error))
            }
        )

    }

    fun setCampaignDrafts(drafts : List<CampaignUiModel>) {
        this.drafts = drafts
    }

    fun getCampaignDrafts(): List<CampaignUiModel> {
        return drafts
    }
}