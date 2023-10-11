package com.scp.auth.analytics

import com.scp.login.core.domain.common.infrastructure.LSdkAnalyticFieldName
import com.scp.login.core.domain.common.infrastructure.LSdkEventName
import com.scp.login.core.domain.common.infrastructure.LSdkPopupActionType
import com.scp.login.core.domain.common.infrastructure.LSdkPopupErrorType
import com.scp.login.core.domain.common.infrastructure.LSdkScreenName
import com.scp.login.core.domain.common.infrastructure.LSdkStatusType
import com.scp.login.sso.BuildConfig
import com.scp.verification.core.domain.common.infrastructure.CVEventFieldName
import com.scp.verification.core.domain.common.infrastructure.CVEventName
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object AuthAnalyticsMapper {

    private val gtm = TrackApp.getInstance().gtm
    fun trackScreenLsdk(eventName: String, param: Map<String, Any?>) {
        val lsdkScreen = getSource(param)
        gtm.sendGeneralEvent(
            createData(
                getTrackerIdScreen(lsdkScreen),
                VIEW_ACCOUNT_EVENT,
                createScreenCategory(lsdkScreen),
                createScreenAction(mapScreenView(lsdkScreen)),
                getTransactionId(param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackEventLsdk(eventName: String, param: Map<String, Any?>) {
        when (eventName) {
            LSdkEventName.LSDK_SCREEN_SHOWN, LSdkEventName.LSDK_HELP_CLICKED,
            LSdkEventName.LSDK_BACK_CLICKED, LSdkEventName.LSDK_TNC_CLICKED,
            LSdkEventName.LSDK_PRIVACY_POLICY_CLICKED -> {
                trackCommonClickedLsdk(eventName, param)
            }

            LSdkEventName.LSDK_VERIFICATION_TRIGGERED, LSdkEventName.LSDK_VERIFICATION_SUCCESS, LSdkEventName.LSDK_VERIFICATION_FAIL -> {
                trackVerificationLsdk(eventName, param)
            }

            LSdkEventName.LSDK_LOGIN_SUCCESS, LSdkEventName.LSDK_LOGIN_FAIL, LSdkEventName.LSDK_CTA_OTHER_ACC, LSdkEventName.LSDK_LOGIN_CTA_CLICKED -> {
                trackLoginLsdk(eventName, param)
            }

            LSdkEventName.LSDK_INPUT_BOX_CLICKED, LSdkEventName.LSDK_ACC_RECOVERY_CLICKED,
            LSdkEventName.LSDK_CONTINUE_CTA_CLICKED, LSdkEventName.LSDK_SOCMED_CTA_CLICKED -> trackInputCredential(eventName, param)

            LSdkEventName.LSDK_POPUP_VIEW, LSdkEventName.LSDK_POPUP_ACTION -> trackPopup(eventName, param)
            LSdkEventName.LSDK_LOGOUT_INIT, LSdkEventName.LSDK_LOGOUT_SUCCESS, LSdkEventName.LSDK_LOGOUT_FAIL -> trackLogout(eventName, param)
            LSdkEventName.LSDK_REFRESH_TOKEN_INIT, LSdkEventName.LSDK_REFRESH_TOKEN_FAIL, LSdkEventName.LSDK_REFRESH_TOKEN_SUCCESS -> trackRefreshToken(eventName, param)
            LSdkEventName.LSDK_SSO_ACCOUNT_FETCH_STATUS -> trackSsoAccount(eventName, param)
            LSdkEventName.LSDK_ONETAP_ACCOUNT_FETCH_STATUS -> trackOneTap(eventName, param)
            LSdkEventName.LSDK_ACCOUNT_FETCH_INIT -> trackSeamless(eventName, param)
        }
    }

    fun trackCommonClickedLsdk(eventName: String, param: Map<String, Any?>) {
        val lsdkScreen = getSource(param)
        gtm.sendGeneralEvent(
            createData(
                getTrackerIdClick(eventName, lsdkScreen, param),
                CLICK_ACCOUNT_EVENT,
                createScreenCategory(getSource(param)),
                createClickAction(clickTypeFactory(eventName)),
                getTransactionId(param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackVerificationLsdk(eventName: String, param: Map<String, Any?>) {
        val lsdkScreen = getSource(param)
        gtm.sendGeneralEvent(
            createData(
                getTrackerIdClick(eventName, lsdkScreen, param),
                VIEW_ACCOUNT_EVENT,
                TRIGGER_PAGE_CATEGORY,
                createScreenAction("goto trigger"),
                getVerificationLabel(eventName, param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackLoginLsdk(eventName: String, param: Map<String, Any?>) {
        val lsdkScreen = getSource(param)
        gtm.sendGeneralEvent(
            createData(
                getTrackerIdClick(eventName, lsdkScreen, param),
                CLICK_ACCOUNT_EVENT,
                createScreenCategory("sso login"),
                createClickAction(clickTypeFactory(eventName)),
                getLoginLabel(eventName, param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    fun trackProfileFetch(type: String) {
        gtm.sendGeneralEvent(
            createData(
                "47756",
                VIEW_ACCOUNT_EVENT,
                TRIGGER_PAGE_CATEGORY,
                VIEW_ON_PROFILE_FETCH_ACTION,
                type,
                createCustomDimension(com.scp.login.core.BuildConfig.VERSION_CODE)
            )
        )
    }

    fun trackInputCredential(eventName: String, param: Map<String, Any?>) {
        val lsdkScreen = getSource(param)
        gtm.sendGeneralEvent(
            createData(
                getTrackerIdClick(eventName, lsdkScreen, param),
                CLICK_ACCOUNT_EVENT,
                createScreenCategory("login"),
                createClickAction(clickTypeFactory(eventName)),
                getCredentialScreenLabel(eventName, param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    private fun trackPopup(eventName: String, param: Map<String, Any?>) {
        val lsdkScreen = getSource(param)
        gtm.sendGeneralEvent(
            createData(
                getTrackerIdClick(eventName, lsdkScreen, param),
                getPopupEvent(eventName),
                getPopupCategoryFactory(param),
                getPopupActionType(eventName, param),
                getPopupEventLabel(eventName, param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    private fun trackLogout(eventName: String, param: Map<String, Any?>) {
        val lsdkScreen = getSource(param)
        gtm.sendGeneralEvent(
            createData(
                getTrackerIdClick(eventName, lsdkScreen, param),
                VIEW_ACCOUNT_EVENT,
                TRIGGER_PAGE_CATEGORY,
                LOGOUT_ACTION,
                createEventLabelLogout(eventName, param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    private fun trackRefreshToken(eventName: String, param: Map<String, Any?>) {
        val lsdkScreen = getSource(param)
        gtm.sendGeneralEvent(
            createData(
                getTrackerIdClick(eventName, lsdkScreen, param),
                VIEW_ACCOUNT_EVENT,
                TRIGGER_PAGE_CATEGORY,
                REFRESH_TOKEN_ACTION,
                createEventLabelRefreshToken(eventName, param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    private fun trackSsoAccount(eventName: String, param: Map<String, Any?>) {
        val lsdkScreen = getSource(param)
        gtm.sendGeneralEvent(
            createData(
                getTrackerIdClick(eventName, lsdkScreen, param),
                VIEW_ACCOUNT_EVENT,
                TRIGGER_PAGE_CATEGORY,
                SSO_ACCCOUNT_ACTION,
                createEventLabelSso(param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    // only track triggered
    private fun trackSeamless(eventName: String, param: Map<String, Any?>) {
        val lsdkScreen = getSource(param)
        gtm.sendGeneralEvent(
            createData(
                getTrackerIdClick(eventName, lsdkScreen, param),
                VIEW_ACCOUNT_EVENT,
                TRIGGER_PAGE_CATEGORY,
                SSO_ACCCOUNT_ACTION,
                TRIGGERED_EVENT_LABEL,
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    private fun trackOneTap(eventName: String, param: Map<String, Any?>) {
        val lsdkScreen = getSource(param)
        gtm.sendGeneralEvent(
            createData(
                getTrackerIdClick(eventName, lsdkScreen, param),
                VIEW_ACCOUNT_EVENT,
                TRIGGER_PAGE_CATEGORY,
                ONE_TAP_ACTION,
                createEventLabelSso(param),
                createCustomDimension(getSdkVersion(param))
            )
        )
    }

    private fun getPopupCategoryFactory(param: Map<String, Any?>): String {
        val loginError = "login error"
        return if (param[CVEventFieldName.TYPE] == LSdkPopupErrorType.TokoDialog.status) {
            createPopupCategory(loginError)
        } else {
            createScreenCategory(loginError)
        }
    }

    private fun createScreenCategory(page: String): String =
        "goto $page page"

    private fun createPopupCategory(page: String): String =
        "goto $page popup"

    private fun createScreenAction(page: String): String = "view on $page page"

    private fun createClickAction(type: String): String = "click on $type"

    internal fun createData(
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
            LSdkScreenName.SCREEN_ACCOUNT_SELECTION -> MULTIPLE_ACCOUNT
            LSdkScreenName.SCREEN_INPUT_CREDS -> LOGIN_SCREEN
            else -> ""
        }
    }

    private fun getTrackerIdScreen(lsdkScreen: String): String {
        return when (lsdkScreen) {
            LSdkScreenName.SCREEN_LANDING -> "46263" // sso screen
            LSdkScreenName.SCREEN_SEAMLESS_ACC_SELECTION -> "46269"
            LSdkScreenName.SCREEN_ACCOUNT_SELECTION -> "46281"
            LSdkScreenName.SCREEN_INPUT_CREDS -> "46273"
            else -> ""
        }
    }

    private fun getTrackerIdClick(event: String, lsdkScreen: String, param: Map<String, Any?>): String {
        return when (event) {
            CVEventName.CVSDK_HELP_CLICKED -> {
                when (lsdkScreen) {
                    LSdkScreenName.SCREEN_LANDING -> "" // sso screen
                    LSdkScreenName.SCREEN_SEAMLESS_ACC_SELECTION -> ""
                    LSdkScreenName.SCREEN_ACCOUNT_SELECTION -> ""
                    LSdkScreenName.SCREEN_INPUT_CREDS -> "46278"
                    else -> ""
                }
            }

            CVEventName.CVSDK_BACK_CLICKED -> {
                when (lsdkScreen) {
                    LSdkScreenName.SCREEN_LANDING -> "46266" // sso screen
                    LSdkScreenName.SCREEN_SEAMLESS_ACC_SELECTION -> "46272"
                    LSdkScreenName.SCREEN_ACCOUNT_SELECTION -> "46283"
                    LSdkScreenName.SCREEN_INPUT_CREDS -> "46280"
                    else -> ""
                }
            }

            LSdkEventName.LSDK_TNC_CLICKED -> {
                when (lsdkScreen) {
                    LSdkScreenName.SCREEN_LANDING -> "46267" // sso screen
                    LSdkScreenName.SCREEN_SEAMLESS_ACC_SELECTION -> ""
                    LSdkScreenName.SCREEN_ACCOUNT_SELECTION -> ""
                    LSdkScreenName.SCREEN_INPUT_CREDS -> "46279"
                    else -> ""
                }
            }

            LSdkEventName.LSDK_PRIVACY_POLICY_CLICKED -> "46268"
            LSdkEventName.LSDK_LOGIN_SUCCESS, LSdkEventName.LSDK_LOGIN_FAIL -> "continue"
            LSdkEventName.LSDK_CTA_OTHER_ACC -> "46265"
            LSdkEventName.LSDK_INPUT_BOX_CLICKED -> "46274"
            LSdkEventName.LSDK_ACC_RECOVERY_CLICKED -> "46277"
            LSdkEventName.LSDK_CONTINUE_CTA_CLICKED -> "46275"
            LSdkEventName.LSDK_SOCMED_CTA_CLICKED -> "46276"
            LSdkEventName.LSDK_LOGIN_CTA_CLICKED -> "46264"
            LSdkEventName.LSDK_VERIFICATION_TRIGGERED -> "46284"
            LSdkEventName.LSDK_POPUP_ACTION -> {
                if (param[CVEventFieldName.TYPE] == LSdkPopupErrorType.TokoDialog.status) {
                    if (param[LSdkAnalyticFieldName.ACTION] == LSdkPopupActionType.Cancelled) {
                        return "46297"
                    } else if (param[LSdkAnalyticFieldName.ACTION] == LSdkPopupActionType.Ok) {
                        return "46296"
                    } else {
                        ""
                    }
                } else {
                    return "46292"
                }
            }

            LSdkEventName.LSDK_POPUP_VIEW -> {
                if (param[CVEventFieldName.TYPE] == POPUP_TYPE_DIALOG) {
                    return "46295"
                } else {
                    return "46291"
                }
            }

            LSdkEventName.LSDK_LOGOUT_INIT, LSdkEventName.LSDK_LOGOUT_SUCCESS, LSdkEventName.LSDK_LOGOUT_FAIL -> {
                return "47751"
            }

            LSdkEventName.LSDK_REFRESH_TOKEN_INIT, LSdkEventName.LSDK_REFRESH_TOKEN_SUCCESS, LSdkEventName.LSDK_REFRESH_TOKEN_FAIL -> {
                return "47752"
            }

            LSdkEventName.LSDK_SSO_ACCOUNT_FETCH_STATUS -> {
                return "47753"
            }

            LSdkEventName.LSDK_ONETAP_ACCOUNT_FETCH_STATUS -> {
                return "47754"
            }

            LSdkEventName.LSDK_ACCOUNT_FETCH_INIT -> {
                return "47755"
            }

            else -> ""
        }
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

    private fun getPopupActionType(eventName: String, param: Map<String, Any?>): String {
        return when (eventName) {
            LSdkEventName.LSDK_POPUP_VIEW -> {
                if (param[CVEventFieldName.TYPE] == POPUP_TYPE_DIALOG) {
                    return createScreenAction("error popup")
                } else {
                    return createScreenAction("error page")
                }
            }

            LSdkEventName.LSDK_POPUP_ACTION -> {
                if (param[CVEventFieldName.TYPE] == LSdkPopupErrorType.TokoDialog.status) {
                    if (param[LSdkAnalyticFieldName.ACTION] == LSdkPopupActionType.Cancelled) {
                        return CLICK_BUTTON_CLOSE_POPUP_ACTION
                    } else if (param[LSdkAnalyticFieldName.ACTION] == LSdkPopupActionType.Ok) {
                        return createClickAction(CLICK_BUTTON_TOKOPEDIA_CARE_ACTION)
                    } else {
                        ""
                    }
                } else {
                    return createScreenAction(CLICK_RETRY_ACTION)
                }
            }

            else -> ""
        }
    }

    private fun getPopupEvent(eventName: String): String {
        return when (eventName) {
            LSdkEventName.LSDK_POPUP_VIEW -> VIEW_ACCOUNT_EVENT

            LSdkEventName.LSDK_POPUP_ACTION -> CLICK_ACCOUNT_EVENT
            else -> ""
        }
    }

    private fun getPopupEventLabel(eventName: String, param: Map<String, Any?>): String {
        return when (eventName) {
            LSdkEventName.LSDK_POPUP_VIEW -> {
                if (param[CVEventFieldName.TYPE] == POPUP_TYPE_DIALOG) {
                    return "${setCcuButtonValue(param)} - ${param[CVEventFieldName.ERROR_MESSAGE]} - ${getTransactionId(param)}"
                } else {
                    return "${param[CVEventFieldName.ERROR_TYPE]} - ${getTransactionId(param)}"
                }
            }

            LSdkEventName.LSDK_POPUP_ACTION -> {
                if (param[CVEventFieldName.TYPE] == POPUP_TYPE_DIALOG) {
                    return ""
                } else {
                    return "${param[CVEventFieldName.ERROR_TYPE]} - ${getTransactionId(param)}"
                }
            }

            else -> ""
        }
    }

    private fun createEventLabelLogout(eventName: String, param: Map<String, Any?>): String {
        return when (eventName) {
            LSdkEventName.LSDK_LOGOUT_SUCCESS -> "success"
            LSdkEventName.LSDK_LOGOUT_INIT -> "triggered"
            LSdkEventName.LSDK_LOGOUT_FAIL -> "failed - ${param[CVEventFieldName.ERROR_MESSAGE]}"
            else -> ""
        }
    }

    private fun createEventLabelRefreshToken(eventName: String, param: Map<String, Any?>): String {
        return when (eventName) {
            LSdkEventName.LSDK_REFRESH_TOKEN_SUCCESS -> "success"
            LSdkEventName.LSDK_REFRESH_TOKEN_INIT -> "triggered"
            LSdkEventName.LSDK_REFRESH_TOKEN_FAIL -> "failed - ${param[CVEventFieldName.ERROR_MESSAGE]}"
            else -> ""
        }
    }

    private fun createEventLabelSso(param: Map<String, Any?>): String {
        return when (param[CVEventFieldName.TYPE]) {
            LSdkStatusType.Initiated.status -> "triggered"
            LSdkStatusType.Success.status -> "success"
            LSdkStatusType.Failed.status -> "failed - ${param[CVEventFieldName.ERROR_MESSAGE]}"
            else -> ""
        }
    }

    private fun setCcuButtonValue(param: Map<String, Any?>): String {
        return if (param[LSdkAnalyticFieldName.ERROR_REASON].toString() == "null" || param[LSdkAnalyticFieldName.ERROR_REASON] == "") {
            WITHOUT_CCU_BUTTON_VALUE
        } else {
            CCU_BUTTON_VALUE
        }
    }

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
            LSdkEventName.LSDK_CTA_OTHER_ACC -> transactionId
            LSdkEventName.LSDK_LOGIN_CTA_CLICKED -> "click - $transactionId"
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

    private const val FAILED_ACCOUNT_EVENT = "todo please replace with the correct value"
    private const val CLICK_ACCOUNT_EVENT = "clickAccount"
    internal const val VIEW_ACCOUNT_EVENT = "viewAccountIris"
    private const val CVSDK_VERIFICATION_CATEGORY = "cvsdk verification page"

    private const val ANDROID_ID = "androidId"
    private const val SDK_VERSION = "sdkVersion"
    private const val BUSINESS_UNIT = "businessUnit"
    private const val TRACKER_ID = "trackerId"
    private const val POPUP_TYPE_DIALOG = "dialog"
    private const val CCU_BUTTON_VALUE = "with CCU button"
    private const val WITHOUT_CCU_BUTTON_VALUE = "no CCU button"

    // static event label
    private const val TRIGGERED_EVENT_LABEL = "triggered"

    // static category
    private const val TRIGGER_PAGE_CATEGORY = "goto trigger page"

    // screen
    private const val SSO_LOGIN_SCREEN = "sso"
    private const val MULTIPLE_SSO = "multiple sso"
    private const val MULTIPLE_ACCOUNT = "multiple account"
    private const val LOGIN_SCREEN = "login"

    // static action
    private const val CLICK_BUTTON_CLOSE_POPUP_ACTION = "click on button close"
    private const val CLICK_BUTTON_TOKOPEDIA_CARE_ACTION = "click on contact tokopedia care"
    private const val CLICK_RETRY_ACTION = "click on button coba lagi"
    private const val LOGOUT_ACTION = "view on logout trigger"
    private const val REFRESH_TOKEN_ACTION = "view on refresh token page"
    private const val SSO_ACCCOUNT_ACTION = "view on sso account"
    private const val SEAMLESS_ACCOUNT_ACTION = "view on seamless account fetch"
    private const val ONE_TAP_ACTION = "view on onetap account fetch"
    private const val VIEW_ON_PROFILE_FETCH_ACTION = "view on profile fetch"
}
