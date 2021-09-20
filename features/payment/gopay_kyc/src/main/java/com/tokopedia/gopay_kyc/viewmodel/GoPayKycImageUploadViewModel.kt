package com.tokopedia.gopay_kyc.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gopay_kyc.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.gopay_kyc.domain.data.InitiateKycResponse
import com.tokopedia.gopay_kyc.domain.usecase.InitiateKycUseCase
import com.tokopedia.gopay_kyc.domain.usecase.SubmitKycUseCase
import com.tokopedia.gopay_kyc.domain.usecase.UploadKycDocumentUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GoPayKycImageUploadViewModel @Inject constructor(
    private val initiateKycUseCase: InitiateKycUseCase,
    private val uploadKycDocumentUseCase: UploadKycDocumentUseCase,
    private val submitKycUseCase: SubmitKycUseCase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    var ktpPath = ""
    var selfieKtpPath = ""
    var kycRequestId: String = ""
    val uploadSuccessLiveData = MutableLiveData<Boolean>()

    // to prevent unwanted re-initiate kyc call in that case where submit kyc has failed
    private fun isKycUploadRequired() = kycRequestId.isEmpty()

    private fun submitKycInfo() {
        submitKycUseCase.submitKyc(
            ::onKycSubmissionSuccess,
            ::onKycSubmissionError,
            kycRequestId
        )
    }

    fun initiateGoPayKyc() {
        if (isKycUploadRequired()) {
            initiateKycUseCase.cancelJobs()
            initiateKycUseCase.initiateGoPayKyc(
                ::onKycInitiated,
                ::onKycInitiationFailed
            )
        } else submitKycInfo()
    }

    private fun onKycInitiated(initiateKycResponse: InitiateKycResponse) {
        if (initiateKycResponse.code == CODE_SUCCESS) {
            uploadKycDocumentUseCase.setRequestParams(
                initiateKycResponse.initiateKycData.kycDocuments,
                ktpPath,
                selfieKtpPath
            )
            uploadKycDocumentUseCase.uploadKycDocuments({
                kycRequestId = initiateKycResponse.initiateKycData.kycRequestId
                submitKycInfo()
            }, {
                updateKycUploadStatus(false)
            })
        } else updateKycUploadStatus(false)
    }

    private fun onKycInitiationFailed(throwable: Throwable) {
        updateKycUploadStatus(false)
    }

    private fun onKycSubmissionSuccess(isSuccess: String) {
        uploadSuccessLiveData.postValue(isSuccess == CODE_SUCCESS)
    }

    private fun onKycSubmissionError(throwable: Throwable) {
        uploadSuccessLiveData.postValue(false)
    }

    private fun updateKycUploadStatus(isSuccess: Boolean) {
        uploadSuccessLiveData.postValue(isSuccess)
    }

    companion object {
        const val CODE_SUCCESS = "SUCCESS"
        const val DOCUMENT_TYPE_KYC_PROOF = "KYC_PROOF"
        const val DOCUMENT_TYPE_SELFIE = "SELFIE"
    }

    override fun onCleared() {
        super.onCleared()
        initiateKycUseCase.cancelJobs()
        uploadKycDocumentUseCase.cancelJobs()
        submitKycUseCase.cancelJobs()
    }

}