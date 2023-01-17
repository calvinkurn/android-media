package com.tokopedia.topads.sdk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject


class TopAdsImageViewViewModel @Inject constructor(private val useCase: TopAdsImageViewUseCase) : BaseViewModel(Dispatchers.Main), CoroutineScope {

    private val response: MutableLiveData<Result<ArrayList<TopAdsImageViewModel>>> = MutableLiveData()

    fun getImageData(queryParams: MutableMap<String, Any>) {
        launchCatchError(
                block = {
                    response.postValue(Success(useCase.getImageData(queryParams)))
                },
                onError = {
                    response.postValue(Fail(it))
                })
    }

    fun getQueryParams(query: String, source: String, pageToken: String, adsCount: Int, dimenId: Int, depId: String, productID: String, page: String = ""): MutableMap<String, Any> {
        return useCase.getQueryMap(query, source, pageToken, adsCount, dimenId, depId, productID, page)
    }


    fun getResponse(): LiveData<Result<ArrayList<TopAdsImageViewModel>>> = response

}