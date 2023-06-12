package com.tokopedia.play.broadcaster.shorts.analytic.affiliate

interface PlayShortsAffiliateAnalytic {

    fun sendClickRegisterAffiliateCardEvent(partnerId: String)

    fun sendClickAcceptTcAffiliateEvent(partnerId: String)

    fun sendClickNextRegisterAffiliateEvent(partnerId: String)

    fun sendClickNextCreateContentEvent(partnerId: String)

    fun sendImpressionCekProductCommissionEvent(partnerId: String)

    fun sendImpressionTagProductCommissionEvent(partnerId: String)
}
