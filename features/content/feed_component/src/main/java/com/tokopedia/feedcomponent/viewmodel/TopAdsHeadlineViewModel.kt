package com.tokopedia.feedcomponent.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.feedcomponent.domain.model.TopAdsHeadlineResponse
import com.tokopedia.feedcomponent.domain.usecase.GetTopAdsHeadlineUseCase
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

class TopAdsHeadlineViewModel : ViewModel(){

    private val graphqlRepository:GraphqlRepository by lazy {
        GraphqlInteractor.getInstance().graphqlRepository
    }

    private val getTopAdsHeadlineUseCase : GetTopAdsHeadlineUseCase by lazy {
        GetTopAdsHeadlineUseCase(graphqlRepository)
    }

    private var headlineAdsLiveData = MutableLiveData<Result<TopAdsHeadlineResponse>>()

    fun getHeadlineAdsLiveData(): LiveData<Result<TopAdsHeadlineResponse>> = headlineAdsLiveData

    fun getTopAdsHeadlineDate(params:String){
        viewModelScope.launchCatchError(
                block = {
                    getTopAdsHeadlineUseCase.setParams(params)
                    val response = getTopAdsHeadlineUseCase.executeOnBackground()
                    headlineAdsLiveData.value = Success(response)
                },
                onError = {
                    headlineAdsLiveData.value = Fail(it)
                }
        )
    }
}