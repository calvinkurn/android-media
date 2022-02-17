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
                    analyticsEvent.partnerId,
                    analyticsEvent.productId, analyticsEvent.userStatus
                )
        }
    }


    private fun sendGeneralEvent(map: MutableMap<String, Any>) {
        map[KEY_USER_ID] = userSession.get().userId
        map[KEY_BUSINESS_UNIT] = FINTECH_PAYLATER_BUSIONNES
        map[KEY_CURRENT_SITE] = FINTECH_PAYLATER_CURRENTSITE
        analyticTracker.sendGeneralEvent(map)
    }

    private fun sendPdpWidgetImpression(partnerId: String, productId: String, userStatus: String) {
        val map = TrackAppUtils.gtmData(
            viewEvent,
            eventCategory,
            pdpBnplImpression,
            "$eventLable- $productId - $userStatus -$partnerId"

        )
        sendGeneralEvent(map)

    }


    companion object {
        const val viewEvent = "viewFintechIris"
        const val clickEvent = "clickFintech"
        const val pdpBnplImpression = "pdp bnpl - impression status buyers"
        const val pdpBnplImpressionEligible = "pdp bnpl - impression status eligible buyers"
        const val pdpWidgetScrollEvent = "pdp bnpl - scroll widget"
        const val eventCategory = "fin - pdp page"
        const val eventLable = "eventLabel"
        const val KEY_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val FINTECH_PAYLATER_BUSIONNES = "fintechPaylater"
        const val FINTECH_PAYLATER_CURRENTSITE = "TokopediaFintech"
    }


}