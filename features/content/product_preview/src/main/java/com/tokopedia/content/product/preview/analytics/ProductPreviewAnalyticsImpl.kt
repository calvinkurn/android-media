package com.tokopedia.content.product.preview.analytics

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/4459
 * 1 - 18
 */
class ProductPreviewAnalyticsImpl @AssistedInject constructor(
    @Assisted private val productId: String,
    analyticManagerFactory: ContentAnalyticManager.Factory
) : ProductPreviewAnalytics {

    private val analyticManager = analyticManagerFactory.create(
        businessUnit = BusinessUnit.content,
        eventCategory = EventCategory.unifiedViewPDP
    )

    @AssistedFactory
    interface Factory : ProductPreviewAnalytics.Factory {
        override fun create(productId: String): ProductPreviewAnalyticsImpl
    }

    /**
     * 1. swipe left or right to next content / tab
     * 49587
     */
    override fun onSwipeContentAndTab(tabName: String, isTabChanged: Boolean) {
        analyticManager.sendClickContent(
            eventAction = "scroll - swipe left right content",
            eventLabel = "$tabName - $productId - ${if (isTabChanged) "different tab" else "same tab"}",
            mainAppTrackerId = "49587",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 2. impress video
     * 49588
     */
    override fun onImpressVideo(pageSource: String) {
        analyticManager.sendOpenScreen(
            screenName = "/product detail page - unified content viewing - $productId - 0 - $pageSource",
            mainAppTrackerId = "49588",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 3. impress ATC button
     * 49589
     */
    override fun onImpressATC(pageSource: String) {
        analyticManager.sendViewContent(
            eventAction = "view - add to cart media fullscreen",
            eventLabel = "$pageSource - $productId - 0",
            mainAppTrackerId = "49589",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 5. click content thumbnail in Produk tab
     * 49594
     */
    override fun onClickThumbnailProduct() {
        analyticManager.sendClickContent(
            eventAction = "click - content thumbnail",
            eventLabel = productId,
            mainAppTrackerId = "49594",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 6. impress image content
     * 49598
     */
    override fun onImpressImage(pageSource: String) {
        analyticManager.sendViewContent(
            eventAction = "view - image content",
            eventLabel = "$pageSource - $productId",
            mainAppTrackerId = "49598",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 7. impress Ingatkan Saya button
     * 49600
     */
    override fun onImpressRemindMe(pageSource: String) {
        analyticManager.sendViewContent(
            eventAction = "view - ingatkan saya",
            eventLabel = "$pageSource - $productId",
            mainAppTrackerId = "49600",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 8. click Ingatkan Saya button
     * 49601
     */
    override fun onClickRemindMe(pageSource: String) {
        analyticManager.sendClickContent(
            eventAction = "click - ingatkan saya",
            eventLabel = "$pageSource - $productId",
            mainAppTrackerId = "49601",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 9. swipe up down to next content in Ulasan tab
     * 49602
     */
    override fun onSwipeReviewNextContent() {
        analyticManager.sendClickContent(
            eventAction = "scroll - swipe up down ulasan tab",
            eventLabel = productId,
            mainAppTrackerId = "49602",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 10. click account name
     * 49603
     */
    override fun onClickReviewAccountName() {
        analyticManager.sendClickContent(
            eventAction = "click - account name",
            eventLabel = "$productId - 0",
            mainAppTrackerId = "49603",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 11. Click 3 dots menu
     * 49605
     */
    override fun onClickReviewThreeDots() {
        analyticManager.sendClickContent(
            eventAction = "click - three dots",
            eventLabel = productId,
            mainAppTrackerId = "49605",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 12. click Back button to PDP
     * 49606
     */
    override fun onClickBackButton(pageSource: String) {
        analyticManager.sendClickContent(
            eventAction = "click - back button to pdp",
            eventLabel = "$pageSource - $productId",
            mainAppTrackerId = "49606",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 13. click ATC to global variant bottomsheet
     * 49607
     */
    override fun onClickVariantGBVS(pageSource: String) {
        analyticManager.sendClickContent(
            eventAction = "click - variant bottomsheet atc entry point",
            eventLabel = "$pageSource - $productId",
            mainAppTrackerId = "49607",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 14. click laporkan ulasan in ulasan tab
     * 49650
     */
    override fun onClickReviewReport() {
        analyticManager.sendClickContent(
            eventAction = "click - laporkan ulasan",
            eventLabel = "$productId - 0",
            mainAppTrackerId = "49650",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 15. click mode nonton in ulasan tab
     * 49651
     */
    override fun onClickReviewWatchMode() {
        analyticManager.sendClickContent(
            eventAction = "click - mode nonton",
            eventLabel = productId,
            mainAppTrackerId = "49651",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 16. click pause/play in video
     * 49845
     */
    override fun onClickPauseOrPlayVideo(pageSource: String) {
        analyticManager.sendClickContent(
            eventAction = "click - pause play button",
            eventLabel = "$pageSource - $productId - 0",
            mainAppTrackerId = "49845",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 17. submit report from ulasan tab
     * 49850
     */
    override fun onClickSubmitReport() {
        analyticManager.sendClickContent(
            eventAction = "click - submit report ulasan",
            eventLabel = "$productId - 0",
            mainAppTrackerId = "49850",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 18. like/unlike content
     * 49851
     */
    override fun onClickLikeOrUnlike() {
        analyticManager.sendClickContent(
            eventAction = "click - like button",
            eventLabel = "$productId - 0",
            mainAppTrackerId = "49851",
            customFields = mapOf(Key.productId to productId)
        )
    }
}
