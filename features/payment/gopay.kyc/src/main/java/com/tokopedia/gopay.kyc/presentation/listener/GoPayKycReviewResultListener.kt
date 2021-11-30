package com.tokopedia.gopay.kyc.presentation.listener

interface GoPayKycReviewResultListener {

    fun showKycSuccessScreen()
    fun showKycFailedBottomSheet(ktpPath: String, selfieKtpPath: String)

}