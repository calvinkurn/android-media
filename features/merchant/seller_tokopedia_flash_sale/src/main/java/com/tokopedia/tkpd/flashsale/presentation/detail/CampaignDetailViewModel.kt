package com.tokopedia.tkpd.flashsale.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tkpd.flashsale.common.extension.dateOnly
import com.tokopedia.tkpd.flashsale.common.extension.epochToDate
import com.tokopedia.tkpd.flashsale.domain.entity.Campaign
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleDetailForSellerUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import java.util.*
import javax.inject.Inject

class CampaignDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleDetailForSellerUseCase: GetFlashSaleDetailForSellerUseCase
) : BaseViewModel(dispatchers.main) {

    private var _campaign = MutableLiveData<Result<Campaign>>()
    val campaign: LiveData<Result<Campaign>>
    get() = _campaign

    fun getCampaignDetail(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleDetailForSellerUseCase.execute(
                    tabName = "upcoming",
                    campaignId = campaignId
                )
                _campaign.postValue(Success(result))
            },
            onError = { error ->
                _campaign.postValue(Fail(error))
            }
        )
    }

    fun isCampaignRegisterClosed(campaign: Campaign): Boolean {
        return Date() > campaign.submissionEndDateUnix.epochToDate()
    }

    fun isFlashSalePeriodOnTheSameDate(campaign: Campaign): Boolean {
        val startDate = campaign.startDateUnix.epochToDate().dateOnly()
        val endDate = campaign.endDateUnix.epochToDate().dateOnly()
        return startDate == endDate
    }
 }