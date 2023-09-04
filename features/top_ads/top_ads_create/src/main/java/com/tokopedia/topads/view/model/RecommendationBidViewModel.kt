package com.tokopedia.topads.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.data.ImpressionPredictionResponse
import com.tokopedia.topads.domain.usecase.TopAdsImpressionPredictionBrowseUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import javax.inject.Inject

class RecommendationBidViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val topAdsImpressionPredictionUseCase: TopAdsImpressionPredictionBrowseUseCase
) : BaseViewModel(dispatcher.main) {


    private val _performanceData = MutableLiveData<Result<ImpressionPredictionResponse>>()
    val performanceData: LiveData<Result<ImpressionPredictionResponse>>
        get() = _performanceData


    fun getPerformanceData(productIds: List<String>,
                           finalBid: Float,
                           initialBid: Float,
                           dailyBudget: Float) {
        launchCatchError(block = {
            val data = topAdsImpressionPredictionUseCase.invoke("test", productIds, finalBid, initialBid, dailyBudget)
            _performanceData.value = data
        }, onError = {
            _performanceData.value = Fail(it)
        })
    }

}
