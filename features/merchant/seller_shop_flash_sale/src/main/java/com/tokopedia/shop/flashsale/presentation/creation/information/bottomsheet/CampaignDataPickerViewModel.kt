package com.tokopedia.shop.flashsale.presentation.creation.information.bottomsheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.common.constant.Constant
import com.tokopedia.shop.flashsale.common.extension.dateOnly
import com.tokopedia.shop.flashsale.common.extension.removeTimeZone
import com.tokopedia.shop.flashsale.domain.entity.CampaignUiModel
import com.tokopedia.shop.flashsale.domain.entity.GroupedCampaign
import com.tokopedia.shop.flashsale.domain.entity.enums.activeCampaignStatusIds
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignAttributeUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignListUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class CampaignDataPickerViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignListUseCase: GetSellerCampaignListUseCase,
    private val getSellerCampaignAttributeUseCase: GetSellerCampaignAttributeUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val CAMPAIGN_TO_FETCH = 100
    }

    private val _campaigns = MutableLiveData<Result<List<GroupedCampaign>>>()
    val campaigns: LiveData<Result<List<GroupedCampaign>>>
        get() = _campaigns


    private val _campaignQuota = MutableLiveData<Result<Int>>()
    val campaignQuota: LiveData<Result<Int>>
        get() = _campaignQuota

    fun getUpcomingCampaigns() {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaigns = getSellerCampaignListUseCase.execute(
                    rows = CAMPAIGN_TO_FETCH,
                    offset = Constant.FIRST_PAGE,
                    statusId = activeCampaignStatusIds
                )
                val groupedCampaign = groupByDate(campaigns.campaigns)
                _campaigns.postValue(Success(groupedCampaign))
            },
            onError = { error ->
                _campaigns.postValue(Fail(error))
            }
        )

    }

    fun getCampaignQuota(month : Int, year: Int, vpsPackageId : Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaignAttribute = getSellerCampaignAttributeUseCase.execute(
                    month = month,
                    year = year,
                    vpsPackageId = vpsPackageId
                )

                _campaignQuota.postValue(Success(campaignAttribute.remainingCampaignQuota))
            },
            onError = { error ->
                _campaignQuota.postValue(Fail(error))
            }
        )

    }

    private fun groupByDate(campaigns: List<CampaignUiModel>): List<GroupedCampaign> {
        return campaigns
            .groupBy { it.startDate.removeTimeZone().dateOnly() }
            .map { GroupedCampaign(it.key, it.value.size) }
    }
}