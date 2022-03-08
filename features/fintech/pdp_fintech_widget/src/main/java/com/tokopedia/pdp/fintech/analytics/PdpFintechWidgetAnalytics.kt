package com.tokopedia.pdp.fintech.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PdpFintechWidgetAnalytics @Inject constructor(
    private val userSession: dagger.Lazy<UserSessionInterface>
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

    fun sendAnalyticsEvent(analyticsEvent: FintechWidgetAnalyticsEvent) {
        when (analyticsEvent) {
            is FintechWidgetAnalyticsEvent.PdpWidgetImpression ->
                sendPdpWidgetImpression(
                    analyticsEvent.partnerId, analyticsEvent.linkingStatus,
                    analyticsEvent.productId, analyticsEvent.userStatus, analyticsEvent.chipType
                )

            is FintechWidgetAnalyticsEvent.ActivationBottomSheetClick -> sendActivationClick(
                analyticsEvent.userStatus,
                analyticsEvent.linkingStatus,
                analyticsEvent.partner,
                analyticsEvent.ctaWording
            )
        }
    }

    private fun sendActivationClick(
        userStatus: String,
        linkingStatus: String,
        partner: String,
        ctaWording: String
    ) {
        val map = TrackAppUtils.gtmData(
            clickEvent,
            eventCategoryBottomSheet,
            clickActivationAction,
            "$userStatus - $linkingStatus - $partner - $userStatus - $ctaWording"

        )
        sendGeneralEvent(map)

    }


    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[KEY_USER_ID] = userSession.get().userId
        map[KEY_BUSINESS_UNIT] = FINTECH_PAYLATER_BUSIONNES
        map[KEY_CURRENT_SITE] = FINTECH_PAYLATER_CURRENTSITE
        analyticTracker.sendGeneralEvent(map)
    }

    private fun sendPdpWidgetImpression(
        partnerId: String,
        linkingStatus: String,
        productId: String,
        userStatus: String,
        chipType: String
    ) {
        val map = TrackAppUtils.gtmData(
            viewEvent,
            eventCategory,
            pdpBnplImpression,
            "$productId - Yes - ${linkingStatus} - ${userSession.get().userId} - $userStatus - $chipType - $partnerId"

        )
        sendGeneralEvent(map)

    }


    companion object {
        const val viewEvent = "viewFintechIris"
        const val clickEvent = "clickFintech"
        const val eventCategoryBottomSheet = "fin - activation bottom sheet"
        const val pdpBnplImpression = "pdp page - impression bnpl widget"
        const val clickActivationAction = "activation bnpl - click tanda tangan untuk aktifkan"
        const val eventCategory = "fin - pdp page"
        const val KEY_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val FINTECH_PAYLATER_BUSIONNES = "fintechPaylater"
        const val FINTECH_PAYLATER_CURRENTSITE = "TokopediaFintech"
    }


}