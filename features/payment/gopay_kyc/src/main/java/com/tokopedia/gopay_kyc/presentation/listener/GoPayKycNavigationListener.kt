package com.tokopedia.gopay_kyc.presentation.listener

import com.tokopedia.gopay_kyc.analytics.GoPayKycEvent

interface GoPayKycNavigationListener {

    fun openKtpCameraScreen()
    fun openSelfieKtpCameraScreen()
    fun exitKycFlow()
    fun sendAnalytics(event: GoPayKycEvent)
}