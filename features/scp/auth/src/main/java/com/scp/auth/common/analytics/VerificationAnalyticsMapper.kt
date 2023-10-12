package com.scp.auth.common.analytics

import com.scp.verification.core.domain.common.infrastructure.CVEventFieldName
import com.scp.verification.core.domain.common.infrastructure.CVEventName
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class VerificationAnalyticsMapper @Inject constructor(private val gtm: ContextAnalytics, private val userSession: UserSessionInterface) {

    fun trackEventCvSdk(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerIdEventFactory(eventName),
                CLICK_ACCOUNT_EVENT,
                trackerCategoryFactory(eventName),
                eventName,
                createEventLabelCvSdk(param).createEventLabelClick(),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackScreenCvSdk(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                TRACKER_ID_SCREEN_VIEW,
                VIEW_ACCOUNT_EVENT,
                trackerCategoryFactory(eventName),
                eventName,
                createEventLabelCvSdk(param).createEventLabelView(),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackFailedCvSdk(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                TRACKER_ID_INPUT_SCREEN_FAILED,
                FAILED_ACCOUNT_EVENT,
                trackerCategoryFactory(eventName),
                eventName,
                createEventLabelCvSdk(param).createEventLabelFailed(),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackErrorPopupViewCvSdk(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                TRACKER_ID_INPUT_SCREEN_ERROR_POPUP_VIEW,
                VIEW_ACCOUNT_EVENT,
                trackerCategoryFactory(eventName),
                eventName,
                createEventLabelCvSdk(param).createEventLabelPopupView(),
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

    private fun trackerIdEventFactory(eventName: String): String {
        return when (eventName) {
            CVEventName.CVSDK_HELP_CLICKED -> TRACKER_ID_HELP_CLICKED
            CVEventName.CVSDK_BACK_CLICKED -> TRACKER_ID_BACK_CLICKED
            CVEventName.CVSDK_INPUT_SCREEN_SUBMIT_SUCCESSFUL -> TRACKER_ID_INPUT_SCREEN_SUBMIT_SUCCESSFUL
            CVEventName.CVSDK_INPUT_SCREEN_RESEND_CLICKED -> TRACKER_ID_INPUT_SCREEN_RESEND_CLICKED
            CVEventName.CVSDK_INPUT_SCREEN_RESEND_SUCCESSFUL -> TRACKER_ID_INPUT_SCREEN_RESEND_SUCCESSFUL
            CVEventName.CVSDK_INITIATE_CALL_SUCCESSFUL -> TRACKER_ID_INITIATE_CALL_SUCCESSFUL
            CVEventName.CVSDK_METHOD_SELECTED -> TRACKER_ID_METHOD_SELECTED
            CVEventName.CVSDK_METHOD_SELECTION_SUCCESSFUL -> TRACKER_ID_METHOD_SELECTION_SUCCESSFUL
            CVEventName.CVSDK_INPUT_SCREEN_TRY_ANOTHER_WAY_CLICKED -> TRACKER_ID_INPUT_SCREEN_TRY_ANOTHER_METHOD_CLICKED
            CVEventName.CVSDK_INPUT_SCREEN_OPEN_WHATSAPP_CLICKED -> TRACKER_ID_INPUT_SCREEN_OPEN_WHATSAPP_CLICKED
            CVEventName.CVSDK_INPUT_SCREEN_FORGOT_PIN_CLICKED -> TRACKER_ID_INPUT_SCREEN_FORGOT_PIN_CLICKED
            CVEventName.CVSDK_INPUT_SCREEN_SUBMIT -> TRACKER_ID_INPUT_SCREEN_SUBMIT
            CVEventName.CVSDK_INPUT_SCREEN_FORGOT_PASSWORD_CLICKED -> TRACKER_ID_INPUT_SCREEN_FORGOT_PASSWORD_CLICKED
            CVEventName.CVSDK_VALIDATE_CALL_SUCCESSFUL -> TRACKER_ID_VALIDATE_CALL_SUCCESSFUL
            else -> ""
        }
    }

    private fun trackerCategoryFactory(eventName: String): String {
        return when (eventName) {
            CVEventName.CVSDK_METHOD_SELECTED, CVEventName.CVSDK_METHOD_SELECTION_SUCCESSFUL -> CVSDK_METHOD_SCREEN_CATEGORY
            CVEventName.CVSDK_INPUT_SCREEN_SUBMIT_SUCCESSFUL, CVEventName.CVSDK_INPUT_SCREEN_RESEND_CLICKED,
            CVEventName.CVSDK_INPUT_SCREEN_RESEND_SUCCESSFUL, CVEventName.CVSDK_INPUT_SCREEN_TRY_ANOTHER_WAY_CLICKED,
            CVEventName.CVSDK_INPUT_SCREEN_OPEN_WHATSAPP_CLICKED, CVEventName.CVSDK_INPUT_SCREEN_FORGOT_PIN_CLICKED,
            CVEventName.CVSDK_INPUT_SCREEN_FORGOT_PASSWORD_CLICKED, CVEventName.CVSDK_VALIDATE_CALL_SUCCESSFUL,
            CVEventName.CVSDK_INPUT_SCREEN_SUBMIT -> CVSDK_INPUT_SCREEN_CATEGORY
            CVEventName.CVSDK_ERROR_POPUP_VIEWED -> CVSDK_ERROR_POPUP_CATEGORY
            else -> CVSDK_SCREEN_CATEGORY
        }
    }

    companion object {
        private const val FAILED_ACCOUNT_EVENT = "todo please replace with the correct value"
        private const val CLICK_ACCOUNT_EVENT = "clickAccount"
        private const val VIEW_ACCOUNT_EVENT = "viewAccountIris"
        private const val CVSDK_SCREEN_CATEGORY = "cvsdk screen"
        private const val CVSDK_INPUT_SCREEN_CATEGORY = "cvsdk input screen"
        private const val CVSDK_METHOD_SCREEN_CATEGORY = "cvsdk method selection screen"
        private const val CVSDK_ERROR_POPUP_CATEGORY = "cvsdk error popup"

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

        // Tracker ID
        private const val TRACKER_ID_SCREEN_VIEW = "47627"
        private const val TRACKER_ID_HELP_CLICKED = "47628"
        private const val TRACKER_ID_BACK_CLICKED = "47629"
        private const val TRACKER_ID_INITIATE_CALL_SUCCESSFUL = "47630"
        private const val TRACKER_ID_METHOD_SELECTED = "47631"
        private const val TRACKER_ID_METHOD_SELECTION_SUCCESSFUL = "47632"
        private const val TRACKER_ID_INPUT_SCREEN_SUBMIT_SUCCESSFUL = "47633"
        private const val TRACKER_ID_INPUT_SCREEN_RESEND_CLICKED = "47634"
        private const val TRACKER_ID_INPUT_SCREEN_RESEND_SUCCESSFUL = "47635"
        private const val TRACKER_ID_INPUT_SCREEN_TRY_ANOTHER_METHOD_CLICKED = "47636"
        private const val TRACKER_ID_INPUT_SCREEN_OPEN_WHATSAPP_CLICKED = "47638"
        private const val TRACKER_ID_INPUT_SCREEN_FORGOT_PIN_CLICKED = "47639"
        private const val TRACKER_ID_INPUT_SCREEN_FORGOT_PASSWORD_CLICKED = "47640"
        private const val TRACKER_ID_INPUT_SCREEN_FAILED = "47641"
        private const val TRACKER_ID_INPUT_SCREEN_ERROR_POPUP_VIEW = "47642"
        private const val TRACKER_ID_VALIDATE_CALL_SUCCESSFUL = "47697"
        private const val TRACKER_ID_INPUT_SCREEN_SUBMIT = "47708"
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
