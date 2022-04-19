package com.tokopedia.gopay.kyc.presentation.listener

import com.tokopedia.gopay.kyc.analytics.GoPayKycEvent

interface GoPayKycNavigationListener {

    fun openKtpCameraScreen()
    fun openSelfieKtpCameraScreen()
    fun exitKycFlow()
    fun sendAnalytics(event: GoPayKycEvent)
}