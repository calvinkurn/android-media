package com.tokopedia.topads.sdk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive

import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


class TopAdsImageViewViewModel @Inject constructor(private val topAdsImageViewUseCase: TopAdsImageViewUseCase) :ViewModel(), CoroutineScope {

    private val response: MutableLiveData<Result<ArrayList<TopAdsImageViewModel>>> = MutableLiveData()
    private val viewModelJob = SupervisorJob()

    fun getImageData(queryParams: MutableMap<String, Any>) {
        launchCatchError(
                block = {
                    response.postValue(Success(topAdsImageViewUseCase.getImageData(queryParams)))
                },
                onError = {
                    response.postValue(Fail(it))
                })
    }

    fun getQueryParams(query: String,source: String, pageToken: String, adsCount: Int, dimenId: Int, depId: String): MutableMap<String, Any> {
        return topAdsImageViewUseCase.getQueryMap(query,source, pageToken, adsCount, dimenId,depId)
    }


    fun getResponse(): LiveData<Result<ArrayList<TopAdsImageViewModel>>> = response

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + viewModelJob

    fun onClear(){
        onCleared()
    }

    private fun cancelJob(){
        if (isActive && !viewModelJob.isCancelled){
            viewModelJob.cancel()
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelJob()
    }

}