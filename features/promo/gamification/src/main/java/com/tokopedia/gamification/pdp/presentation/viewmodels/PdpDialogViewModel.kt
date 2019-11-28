package com.tokopedia.gamification.pdp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.pdp.data.GamingRecommendationParamResponse
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.data.di.modules.DispatcherModule
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationParamUseCase
import com.tokopedia.gamification.pdp.domain.usecase.GamingRecommendationProductUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class PdpDialogViewModel @Inject constructor(val recommendationProductUseCase: GamingRecommendationProductUseCase,
                                             val paramUseCase: GamingRecommendationParamUseCase,
                                             @Named(DispatcherModule.MAIN) val uiDispatcher: CoroutineDispatcher,
                                             @Named(DispatcherModule.IO) val workerDispatcher: CoroutineDispatcher) : BaseViewModel(uiDispatcher) {

    val recommendationLiveData: MutableLiveData<LiveDataResult<GamingRecommendationParamResponse>> = MutableLiveData()
    val productLiveData: MutableLiveData<LiveDataResult<List<Visitable<*>>>> = MutableLiveData()

    var pageNumber = 1

    fun getRecommendationParams(pageName: String) {
        launchCatchError(block = {
            withContext(workerDispatcher) {
                val response = paramUseCase.getResponse(paramUseCase.getRequestParams(pageName))
                recommendationLiveData.postValue(LiveDataResult.success(response))
                getProducts()
            }
        }, onError = {
            Log.e("NOOB - Params", it.message)
            it.printStackTrace()
            recommendationLiveData.postValue(LiveDataResult.error(it))
        })
    }

    fun getProducts() {

        launchCatchError(block = {
            withContext(workerDispatcher) {
                val params = recommendationLiveData.value!!.data!!.params
                val item = recommendationProductUseCase.getData(recommendationProductUseCase.getRequestParams(params, pageNumber)).first()
                val list = recommendationProductUseCase.mapper.recommWidgetToListOfVisitables(item)
                productLiveData.postValue(LiveDataResult.success(list))
            }
        }, onError = {
            Log.e("NOOB - Product", it.message)
            productLiveData.postValue(LiveDataResult.error(it))
        })

    }

    override fun onCleared() {
        super.onCleared()
        recommendationProductUseCase.unsubscribe()
    }
}