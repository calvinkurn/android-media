package com.tokopedia.thankyou_native.recommendation.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.thankyou_native.recommendation.data.ProductRecommendationData
import com.tokopedia.thankyou_native.recommendation.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.thankyou_native.recommendation.domain.TYPGetRecommendationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MarketPlaceRecommendationViewModel @Inject constructor(
        @ApplicationContext val context: Context,
        @CoroutineMainDispatcher val mainDispatcher: dagger.Lazy<CoroutineDispatcher>,
        private val recommendationUseCase: dagger.Lazy<TYPGetRecommendationUseCase>,
        val userSession: dagger.Lazy<UserSessionInterface>) : BaseViewModel(mainDispatcher.get()) {

    val recommendationMutableData = MutableLiveData<Result<ProductRecommendationData>>()

    fun loadRecommendationData() {
        launchCatchError(block = {
            val data = recommendationUseCase.get().getProductRecommendationData()
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
