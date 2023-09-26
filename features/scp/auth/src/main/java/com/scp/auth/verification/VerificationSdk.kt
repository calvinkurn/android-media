package com.scp.auth.verification

import android.app.Application
import com.scp.verification.CvSdkProvider
import com.scp.verification.GotoVerification
import com.scp.verification.core.CvsdkFlowType

object VerificationSdk {
    var CVSDKINSTANCE: CvSdkProvider? = null

    internal fun getCvSdkProvider(application: Application): CvSdkProvider {
        CVSDKINSTANCE = GotoVerification.getInstance(
            context = application,
            configurations = VerificationSdkConfig(application),
            services = VerificationService.getVerificationService(),
            identifier = CvsdkFlowType.Main
        )
        return CVSDKINSTANCE!!
    }
}
