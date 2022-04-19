package com.tokopedia.gopay.kyc.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gopay.kyc.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.gopay.kyc.domain.data.InitiateKycResponse
import com.tokopedia.gopay.kyc.domain.usecase.InitiateKycUseCase
import com.tokopedia.gopay.kyc.domain.usecase.SubmitKycUseCase
import com.tokopedia.gopay.kyc.domain.usecase.UploadKycDocumentUseCase
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.IllegalStateException
import javax.inject.Inject

class GoPayKycImageUploadViewModel @Inject constructor(
    private val initiateKycUseCase: InitiateKycUseCase,
    private val uploadKycDocumentUseCase: UploadKycDocumentUseCase,
    private val submitKycUseCase: SubmitKycUseCase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    var ktpPath = ""
    var selfieKtpPath = ""
    private var kycRequestId: String = ""
    val uploadSuccessLiveData = MutableLiveData<Boolean>()
    val kycSubmitErrorLiveData = MutableLiveData<Throwable>()

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
            uploadKycDocumentUseCase.uploadKycDocuments({ isUploadSuccessful ->
                if (isUploadSuccessful) {
                    kycRequestId = initiateKycResponse.initiateKycData.kycRequestId
                    submitKycInfo()
                } else uploadKycFailed()
            }, {
                uploadKycFailed()
                kycSubmitErrorLiveData.postValue(it)
            })
        } else {
            uploadKycFailed()
            kycSubmitErrorLiveData.postValue(IllegalStateException(ILLEGAL_STATE))
        }
    }

    private fun onKycInitiationFailed(throwable: Throwable) {
        uploadKycFailed()
        kycSubmitErrorLiveData.postValue(throwable)
    }

    private fun onKycSubmissionSuccess(isSuccess: String) {
        uploadSuccessLiveData.postValue(isSuccess == CODE_SUCCESS)
    }

    private fun onKycSubmissionError(throwable: Throwable) {
        uploadSuccessLiveData.postValue(false)
        kycSubmitErrorLiveData.postValue(throwable)
    }

    private fun uploadKycFailed() {
        uploadSuccessLiveData.postValue(false)
    }

    companion object {
        const val CODE_SUCCESS = "SUCCESS"
        const val DOCUMENT_TYPE_KYC_PROOF = "KYC_PROOF"
        const val ILLEGAL_STATE = "Silakan coba lagi"
    }

    override fun onCleared() {
        super.onCleared()
        initiateKycUseCase.cancelJobs()
        uploadKycDocumentUseCase.cancelJobs()
        submitKycUseCase.cancelJobs()
    }

}