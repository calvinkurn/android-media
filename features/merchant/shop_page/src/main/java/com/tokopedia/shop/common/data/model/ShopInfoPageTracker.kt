package com.tokopedia.shop.common.data.model

import com.tokopedia.shop.info.domain.entity.ShopNote
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopInfoPageTracker @Inject constructor(private val userSession: UserSessionInterface) {

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48699
    fun sendShopInfoOpenScreenImpression(shopId: String) {
        val isLoggedIn = if (userSession.isLoggedIn) "true" else "false"

        Tracker.Builder()
            .setEvent("openScreen")
            .setCustomProperty("trackerId", "48699")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty("isLoggedInStatus", isLoggedIn)
            .setCustomProperty("pageType", "/shoppage - info page")
            .setCustomProperty("screenName", "/shoppage - info page")
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48700
    fun sendCtaExpandPharmacyInformationEvent(shopId: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("shop info - click expand information")
            .setEventCategory("shop page - buyer")
            .setEventLabel("")
            .setCustomProperty("trackerId", "48700")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48701
    fun sendCtaViewPharmacyLocationEvent(shopId: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("shop info - click lihat lokasi")
            .setEventCategory("shop page - buyer")
            .setEventLabel("")
            .setCustomProperty("trackerId", "48701")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48702
    fun sendIconViewAllShopReviewEvent(shopId: String) {
        Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("shop info - click cta all reviews")
            .setEventCategory("shop page - buyer")
            .setEventLabel("")
            .setCustomProperty("trackerId", "48702")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48703
    fun sendReviewImpression(reviewIndex: Int, shopId: String) {
        Tracker.Builder()
            .setEvent("viewPGIris")
            .setEventAction("shop info - impression review highlight")
            .setEventCategory("shop page - buyer")
            .setEventLabel(reviewIndex.toString())
            .setCustomProperty("trackerId", "48703")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/4388
    // Tracker ID: 48704
    fun sendClickShopNoteEvent(shopNote: ShopNote, shopId: String) {
        val shopNoteId = shopNote.id
        val templateId = 0
        val eventLabel = "$shopNoteId - $templateId" Tracker.Builder()
            .setEvent("clickPG")
            .setEventAction("shop info - click shop notes")
            .setEventCategory("shop page - buyer")
            .setEventLabel(eventLabel)
            .setCustomProperty("trackerId", "48704")
            .setBusinessUnit("Physical Goods")
            .setCurrentSite("tokopediamarketplace")
            .setShopId(shopId)
            .setUserId(userSession.userId)
            .build()
            .send()
    }
}
