package com.tokopedia.pdp.fintech.analytics

import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PdpFintechWidgetAnalytics @Inject constructor(
    private val userSession: dagger.Lazy<UserSessionInterface>
) {

    fun sendAnalyticsEvent(analyticsEvent:FintechWidgetAnalyticsEvent)
    {
        when(analyticsEvent){
            is FintechWidgetAnalyticsEvent.PdpWidgetImression -> sendPdpWidgetImpression(analyticsEvent.partnerId,analyticsEvent.productId,analyticsEvent.userStatus)
        }
    }

    private fun sendPdpWidgetImpression(partnerId: String, productId: String, userStatus: String) {

    }




    companion object{
        const val  viewEvent= "viewFintechIris"
        const val pdpBnplImpression = "pdp bnpl - impression status buyers"
        const val eventCategory = "fin - pdp page"
    }


}