package com.scp.auth.verification

import com.scp.verification.core.data.common.services.VerificationServices
import com.scp.verification.core.data.common.services.contract.ScpABTestService
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsEvent
import com.scp.verification.core.data.common.services.contract.ScpAnalyticsService
import com.scp.verification.core.data.common.services.contract.ScpLogService

object VerificationService {
    fun getVerificationService(): VerificationServices {
        return VerificationServices(
            abTestServices = VerificationABTestService(),
            logService = VerificationLogService(),
            analyticsService = VerificationAnalyticsService()
        )
    }
}

class VerificationAnalyticsService : ScpAnalyticsService {
    override fun trackError(eventName: ScpAnalyticsEvent, params: Map<String, Any?>) {
        println("sdkTrack:verif:error: $eventName, $params")
    }

    override fun trackEvent(eventName: ScpAnalyticsEvent, params: MutableMap<String, Any?>) {
        println("sdkTrack:event: $eventName, $params")
    }

    override fun trackView(eventName: ScpAnalyticsEvent, params: Map<String, Any?>) {
        println("sdkTrack:view: $eventName, $params")
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
