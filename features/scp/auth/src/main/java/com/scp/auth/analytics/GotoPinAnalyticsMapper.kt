package com.scp.auth.analytics

import com.gojek.pin.utils.gtpErrorDialogueClicked
import com.gojek.pin.utils.gtpErrorDialogueSeen
import com.gojek.pin.utils.gtpForgotPinClicked
import com.gojek.pin.utils.gtpHelpClicked
import com.gojek.pin.utils.gtpPinBackButtonClicked
import com.gojek.pin.utils.gtpPinEntered
import com.gojek.pin.utils.gtpPinEyeIconClicked
import com.gojek.pin.utils.gtpPinScreenViewed
import com.gojek.pin.utils.gtpPinSubmitted
import com.gojek.pin.utils.gtpPinTyped
import com.gojek.pin.utils.gtpPinValidated
import com.gojek.pin.utils.gtpPinValidationInitiated
import com.gojek.pin.utils.gtpTnCClicked
import com.gojek.pin.utils.keyActionTaken
import com.gojek.pin.utils.keyCaption
import com.gojek.pin.utils.keyDeeplink
import com.gojek.pin.utils.keyDialogueDesc
import com.gojek.pin.utils.keyDialogueHeadline
import com.gojek.pin.utils.keyDialoguePrimaryCTA
import com.gojek.pin.utils.keyDialogueSecondaryCTA
import com.gojek.pin.utils.keyErrorReason
import com.gojek.pin.utils.keyPinFlowType
import com.gojek.pin.utils.keyScreen
import com.gojek.pin.utils.keyTnCUrl
import com.gojek.pin.utils.keyUiElement
import com.tokopedia.track.TrackAppUtils

class GotoPinAnalyticsMapper(
    private val flow: String,
    private val clientId: String
) {

    fun trackEvent(eventName: String, param: Map<String, Any?>) {
        createData(
            trackerIdFactory(eventName),
            eventFactory(eventName),
            GOTO_PIN_PAGE_CATEGORY,
            eventName,
            createEventLabel(eventName, param)
        )
    }

    private fun createEventLabel(eventName: String, param: Map<String, Any?>): String {
        return when (eventName) {
            gtpPinValidationInitiated -> "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} -$clientId"
            gtpPinScreenViewed -> "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} -$clientId - ${param[keyUiElement]}"
            gtpPinBackButtonClicked -> "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} - $clientId"
            gtpPinEyeIconClicked -> "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} - $clientId"
            gtpTnCClicked -> "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} - $clientId - $keyTnCUrl"
            gtpPinTyped -> "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} - $clientId"
            gtpPinEntered -> "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} - $clientId"
            gtpPinSubmitted -> {
                if (param[keyErrorReason].toString() == "null" || param[keyErrorReason].toString().isEmpty()) {
                    "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} - $clientId - success"
                } else {
                    "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} - $clientId - failed - ${param[keyErrorReason]}"
                }
            }
            gtpPinValidated -> "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} - $clientId"
            gtpForgotPinClicked -> "$flow - ${param[keyCaption]} - ${param[keyDeeplink]} - $clientId"
            gtpHelpClicked -> "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} - $clientId - ${param[keyDeeplink]}"
            gtpErrorDialogueSeen -> "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} - $clientId - $keyDialogueHeadline - $keyDialogueDesc - $keyDialoguePrimaryCTA - $keyDialogueSecondaryCTA"
            gtpErrorDialogueClicked -> "$flow - ${param[keyPinFlowType]} - ${param[keyScreen]} - $keyActionTaken - $clientId"
            else -> ""
        }
    }

    private fun createCustomDimension(): Map<String, Any> {
        return mapOf(BUSINESS_UNIT to BUSINESS_UNIT_VALUE, CURRENT_SITE to "")
    }

    private fun eventFactory(eventName: String): String {
        return when (eventName) {
            gtpPinScreenViewed, gtpErrorDialogueSeen -> VIEW_ACCOUNT_EVENT
            gtpPinValidationInitiated, gtpPinBackButtonClicked,
            gtpPinEyeIconClicked, gtpTnCClicked, gtpPinTyped,
            gtpPinEntered, gtpPinSubmitted, gtpPinValidated,
            gtpForgotPinClicked, gtpHelpClicked, gtpErrorDialogueClicked -> CLICK_ACCOUNT_EVENT
            else -> ""
        }
    }

    private fun trackerIdFactory(eventName: String): String {
        return when (eventName) {
            gtpPinValidationInitiated -> "47866"
            gtpPinScreenViewed -> "47867"
            gtpPinBackButtonClicked -> "47868"
            gtpPinEyeIconClicked -> "47869"
            gtpTnCClicked -> "47870"
            gtpPinTyped -> "47871"
            gtpPinEntered -> "47872"
            gtpPinSubmitted -> "47873"
            gtpPinValidated -> "47874"
            gtpForgotPinClicked -> "47875"
            gtpHelpClicked -> "47876"
            gtpErrorDialogueSeen -> "47877"
            gtpErrorDialogueClicked -> "47878"
            else -> ""
        }
    }

    internal fun createData(
        trackerId: String,
        event: String,
        category: String,
        action: String,
        label: String
    ): MutableMap<String, Any> {
        val commonData = TrackAppUtils.gtmData(event, category, action, label)
        commonData.put(trackerId, trackerId)
        commonData.putAll(createCustomDimension())
        return commonData
    }

    companion object {
        private const val CLICK_ACCOUNT_EVENT = "clickAccount"
        internal const val VIEW_ACCOUNT_EVENT = "viewAccountIris"
        private const val GOTO_PIN_PAGE_CATEGORY = "goto pin page"
        private const val BUSINESS_UNIT = "businessUnit"
        private const val CURRENT_SITE = "currentSite"
        private const val BUSINESS_UNIT_VALUE = "Shared Consumer Platform"
    }
}
