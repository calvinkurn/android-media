package com.tokopedia.gopay_kyc.presentation.listener

interface GoPayKycFlowListener {

    fun showKycSuccessScreen()
    fun showKycFailedBottomSheet()
    fun exitKycFlow()

}