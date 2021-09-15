package com.tokopedia.gopay_kyc.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.network.data.model.RestResponse
import com.tokopedia.gopay_kyc.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.gopay_kyc.domain.data.InitiateKycResponse
import com.tokopedia.gopay_kyc.domain.data.KycDocument
import com.tokopedia.gopay_kyc.domain.usecase.InitiateKycUseCase
import com.tokopedia.gopay_kyc.domain.usecase.SubmitKycUseCase
import com.tokopedia.gopay_kyc.domain.usecase.UploadKycUseCase
import com.tokopedia.network.data.model.response.DataResponse
import kotlinx.coroutines.CoroutineDispatcher
import java.lang.reflect.Type
import rx.Subscriber
import javax.inject.Inject

class GoPayKycImageUploadViewModel @Inject constructor(
    private val initiateKycUseCase: InitiateKycUseCase,
    private val uploadKycUseCase: UploadKycUseCase,
    private val submitKycUseCase: SubmitKycUseCase,
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    var ktpPath = ""
    var selfieKtpPath = ""
    val uploadSuccessLiveData = MutableLiveData<Boolean>()

    private fun uploadImage(document: KycDocument) {
        val imagePath = if (document.documentType == DOCUMENT_TYPE_KYC_PROOF) ktpPath else selfieKtpPath
        uploadKycUseCase.setRequestParams(documentUrl = document.documentUrl, imagePath = imagePath)
        uploadKycUseCase.execute(object : Subscriber<Map<Type, RestResponse>>() {
            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                uploadSuccessLiveData.postValue(false)
            }

            override fun onNext(typeResponse: Map<Type, RestResponse>) {

                val token = object : TypeToken<DataResponse<String?>>() {}.type
                val restResponse: RestResponse? = typeResponse[token]
                if (document.documentType == DOCUMENT_TYPE_SELFIE)
                    uploadSuccessLiveData.postValue(true)
            }
        })
    }

    private fun updateKycUploadStatus(isSuccess: Boolean) {
        var mockFalse = false
        uploadSuccessLiveData.postValue(mockFalse)
    }


    fun submitKycInfo(kycRequestID: Int) {
        submitKycUseCase.submitKyc(
            ::onKycSubmissionSuccess,
            ::onKycSubmissionError,
            kycRequestID
        )
    }

    fun initiateGoPayKyc() {
        initiateKycUseCase.cancelJobs()
        initiateKycUseCase.initiateGoPayKyc(
            ::onKycInitiated,
            ::onKycInitiationFailed
        )
    }

    private fun onKycInitiated(initiateKycResponse: InitiateKycResponse) {
        if (initiateKycResponse.code == CODE_SUCCESS) {
            for(document in initiateKycResponse.initiateKycData.kycDocuments)
                uploadImage(document)
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

    companion object {
        const val CODE_SUCCESS = "SUCCESS"
        const val DOCUMENT_TYPE_KYC_PROOF = "KYC_PROOF"
        const val DOCUMENT_TYPE_SELFIE = "SELFIE"
    }


}