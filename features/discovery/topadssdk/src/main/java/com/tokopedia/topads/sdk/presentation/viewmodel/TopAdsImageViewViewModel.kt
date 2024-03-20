package com.tokopedia.topads.sdk.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel
import com.tokopedia.topads.sdk.domain.usecase.TopAdsImageViewUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TopAdsImageViewViewModel @Inject constructor(
    private val useCase: TopAdsImageViewUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val response: MutableLiveData<Result<ArrayList<TopAdsImageUiModel>>> = MutableLiveData()

    fun getImageData(queryParams: MutableMap<String, Any>) {
        launchCatchError(
            block = {
                val result = withContext(dispatcher.io) {
                    useCase.getImageData(queryParams)
                }
                response.value = Success(result)
            },
            onError = {
                response.value = Fail(it)
            }
        )
    }

    fun getQueryParams(query: String, source: String, pageToken: String, adsCount: Int, dimenId: Int, depId: String, productID: String, page: String = ""): MutableMap<String, Any> {
        return useCase.getQueryMap(query, source, pageToken, adsCount, dimenId, depId, productID, page)
    }

    fun getResponse(): LiveData<Result<ArrayList<TopAdsImageUiModel>>> = response
}
