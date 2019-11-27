package com.tokopedia.gamification.pdp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.data.di.modules.DispatcherModule
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationParamUseCase
import com.tokopedia.recommendation_widget_common.domain.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class PdpDialogViewModel @Inject constructor(val recommendationUseCase: GetRecommendationUseCase,
                                             val gamingRecommendationParamUseCase: GamingRecommendationParamUseCase,
                                             @Named(DispatcherModule.MAIN) val uiDispatcher: CoroutineDispatcher,
                                             @Named(DispatcherModule.IO) val workerDispatcher: CoroutineDispatcher) : BaseViewModel(uiDispatcher) {

    val recommendationLiveData: MutableLiveData<LiveDataResult<Boolean>> = MutableLiveData()
    val productLiveData: MutableLiveData<LiveDataResult<List<RecommendationWidget>>> = MutableLiveData()

    fun getRecommendationParams(pageName:String) {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = gamingRecommendationParamUseCase.graphqlUseCase.getData(gamingRecommendationParamUseCase.getRequestParams())
                recommendationLiveData.postValue(LiveDataResult.success(true))
                getProducts()
            }
        }, onError = {
            recommendationLiveData.postValue(LiveDataResult.error(it))
        })


    }

    fun getProducts() {
        launchCatchError(block = {
            withContext(workerDispatcher){
//                val requestParams = recommendationUseCase.getRecomParams()
//                val list = recommendationUseCase.getData(requestParams)
//                productLiveData.postValue(LiveDataResult.success(list))
            }
        }, onError = {
            productLiveData.postValue(LiveDataResult.error(it))
        })

    }

    override fun onCleared() {
        super.onCleared()
        recommendationUseCase.unsubscribe()
        gamingRecommendationParamUseCase.unsubscribe()
    }
}