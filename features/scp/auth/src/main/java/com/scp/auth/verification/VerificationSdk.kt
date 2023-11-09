package com.scp.auth.verification

import android.app.Application
import com.scp.auth.GotoSdk
import com.scp.verification.CvSdkProvider
import com.scp.verification.GotoVerification
import com.scp.verification.core.CvsdkFlowType
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsService
import javax.inject.Inject

object VerificationSdk : IVerificationSdk() {
    private var _CVSDKINSTANCE: CvSdkProvider? = null

    fun getInstance(): CvSdkProvider? = _CVSDKINSTANCE

    internal fun getCvSdkProvider(application: Application): CvSdkProvider {
        if (isAnalyticsServiceInitialized().not()) {
            GotoSdk.getVerifComponent()?.inject(this)
        }
        _CVSDKINSTANCE = GotoVerification.getInstance(
            context = application,
            configurations = VerificationSdkConfig(application),
            services = VerificationService.getVerificationService(analyticsService),
            identifier = CvsdkFlowType.Main
        )
        return _CVSDKINSTANCE!!
    }
}

abstract class IVerificationSdk {
    @Inject
    lateinit var analyticsService: ScpAnalyticsService

    fun isAnalyticsServiceInitialized() = this::analyticsService.isInitialized
}
