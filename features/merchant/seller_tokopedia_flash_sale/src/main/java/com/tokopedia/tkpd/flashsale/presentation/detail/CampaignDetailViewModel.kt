package com.tokopedia.tkpd.flashsale.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tkpd.flashsale.common.extension.*
import com.tokopedia.tkpd.flashsale.data.request.GetFlashSaleSubmittedProductListRequest
import com.tokopedia.tkpd.flashsale.domain.entity.FlashSale
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProduct
import com.tokopedia.tkpd.flashsale.domain.entity.SubmittedProductData
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleDetailForSellerUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleSubmittedProductListUseCase
import com.tokopedia.tkpd.flashsale.util.extension.hoursDifference
import com.tokopedia.tkpd.flashsale.util.extension.minutesDifference
import com.tokopedia.tkpd.flashsale.util.extension.removeTimeZone
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import java.util.*
import javax.inject.Inject

class CampaignDetailViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleDetailForSellerUseCase: GetFlashSaleDetailForSellerUseCase,
    private val getFlashSaleSubmittedProductListUseCase: GetFlashSaleSubmittedProductListUseCase
) : BaseViewModel(dispatchers.main) {

    private var _campaign = MutableLiveData<Result<FlashSale>>()
    val campaign: LiveData<Result<FlashSale>>
        get() = _campaign

    private var _submittedProduct = MutableLiveData<Result<List<SubmittedProduct>>>()
    val submittedProduct: LiveData<Result<List<SubmittedProduct>>>
        get() = _submittedProduct

    companion object {
        private const val TWENTY_FOUR_HOURS = 24
        private const val SIXTY_MINUTES = 60
    }

    fun getCampaignDetail(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleDetailForSellerUseCase.execute(
                    campaignId = campaignId
                )
                _campaign.postValue(Success(result))
            },
            onError = { error ->
                _campaign.postValue(Fail(error))
            }
        )
    }

    fun getSubmittedProduct(campaignId: Long) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleSubmittedProductListUseCase.execute(
                    campaignId = campaignId,
                    pagination = GetFlashSaleSubmittedProductListRequest.Pagination(
                        10,
                        0
                    )
                )
                _submittedProduct.postValue(Success(result.productList))
            },
            onError = { error ->
                _submittedProduct.postValue(Fail(error))
            }
        )
    }

    fun isCampaignRegisterClosed(flashSale: FlashSale): Boolean {
        val now = Date()
        val flashSaleEndDate = flashSale.submissionEndDateUnix
        return now > flashSaleEndDate
    }

    fun isFlashSalePeriodOnTheSameDate(flashSale: FlashSale): Boolean {
        val startDate = flashSale.startDateUnix.dateOnly()
        val endDate = flashSale.endDateUnix.dateOnly()
        return startDate == endDate
    }

    fun isFlashSaleClosedMoreThan24Hours(targetDate: Date): Boolean {
        val now = Date()
        val distanceHoursToSubmissionEndDate = hoursDifference(now, targetDate)
        return distanceHoursToSubmissionEndDate > TWENTY_FOUR_HOURS
    }

    fun isFlashSaleClosedLessThan24Hour(targetDate: Date): Boolean {
        val now = Date()
        val distanceHoursToSubmissionEndDate = hoursDifference(now, targetDate)
        return distanceHoursToSubmissionEndDate in Int.ZERO..TWENTY_FOUR_HOURS
    }

    fun isFlashSaleClosedLessThan60Minutes(targetDate: Date): Boolean {
        val now = Date()
        val distanceMinutesToSubmissionEndDate = minutesDifference(now, targetDate)
        return distanceMinutesToSubmissionEndDate in Int.ZERO..SIXTY_MINUTES
    }
}