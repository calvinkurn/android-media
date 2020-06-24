package com.tokopedia.topads.sdk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

import javax.inject.Inject


class TopAdsImageViewViewModel @Inject constructor(private val topAdsImageViewUseCase: TopAdsImageViewUseCase) : BaseViewModel() {

    private val response: MutableLiveData<Result<ArrayList<TopAdsImageViewModel>>> = MutableLiveData()

    fun getImageData(queryParams: MutableMap<String, Any>) {
        launchCatchError(
                block = {
                    response.value = Success(topAdsImageViewUseCase.getImageData(queryParams))
                },
                onError = {
                    response.postValue(Fail(it))
                })
    }

    fun getQueryParams(query: String,source: String, pageToken: String, adsCount: String, dimenId: Int, depId: String): MutableMap<String, Any> {
        return topAdsImageViewUseCase.getQueryMap(query,source, pageToken, adsCount, dimenId,depId)
    }


    fun getResponse(): LiveData<Result<ArrayList<TopAdsImageViewModel>>> = response

}