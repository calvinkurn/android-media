package com.tokopedia.topads.sdk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

import javax.inject.Inject


class TopAdsImageViewViewModel @Inject constructor(private val topAdsImageViewUseCase: TopAdsImageViewUseCase) : BaseViewModel() {

    private val response: MutableLiveData<Result<TopAdsImageViewResponse>> = MutableLiveData()

    fun getImageData(query: String, queryParams: Map<String, Any>) {
        //will use to fetch data from usecase
        launchCatchError(
                block = {
                    response.value = Success(topAdsImageViewUseCase.getImageData("", queryParams))
                },
                onError = {
                    response.postValue(Fail(it))
                })
    }

    fun getQueryParams(source: String, pageToken: String, adsCount: Int, dimens: String): Map<String, Any> {
        return topAdsImageViewUseCase.getQueryMap(source, pageToken, adsCount, dimens)
    }


    fun getResponse(): LiveData<Result<TopAdsImageViewResponse>> = response

}