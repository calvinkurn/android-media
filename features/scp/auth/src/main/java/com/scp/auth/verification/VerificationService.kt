package com.scp.auth.verification

import com.scp.verification.core.data.common.services.VerificationServices
import com.scp.verification.core.data.common.services.contract.CVSdkAnalyticsEvent
import com.scp.verification.core.data.common.services.contract.ScpABTestService
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsEvent
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsService
import com.scp.verification.core.data.common.services.contract.ScpLogService
import javax.inject.Inject

object VerificationService {
    fun getVerificationService(analyticsService: ScpAnalyticsService): VerificationServices {
        return VerificationServices(
            abTestServices = VerificationABTestService(),
            logService = VerificationLogService(),
            analyticsService = analyticsService
        )
    }
}

class VerificationAnalyticsService @Inject constructor(
    val verificationAnalyticsMapper: VerificationAnalyticsMapper
) : ScpAnalyticsService {
    override fun trackError(eventName: ScpAnalyticsEvent, params: Map<String, Any?>) {
        if (eventName is CVSdkAnalyticsEvent.VerificationErrorPopupShown) {
            verificationAnalyticsMapper.trackErrorPopupViewCvSdk("", eventName.eventName, params)
        } else {
            verificationAnalyticsMapper.trackFailedCvSdk("", eventName.eventName, params)
        }
    }

    override fun trackEvent(eventName: ScpAnalyticsEvent, params: MutableMap<String, Any?>) {
        verificationAnalyticsMapper.trackEventCvSdk("", eventName.eventName, params)
    }

    override fun trackView(eventName: ScpAnalyticsEvent, params: Map<String, Any?>) {
        verificationAnalyticsMapper.trackScreenCvSdk("", eventName.eventName, params)
    }
}

class VerificationABTestService : ScpABTestService

class VerificationLogService : ScpLogService {
    override fun log(logLevel: Int, tag: String, message: String) {
        /* no-op */
    }

    override fun log(message: String) {
        /* no-op */
    }

    override fun logException(exception: Throwable) {
        /* no-op */
    }

    override fun setUserId(userId: String) {
        /* no-op */
    }
}
