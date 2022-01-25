package com.tokopedia.topads.sdk.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.repository.TopAdsRepository
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlin.coroutines.CoroutineContext


class TopAdsImageViewViewModel constructor(application: Application) :AndroidViewModel(application), CoroutineScope {

    private val response: MutableLiveData<Result<ArrayList<TopAdsImageViewModel>>> = MutableLiveData()
    private val viewModelJob = SupervisorJob()
    private var useCase: TopAdsImageViewUseCase =
            TopAdsImageViewUseCase(UserSession(application.applicationContext).userId, TopAdsRepository())

    fun getImageData(queryParams: MutableMap<String, Any>, sessionId: String) {
        launchCatchError(
                block = {
                    response.postValue(Success(useCase.getImageData(queryParams, sessionId)))
                },
                onError = {
                    response.postValue(Fail(it))
                })
    }

    fun getQueryParams(query: String, source: String, pageToken: String, adsCount: Int, dimenId: Int, depId: String, productID: String, page: String = ""): MutableMap<String, Any> {
        return useCase.getQueryMap(query, source, pageToken, adsCount, dimenId, depId, productID, page)
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