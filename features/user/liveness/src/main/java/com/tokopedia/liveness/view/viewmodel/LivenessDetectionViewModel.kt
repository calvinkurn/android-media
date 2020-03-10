package com.tokopedia.liveness.view.viewmodel

import com.tokopedia.liveness.data.model.response.LivenessData
import com.tokopedia.liveness.domain.UploadLivenessResultUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LivenessDetectionViewModel @Inject constructor(
        private val uploadLivenessResultUseCase: UploadLivenessResultUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _livenessResponse = MutableLiveData<Result<LivenessData>>()
    val livenessResponseLiveData : LiveData<Result<LivenessData>>
        get() = _livenessResponse

    fun uploadImages(ktpPath: String, facePath: String, tkpdProjectId: String) {
        launchCatchError(block = {
            withContext(Dispatchers.IO) {
                val livenessResponseResult = uploadLivenessResultUseCase.uploadImages(ktpPath, facePath, tkpdProjectId)
                _livenessResponse.postValue(Success(livenessResponseResult))
            }
        }) {
            _livenessResponse.postValue(Fail(it))
        }
    }
}