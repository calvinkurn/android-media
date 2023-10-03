package com.scp.auth.verification

import com.scp.verification.core.domain.common.infrastructure.CVEventFieldName
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class VerificationAnalyticsMapper @Inject constructor(private val gtm: ContextAnalytics, private val userSession: UserSessionInterface) {

    fun trackClickCvSdk(trackerId: String, eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerId,
                CLICK_ACCOUNT_EVENT,
                CVSDK_VERIFICATION_CATEGORY,
                eventName,
                createEventLabelCvSdk(param).createEventLabelClick(),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackScreenCvSdk(trackerId: String, eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerId,
                VIEW_ACCOUNT_EVENT,
                CVSDK_VERIFICATION_CATEGORY,
                eventName,
                createEventLabelCvSdk(param).createEventLabelView(),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackFailedCvSdk(trackerId: String, eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerId,
                FAILED_ACCOUNT_EVENT,
                CVSDK_VERIFICATION_CATEGORY,
                eventName,
                createEventLabelCvSdk(param).createEventLabelFailed(),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackErrorPopupViewCvSdk(trackerId: String, eventName: String, param: Map<String, String>) {
        gtm.sendGeneralEvent(
            createData(
                trackerId,
                VIEW_ACCOUNT_EVENT,
                CVSDK_VERIFICATION_CATEGORY,
                eventName,
                createEventLabelCvSdk(param).createEventLabelFailed(),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    private fun createData(trackerId: String, event: String, category: String, action: String, label: String, customDimension: Map<String, Any>? = null): MutableMap<String, Any> {
        val commonData = TrackAppUtils.gtmData(event, category, action, label)
        commonData.put(TRACKER_ID, trackerId)
        if (customDimension != null) {
            commonData.putAll(customDimension)
        }
        return commonData
    }

    private fun createCustomDimension(sdkVersion: String): Map<String, Any> {
        return mapOf(ANDROID_ID to userSession.deviceId, SDK_VERSION to sdkVersion, BUSINESS_UNIT to "")
    }

    private fun getSdkVersion(param: Map<String, Any?>): String = param[SDK_VERSION].toString()

    private fun createEventLabelCvSdk(param: Map<String, Any?>) =
        EventLabelCvSdk(
            method = param[CVEventFieldName.METHOD].toString(),
            flow = param[CVEventFieldName.FLOW].toString(),
            transactionId = param[CVEventFieldName.TRANSACTION_ID].toString(),
            type = param[CVEventFieldName.TYPE].toString(),
            countryCode = param[CVEventFieldName.COUNTRY_CODE].toString(),
            source = param[CVEventFieldName.SOURCE].toString(),
            errorCode = param[CVEventFieldName.ERROR_CODE].toString(),
            errorMessage = param[CVEventFieldName.ERROR_MESSAGE].toString(),
            statusCode = param[CVEventFieldName.STATUS_CODE].toString()
        )

    companion object {
        private const val FAILED_ACCOUNT_EVENT = "todo please replace with the correct value"
        private const val CLICK_ACCOUNT_EVENT = "clickAccount"
        private const val VIEW_ACCOUNT_EVENT = "viewAccountIris"
        private const val CVSDK_VERIFICATION_CATEGORY = "cvsdk verification page"

        private const val ANDROID_ID = "androidId"
        private const val SDK_VERSION = "sdkVersion"
        private const val BUSINESS_UNIT = "businessUnit"
        private const val TRACKER_ID = "trackerId"

        // Field Name on Param from CVSDK
        private const val METHOD = "method"
        private const val FLOW = "flow"
        private const val TRANSACTION_ID = "transactionID"
        private const val TYPE = "type"
        private const val COUNTRY_CODE = "countryCode"
    }
}

data class EventLabelCvSdk(
    val method: String,
    val flow: String,
    val transactionId: String,
    val type: String,
    val countryCode: String,
    val source: String,
    val errorCode: String,
    val errorMessage: String,
    val statusCode: String
) {
    fun createEventLabelClick(): String {
        return "$method - $flow - $transactionId - $countryCode"
    }

    fun createEventLabelView(): String {
        return "$method - $flow - $transactionId - $type - $countryCode"
    }

    fun createEventLabelFailed(): String {
        return "$method - $flow - $transactionId - $countryCode - $source - $errorCode - $errorMessage - $statusCode"
    }

    fun createEventLabelPopupView(): String {
        return "$method - $flow - $transactionId - $countryCode - $type - $errorCode - $errorMessage - $statusCode  "
    }
}
