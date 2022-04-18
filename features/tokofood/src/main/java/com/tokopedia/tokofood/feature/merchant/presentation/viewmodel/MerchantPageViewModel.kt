package com.tokopedia.tokofood.feature.merchant.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokofood.common.util.ResourceProvider
import com.tokopedia.tokofood.feature.merchant.domain.GetMerchantDataUseCase
import com.tokopedia.tokofood.feature.merchant.domain.model.response.GetMerchantDataResponse
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantOpsHour
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodMerchantProfile
import com.tokopedia.tokofood.feature.merchant.domain.model.response.TokoFoodTickerDetail
import com.tokopedia.tokofood.feature.merchant.presentation.model.CarouselData
import com.tokopedia.tokofood.feature.merchant.presentation.model.DataType
import com.tokopedia.tokofood.feature.merchant.presentation.model.MerchantOpsHour
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class MerchantPageViewModel @Inject constructor(
        private val resourceProvider: ResourceProvider,
        private val dispatchers: CoroutineDispatchers,
        private val getMerchantDataUseCase: GetMerchantDataUseCase
) : BaseViewModel(dispatchers.main) {

    private val getMerchantDataResultLiveData = MutableLiveData<Result<GetMerchantDataResponse>>()
    val getMerchantDataResult: LiveData<Result<GetMerchantDataResponse>> get() = getMerchantDataResultLiveData

    fun getMerchantData(merchantId: String, latlong: String, timezone: String) {
        launchCatchError(block = {
            val result = withContext(dispatchers.io) {
                val params = GetMerchantDataUseCase.createRequestParams(
                        merchantId = merchantId,
                        latlong = latlong,
                        timezone = timezone
                )
                getMerchantDataUseCase.setRequestParams(params = params.parameters)
                getMerchantDataUseCase.executeOnBackground()
            }
            getMerchantDataResultLiveData.value = Success(result)
        }, onError = {
            getMerchantDataResultLiveData.value = Fail(it)
        })
    }

    fun mapMerchantProfileToCarouselData(merchantProfile: TokoFoodMerchantProfile): List<CarouselData> {
        // rating
        val ratingData = CarouselData(
                dataType = DataType.RATING,
                title = merchantProfile.totalRatingFmt,
                information = merchantProfile.ratingFmt
        )
        // distance
        val distanceData = CarouselData(
                dataType = DataType.DISTANCE,
                title = resourceProvider.getDistanceTitle() ?: "",
                information = merchantProfile.distanceFmt.content,
                isWarning = merchantProfile.distanceFmt.isWarning
        )
        // estimation
        val estimationData = CarouselData(
                dataType = DataType.ETA,
                title = resourceProvider.getEstimationTitle() ?: "",
                information = merchantProfile.etaFmt.content,
                isWarning = merchantProfile.etaFmt.isWarning
        )
        // ops hours
        val opsHoursData = CarouselData(
                dataType = DataType.OPS_HOUR,
                title = resourceProvider.getOpsHoursTitle() ?: "",
                information = merchantProfile.opsHourFmt.content,
                isWarning = merchantProfile.opsHourFmt.isWarning
        )
        return listOf(ratingData, distanceData, estimationData, opsHoursData)
    }

    private fun formatRating(totalRating: Int): String {
        return when {
            // 10K - 10K+
            totalRating >= 10000 -> {
                return if (totalRating == 10000) "10K"
                else "10K+"
            }
            // 1K - 1K+
            totalRating >= 1000 -> {
                return if (totalRating == 1000) "1K"
                else {
                    var suffix = ""
                    if (totalRating % 1000 != 0) suffix = "+"
                    val ratingThousandCount = totalRating / 1000
                    ratingThousandCount.toString() + suffix
                }
            }
            // 100 - 999
            totalRating >= 100 -> {
                var suffix = ""
                if (totalRating % 100 != 0) suffix = "+"
                totalRating.toString() + suffix
            }
            else -> totalRating.toString()
        }
    }

    fun mapOpsHourDetailsToMerchantOpsHours(opsHourDetails: List<TokoFoodMerchantOpsHour>): List<MerchantOpsHour> {
        return opsHourDetails.mapIndexed { index, opsHourDetail ->
            val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            // response data from be always start from monday with index 0
            // monday index from be = 0 ; Calendar.MONDAY = 2
            var day = index + 2
            // sunday index from be = 6 ; Calendar.SUNDAY = 1
            if (index == opsHourDetails.lastIndex) {
                day -= 5
            }
            MerchantOpsHour(
                    initial = opsHourDetail.day.firstOrNull(),
                    day = opsHourDetail.day,
                    time = opsHourDetail.time,
                    isWarning = opsHourDetail.isWarning,
                    isToday = day == today
            )
        }
    }

    fun isTickerDetailEmpty(tickerData: TokoFoodTickerDetail): Boolean {
        return tickerData.title.isBlank() && tickerData.subtitle.isBlank()
    }
}