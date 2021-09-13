package com.tokopedia.gopay_kyc.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gopay_kyc.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.gopay_kyc.domain.data.CameraImageResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GoPayKycImageUploadViewModel @Inject constructor(
    // image upload use case here
    @CoroutineMainDispatcher val dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    var ktpPath = ""
    var selfieKtpPath = ""

    fun uploadImage() {

    }

    private fun onSaveSuccess() {
    }

    private fun onSaveError(throwable: Throwable) {

    }
}