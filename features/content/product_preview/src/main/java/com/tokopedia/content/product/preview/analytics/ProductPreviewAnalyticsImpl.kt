package com.tokopedia.content.product.preview.analytics

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/4459
 * 1 - 18
 */
class ProductPreviewAnalyticsImpl @Inject constructor(
    private val userSession: UserSessionInterface
) : ProductPreviewAnalytics {

    private val userId: String
        get() = userSession.userId.orEmpty()

    /**
     * 1. swipe left or right to next content / tab
     * 49587
     */
    override fun onSwipeContentAndTab(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("scroll - swipe left right content")
            .setEventCategory("unified view pdp")
            .setEventLabel(productId)
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49587")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 2. impress video
     * 49588
     */
    override fun onImpressVideo(productId: String) {
        Tracker.Builder()
            .setEvent(Event.openScreen)
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49588")
            .setCustomProperty(Key.isLoggedInStatus, userSession.isLoggedIn)
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.screenName, "/unified view pdp - $productId - {channel_id}")
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 3. impress ATC button
     * 49589
     */
    override fun onImpressATC(productId: String) {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("view - add to cart media fullscreen")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49589")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 4. click ATC button
     * 49590
     */
    override fun onClickATC(productId: String) {
        Tracker.Builder()
            .setEvent(Event.add_to_cart)
            .setEventAction("click - add to cart media fullscreen")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49590")
            .setCustomProperty(Key.items, "items")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 5. click content thumbnail in Produk tab
     * 49594
     */
    override fun onClickThumbnailProduct(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - content thumbnail")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCustomProperty(Key.trackerId, "49594")
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 6. impress image content
     * 49598
     */
    override fun onImpressImageContent(productId: String) {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("view - image content")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setCustomProperty(Key.trackerId, "49598")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 7. impress Ingatkan Saya button
     * 49600
     */
    override fun onImpressRemindMe(productId: String) {
        Tracker.Builder()
            .setEvent(Event.viewContentIris)
            .setEventAction("view - ingatkan saya")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49600")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 8. click Ingatkan Saya button
     * 49601
     */
    override fun onClickRemindMe(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - ingatkan saya")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49601")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 9. swipe up down to next content in Ulasan tab
     * 49602
     */
    override fun onSwipeReviewNextContent(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("scroll - swipe up down ulasan tab")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49602")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 10. click account name
     * 49603
     */
    override fun onClickReviewAccountName(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - account name")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49603")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 11. Click 3 dots menu
     * 49605
     */
    override fun onClickReviewThreeDots(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - three dots")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49605")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 12. click Back button to PDP
     * 49606
     */
    override fun onClickBackButton(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - back button to pdp")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49606")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 13. click ATC to global variant bottomsheet
     * 49607
     */
    override fun onOpenGBVS(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - variant bottomsheet atc entry point")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49607")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 14. click laporkan ulasan in ulasan tab
     * 49650
     */
    override fun onClickReviewReport(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - laporkan ulasan")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49650")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 15. click mode nonton in ulasan tab
     * 49651
     */
    override fun onClickReviewWatchMode(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - mode nonton")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49651")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 16. click pause/play in video
     * 49845
     */
    override fun onClickPauseOrPlayVideo(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - pause play button")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49845")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    /**
     * 17. submit report from ulasan tab
     * 49850
     */
    override fun onClickSubmitReport(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - submit report ulasan")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49850")
            .setCustomProperty(Key.sessionIris, sessionIris)
            .build()
            .send()
    }

    /**
     * 18. like/unlike content
     * 49851
     */
    override fun onClickLikeOrUnlike(productId: String) {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - like button")
            .setEventCategory("unified view pdp")
            .setEventLabel("eventLabel")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite("tokopediamarketplace")
            .setCustomProperty(Key.trackerId, "49851")
            .setCustomProperty(Key.productId, productId)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }
}
