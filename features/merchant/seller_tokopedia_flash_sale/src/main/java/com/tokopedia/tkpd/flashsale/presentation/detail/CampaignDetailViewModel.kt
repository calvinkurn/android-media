package com.tokopedia.tkpd.flashsale.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tkpd.flashsale.common.extension.dateOnly
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleDetailForSellerUseCase
import com.tokopedia.tkpd.flashsale.util.extension.hoursDifference
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

    private var _campaign = MutableLiveData<Result<FlashSale>>()
    val campaign: LiveData<Result<FlashSale>>
        get() = _campaign

    companion object {
        private const val TWENTY_FOUR_HOURS = 24
        private const val TAB_NAME = "upcoming"
    }

    fun getCampaignDetail(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleDetailForSellerUseCase.execute(
                    tabName = TAB_NAME,
                    campaignId = campaignId
                )
                _campaign.postValue(Success(result))
            },
            onError = { error ->
                _campaign.postValue(Fail(error))
            }
        )
    }

    fun isCampaignRegisterClosed(flashSale: FlashSale): Boolean {
        return Date() > flashSale.submissionEndDateUnix
    }

    fun isFlashSalePeriodOnTheSameDate(flashSale: FlashSale): Boolean {
        val startDate = flashSale.startDateUnix.dateOnly()
        val endDate = flashSale.endDateUnix.dateOnly()
        return startDate == endDate
    }

    fun isFlashSaleClosedMoreThan24Hours(targetDate: Date): Boolean {
        val now = Date()
        val hourDifference = targetDate.time - now.time
        return hourDifference > TWENTY_FOUR_HOURS
    }

    fun isFlashSaleClosedLessThan24Hour(targetDate: Date): Boolean {
        val now = Date()
        val distanceHoursToSubmissionEndDate = hoursDifference(now, targetDate)
        return distanceHoursToSubmissionEndDate in Int.ZERO..TWENTY_FOUR_HOURS
    }
}