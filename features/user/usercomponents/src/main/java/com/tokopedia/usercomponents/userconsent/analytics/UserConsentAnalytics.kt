package com.tokopedia.usercomponents.userconsent.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.usercomponents.userconsent.common.PurposeDataModel
import com.tokopedia.usercomponents.userconsent.common.UserConsentCollectionDataModel
import javax.inject.Inject

class UserConsentAnalytics @Inject constructor() {

    private val tracker = TrackApp.getInstance().gtm

    private fun sendTracker(action: String, label: String) {
        val trackerParam = TrackAppUtils.gtmData(
            EVENT.CLICK_ACCOUNT,
            CATEGORY.CONSENT_BOX,
            action,
            label
        )

        trackerParam[EVENT_BUSINESS_UNIT] = BUSINESS_UNIT_USER_PLATFORM
        trackerParam[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE

        tracker.sendGeneralEvent(trackerParam)
    }

    private fun generatePurposeId(purposes: MutableList<PurposeDataModel>): String {
        var purposeIds = ""
        if (purposes.size == 1) {
            return purposes.first().id
        }

        purposes.forEachIndexed { index, purposeDataModel ->
            purposeIds += if (index == purposes.size - 1) purposeDataModel.id else "${purposeDataModel.id} ,"
        }

        return purposeIds
    }

    fun trackOnPurposeCheck(isChecked: Boolean, purposes: MutableList<PurposeDataModel>) {
        sendTracker(
            action = ACTION.CLICK_TICK_BOX,
            label = String.format(
                LABEL.TICK_BOX,
                if (isChecked) LABEL.CHECK
                else LABEL.UNCHECK,
                generatePurposeId(purposes)
            )
        )
    }

    fun trackOnPurposeCheckOnOptional(isChecked: Boolean, purposes: PurposeDataModel) {
        sendTracker(
            action = ACTION.CLICK_TICK_BOX,
            label = String.format(
                LABEL.TICK_BOX,
                if (isChecked) LABEL.CHECK
                else LABEL.UNCHECK,
                purposes.id
            )
        )
    }

    fun trackOnTnCHyperLinkClicked(purposes: MutableList<PurposeDataModel>) {
        sendTracker(
            action = ACTION.CLICK_TNC_HYPER_LINK,
            label = generatePurposeId(purposes)
        )
    }

    fun trackOnPolicyHyperLinkClicked(purposes: MutableList<PurposeDataModel>) {
        sendTracker(
            action = ACTION.CLICK_PRIVACY_HYPER_LINK,
            label = generatePurposeId(purposes)
        )
    }

    fun trackOnActionButtonClicked(purposes: MutableList<PurposeDataModel>) {
        sendTracker(
            action = ACTION.CLICK_ACTION_BUTTON,
            label = generatePurposeId(purposes)
        )
    }

    fun trackOnConsentView(purposes: MutableList<PurposeDataModel>) {
        sendTracker(
            action = ACTION.VIEW_USER_CONSENT,
            label = generatePurposeId(purposes)
        )
    }

    companion object {

        const val EVENT_BUSINESS_UNIT = "businessUnit"
        const val EVENT_CURRENT_SITE = "currentSite"

        const val BUSINESS_UNIT_USER_PLATFORM = "User Platform"
        const val TOKOPEDIA_MARKETPLACE_SITE = "tokopediamarketplace"

        object EVENT {
            const val CLICK_ACCOUNT = "clickAccount"
        }

        object ACTION {
            const val CLICK_TICK_BOX = "click tick box"
            const val CLICK_TNC_HYPER_LINK = "click tnc hyperlink"
            const val CLICK_PRIVACY_HYPER_LINK = "click privacy hyperlink"
            const val CLICK_ACTION_BUTTON = "click action button"
            const val VIEW_USER_CONSENT = "view consent box"
        }

        object CATEGORY {
            const val CONSENT_BOX = "consent box"
        }

        object LABEL {
            const val CHECK = "check"
            const val UNCHECK = "uncheck"
            const val TICK_BOX = "%s - tick box - %s"
        }
    }
}
