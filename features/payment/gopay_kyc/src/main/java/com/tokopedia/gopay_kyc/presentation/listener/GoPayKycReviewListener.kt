package com.tokopedia.gopay_kyc.presentation.listener

interface GoPayKycReviewListener {

    fun showKycSuccessScreen()
    fun showKycFailedBottomSheet()
    fun uploadImageToServer()

}