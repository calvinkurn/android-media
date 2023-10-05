package com.scp.auth.analytics

import com.scp.login.core.domain.common.infrastructure.LSdkScreenName
import com.scp.verification.core.domain.common.infrastructure.CVEventFieldName
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

class AuthAnalyticsMapper {

    private val gtm = TrackApp.getInstance().gtm
    fun trackScreenLsdk(eventName: String, param: Map<String, Any?>) {
        gtm.sendGeneralEvent(
            createData(
                trackerIdEventFactory(eventName),
                eventName,
                createScreenCategory(getSource(param)),
                createScreenAction(getSource(param)),
                getTransactionId(param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

//    fun trackHelpClicked(eventName: String, param: Map<String, Any?>) {
//        gtm.sendGTMGeneralEvent()
//    }

    private fun createScreenCategory(page: String): String =
        "goto $page page"

    private fun createScreenAction(page: String): String = "view on $page"

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

    private fun getSource(param: Map<String, Any?>): String = param[CVEventFieldName.SOURCE].toString()

    private fun getTransactionId(param: Map<String, Any?>): String = param[CVEventFieldName.TRANSACTION_ID].toString()

    private fun getSdkVersion(param: Map<String, Any?>): String = param[CVEventFieldName.SDK_VERSION].toString()

    companion object {
        private const val FAILED_ACCOUNT_EVENT = "todo please replace with the correct value"
        private const val CLICK_ACCOUNT_EVENT = "clickAccount"
        private const val VIEW_ACCOUNT_EVENT = "viewAccountIris"
        private const val CVSDK_VERIFICATION_CATEGORY = "cvsdk verification page"

        private const val ANDROID_ID = "androidId"
        private const val SDK_VERSION = "sdkVersion"
        private const val BUSINESS_UNIT = "businessUnit"
        private const val TRACKER_ID = "trackerId"

        // screen
        private const val SSO_LOGIN_SCREEN = "sso"
        private const val MULTIPLE_SSO = "multiple sso"
        private const val LOGIN_SCREEN = "login"
    }
}
