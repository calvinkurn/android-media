package com.tokopedia.kyc_centralized.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.domain.KycUploadUseCase
import com.tokopedia.kyc_centralized.util.DispatcherProvider
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.withContext
import javax.inject.Inject

class KycUploadViewModel @Inject constructor(
        private val kycUploadUseCase: KycUploadUseCase,
        private val dispatcher: DispatcherProvider
) : BaseViewModel(dispatcher.main()) {

    private val _kycResponse = MutableLiveData<Result<KycData>>()
    val kycResponseLiveData : LiveData<Result<KycData>>
        get() = _kycResponse

    fun uploadImages(ktpPath: String, facePath: String, tkpdProjectId: String) {
        launchCatchError(block = {
            withContext(dispatcher.io()) {
                val kycUploadResult = kycUploadUseCase.uploadImages(ktpPath, facePath, tkpdProjectId)
                _kycResponse.postValue(Success(kycUploadResult))
            }
        }) {
            _kycResponse.postValue(Fail(it))
        }
    }
}