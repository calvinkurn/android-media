package com.tokopedia.gopay.kyc.presentation.listener

interface GoPayKycReviewListener {

    fun showKycSuccessScreen()
    fun showKycFailedBottomSheet(ktpPath: String, selfieKtpPath: String)

}