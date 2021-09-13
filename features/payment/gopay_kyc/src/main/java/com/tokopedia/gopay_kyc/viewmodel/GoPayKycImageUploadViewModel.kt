package com.tokopedia.gopay_kyc.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gopay_kyc.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import javax.inject.Inject

class GoPayKycImageUploadViewModel @Inject constructor(
    // image upload use case here
    // update kyc upload status
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    var ktpPath = ""
    var selfieKtpPath = ""
    val uploadSuccessLiveData = MutableLiveData<Boolean>()

    fun uploadImage() {
        Log.d("GoPay KYC", "$ktpPath ====> $selfieKtpPath")
        launchCatchError(
            block = {
                delay(2000)
                updateKycUploadStatus()
            },
            onError = {
                uploadSuccessLiveData.postValue(false)
            })
    }

    private fun updateKycUploadStatus() {
        var mockFalse = true
        uploadSuccessLiveData.postValue(mockFalse)
        //uploadSuccessLiveData.postValue(true)
    }

    private fun onSaveSuccess() {
    }

    private fun onSaveError(throwable: Throwable) {

    }
}