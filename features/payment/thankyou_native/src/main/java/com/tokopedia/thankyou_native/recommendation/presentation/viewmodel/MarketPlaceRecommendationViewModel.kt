package com.tokopedia.thankyou_native.recommendation.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.recommendation.data.ProductRecommendationData
import com.tokopedia.thankyou_native.recommendation.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.recommendation.domain.TYPGetRecommendationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MarketPlaceRecommendationViewModel @Inject constructor(
        @CoroutineMainDispatcher val mainDispatcher:CoroutineDispatcher,
        private val recommendationUseCase: dagger.Lazy<TYPGetRecommendationUseCase>,
       ) : BaseViewModel(mainDispatcher) {

    val recommendationMutableData = MutableLiveData<Result<ProductRecommendationData>>()

    fun loadRecommendationData(thanksPageData: ThanksPageData) {
        launchCatchError(block = {
            val data = recommendationUseCase.get().getProductRecommendationData(thanksPageData)
            data?.let {
                recommendationMutableData.postValue(Success(data))
            }?:run{
                recommendationMutableData.postValue(Fail(Exception()))
            }
        }, onError = {
            recommendationMutableData.postValue(Fail(it))
        })
    }
}
