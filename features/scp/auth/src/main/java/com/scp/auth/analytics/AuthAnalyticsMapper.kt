package com.scp.auth.analytics

import com.scp.login.core.domain.common.infrastructure.LSdkAnalyticFieldName
import com.scp.login.core.domain.common.infrastructure.LSdkEventName
import com.scp.login.core.domain.common.infrastructure.LSdkScreenName
import com.scp.verification.core.domain.common.infrastructure.CVEventFieldName
import com.scp.verification.core.domain.common.infrastructure.CVEventName
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class AuthAnalyticsMapper {

    private val gtm = TrackApp.getInstance().gtm
    fun trackScreenLsdk(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerIdEventFactory(eventName),
                VIEW_ACCOUNT_EVENT,
                createScreenCategory(getSource(param)),
                createScreenAction(getSource(param)),
                getTransactionId(param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackClickedLsdk(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerIdEventFactory(eventName),
                CLICK_ACCOUNT_EVENT,
                createScreenCategory(getSource(param)),
                createClickAction(clickTypeFactory(eventName)),
                getTransactionId(param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackVerificationLsdk(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerIdEventFactory(eventName),
                VIEW_ACCOUNT_EVENT,
                MFA_CATEGORY,
                createScreenAction("mfa trigger"),
                getVerificationLabel(eventName, param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackLoginLsdk(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerIdEventFactory(eventName),
                CLICK_ACCOUNT_EVENT,
                createScreenCategory("sso login"),
                createClickAction(clickTypeFactory(eventName)),
                getLoginLabel(eventName, param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackOtherAccountClicked(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerIdEventFactory(eventName),
                CLICK_ACCOUNT_EVENT,
                createScreenCategory("sso login"),
                createClickAction(clickTypeFactory(eventName)),
                getLoginLabel(eventName, param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackInputCredential(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerIdEventFactory(eventName),
                CLICK_ACCOUNT_EVENT,
                createScreenCategory("login"),
                createClickAction(clickTypeFactory(eventName)),
                getCredentialScreenLabel(eventName, param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackPopupView(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerIdEventFactory(eventName),
                VIEW_ACCOUNT_EVENT,
                LSdkEventName.LSDK_POPUP_VIEW
            )
        )
    }

    private fun createScreenCategory(page: String): String =
        "goto $page page"

    private fun createScreenAction(page: String): String = "view on $page"

    private fun createClickAction(type: String): String = "click on $type"

    private fun createData(
        trackerId: String,
        event: String,
        category: String,
        action: String,
        label: String,
        customDimension: Map<String, Any>? = null
    ): MutableMap<String, Any> {
        val commonData = TrackAppUtils.gtmData(event, category, action, label)
        commonData.put(TRACKER_ID, trackerId)
        if (customDimension != null) {
            commonData.putAll(customDimension)
        }
        return commonData
    }

    private fun createCustomDimension(sdkVersion: String): Map<String, Any> {
        return mapOf(SDK_VERSION to sdkVersion, BUSINESS_UNIT to "")
    }

    private fun mapScreenView(lsdkScreen: String): String {
        return when (lsdkScreen) {
            LSdkScreenName.SCREEN_LANDING -> SSO_LOGIN_SCREEN
            LSdkScreenName.SCREEN_SEAMLESS_ACC_SELECTION -> MULTIPLE_SSO
            LSdkScreenName.SCREEN_ACCOUNT_SELECTION -> ""
            LSdkScreenName.SCREEN_INPUT_CREDS -> LOGIN_SCREEN
            else -> ""
        }
    }

    private fun trackerIdEventFactory(event: String): String {
        return ""
    }

    private fun clickTypeFactory(event: String): String {
        return when (event) {
            CVEventName.CVSDK_HELP_CLICKED -> "icon help"
            CVEventName.CVSDK_BACK_CLICKED -> "button back"
            LSdkEventName.LSDK_TNC_CLICKED -> "terms of service"
            LSdkEventName.LSDK_PRIVACY_POLICY_CLICKED -> "privacy policy"
            LSdkEventName.LSDK_LOGIN_SUCCESS, LSdkEventName.LSDK_LOGIN_FAIL -> "continue"
            LSdkEventName.LSDK_CTA_OTHER_ACC -> "continue with other account"
            LSdkEventName.LSDK_INPUT_BOX_CLICKED -> "input box"
            LSdkEventName.LSDK_ACC_RECOVERY_CLICKED -> "cannot access account"
            LSdkEventName.LSDK_CONTINUE_CTA_CLICKED -> "continue login"
            LSdkEventName.LSDK_SOCMED_CTA_CLICKED -> "login with google sso"
            else -> ""
        }
    }

    private fun getSource(param: Map<String, Any?>): String = param[CVEventFieldName.SOURCE].toString()

    private fun getTransactionId(param: Map<String, Any?>): String = param[CVEventFieldName.TRANSACTION_ID].toString()

    private fun getSdkVersion(param: Map<String, Any?>): String = param[CVEventFieldName.SDK_VERSION].toString()

    private fun getVerificationType(param: Map<String, Any?>): String = param[LSdkAnalyticFieldName.VERIFICATION_TYPE].toString()

    private fun getVerificationLabel(eventName: String, param: Map<String, Any?>): String {
        val type = getVerificationType(param)
        return when (eventName) {
            LSdkEventName.LSDK_VERIFICATION_TRIGGERED -> "$type - triggered"
            LSdkEventName.LSDK_VERIFICATION_FAIL -> {
                val errorMsg = param[CVEventFieldName.ERROR_MESSAGE]
                return "$type - failed - $errorMsg"
            }
            LSdkEventName.LSDK_VERIFICATION_SUCCESS -> "$type - success"
            else -> ""
        }
    }

    private fun getLoginLabel(eventName: String, param: Map<String, Any?>): String {
        val transactionId = getTransactionId(param)
        return when (eventName) {
            LSdkEventName.LSDK_LOGIN_SUCCESS -> "success - login - $transactionId"
            LSdkEventName.LSDK_LOGIN_FAIL -> "failed - login - ${param[CVEventFieldName.ERROR_MESSAGE]} - $transactionId"
            else -> ""
        }
    }

    private fun getCredentialScreenLabel(eventName: String, param: Map<String, Any?>): String {
        return when (eventName) {
            LSdkEventName.LSDK_INPUT_BOX_CLICKED, LSdkEventName.LSDK_ACC_RECOVERY_CLICKED -> getTransactionId(param)
            LSdkEventName.LSDK_CONTINUE_CTA_CLICKED -> "click - ${getSource(param)} - ${getTransactionId(param)}"
            LSdkEventName.LSDK_SOCMED_CTA_CLICKED -> "click - ${getTransactionId(param)}"
            else -> ""
        }
    }

    companion object {
        private const val FAILED_ACCOUNT_EVENT = "todo please replace with the correct value"
        private const val CLICK_ACCOUNT_EVENT = "clickAccount"
        private const val VIEW_ACCOUNT_EVENT = "viewAccountIris"
        private const val CVSDK_VERIFICATION_CATEGORY = "cvsdk verification page"

        private const val ANDROID_ID = "androidId"
        private const val SDK_VERSION = "sdkVersion"
        private const val BUSINESS_UNIT = "businessUnit"
        private const val TRACKER_ID = "trackerId"

        // static category
        private const val MFA_CATEGORY = "goto login mfa"

        // static action
        private const val MFA_ACTION = ""

        // screen
        private const val SSO_LOGIN_SCREEN = "sso"
        private const val MULTIPLE_SSO = "multiple sso"
        private const val LOGIN_SCREEN = "login"
    }
}
