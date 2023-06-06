package com.tokopedia.play.broadcaster.shorts.analytic.affiliate

import com.tokopedia.play.broadcaster.shorts.analytic.sender.PlayShortsAnalyticSender
import javax.inject.Inject

class PlayShortsAffiliateAnalyticImpl @Inject constructor(
    private val analyticSender: PlayShortsAnalyticSender,
) : PlayShortsAffiliateAnalytic {

    /**
     * Mynakama Tracker
     * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3757
     */

    /** row 1 **/
    override fun sendClickRegisterAffiliateCardEvent(partnerId: String) {
        sendGeneralClickEvent(
            eventAction = "click - daftar affiliate card",
            eventLabel = "$partnerId - user",
            trackerId = "41405",
        )
    }

    /** row 2 **/
    override fun sendClickAcceptTcAffiliateEvent(partnerId: String) {
        sendGeneralClickEvent(
            eventAction = "click - accept t&c affiliate",
            eventLabel = "$partnerId - user",
            trackerId = "41406",
        )
    }

    /** row 3 **/
    override fun sendClickNextRegisterAffiliateEvent(partnerId: String) {
        sendGeneralClickEvent(
            eventAction = "click - lanjut register affiliate",
            eventLabel = "$partnerId - user",
            trackerId = "41407",
        )
    }

    /** row 5 **/
    override fun sendClickNextCreateContentEvent(partnerId: String) {
        sendGeneralClickEvent(
            eventAction = "click - lanjut buat konten",
            eventLabel = "$partnerId - user",
            trackerId = "42840",
        )
    }

    /** row 6 **/
    override fun sendImpressionCekProductCommissionEvent(partnerId: String) {
        sendGeneralViewEvent(
            eventAction = "impression - cek produk komisi",
            eventLabel = "$partnerId - user",
            trackerId = "42841",
        )
    }

    /** row 7 **/
    override fun sendImpressionTagProductCommissionEvent(partnerId: String) {
        sendGeneralViewEvent(
            eventAction = "impression - tag product komisi",
            eventLabel = "$partnerId - user",
            trackerId = "42842",
        )
    }

    private fun sendGeneralViewEvent(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    ) {
        analyticSender.sendGeneralViewEventContent(eventAction, eventLabel, trackerId)
    }

    private fun sendGeneralClickEvent(
        eventAction: String,
        eventLabel: String,
        trackerId: String
    ) {
        analyticSender.sendGeneralClickEventContent(eventAction, eventLabel, trackerId)
    }

}
