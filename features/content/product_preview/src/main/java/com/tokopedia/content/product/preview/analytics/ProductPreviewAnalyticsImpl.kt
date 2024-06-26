package com.tokopedia.content.product.preview.analytics

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.EventCategory
import com.tokopedia.content.analytic.Key
import com.tokopedia.content.analytic.manager.ContentAnalyticManager
import com.tokopedia.content.analytic.model.ContentEnhanceEcommerce
import com.tokopedia.content.product.preview.view.uimodel.BottomNavUiModel
import com.tokopedia.content.product.preview.view.uimodel.finalPrice
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
     * 4. click content thumbnail in Produk tab
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
     * 5. impress image content
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
     * 6. impress Ingatkan Saya button
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
     * 7. click Ingatkan Saya button
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
     * 8. swipe up down to next content in Ulasan tab
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
     * 9. click account name
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
     * 10. Click 3 dots menu
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
     * 11. click Back button to PDP
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
     * 12. click laporkan ulasan in ulasan tab
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
     * 13. click mode nonton in ulasan tab
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
     * 14. click pause/play in video
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
     * 15. submit report from ulasan tab
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
     * 16. like/unlike content
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

    /**
     * 17. click ATC without global variant bottomsheet
     * 50427
     */
    override fun onAtcWithoutVariant(bottomNavUiModel: BottomNavUiModel, currentTab: String) {
        var categoryId = ""
        var itemCategory = ""
        bottomNavUiModel.categoryTree.forEachIndexed { index, categoryTree ->
            categoryId += if (index != 0) " / " else "" + categoryTree.id
            itemCategory += if (index != 0) " / " else "" + categoryTree.name
        }
        analyticManager.sendEEProduct(
            event = Event.add_to_cart,
            eventAction = "click - add to cart media fullscreen",
            eventLabel = "$currentTab - $productId",
            itemList = "",
            products = listOf(
                ContentEnhanceEcommerce.Product(
                    itemId = productId,
                    itemName = bottomNavUiModel.title,
                    itemBrand = "",
                    itemCategory = itemCategory,
                    itemVariant = bottomNavUiModel.hasVariant.toString(),
                    price = bottomNavUiModel.price.finalPrice,
                    index = "1",
                    customFields = mapOf(
                        Key.categoryId to categoryId,
                        Key.itemShopId to bottomNavUiModel.shop.id,
                        Key.itemShopName to bottomNavUiModel.shop.name,
                    )
                )
            ),
            mainAppTrackerId = "50427",
            customFields = mapOf(Key.productId to productId)
        )
    }

    /**
     * 18. click ATC to open global variant bottomsheet
     * 50428
     */
    override fun onAtcVariant(bottomNavUiModel: BottomNavUiModel, currentTab: String) {
        analyticManager.sendClickContent(
            eventAction = "click - variant bottomsheet atc entry point",
            eventLabel = "$currentTab - $productId",
            mainAppTrackerId = "50428",
            customFields = mapOf(Key.productId to productId)
        )
    }

    override fun onClickBottomNav(model: BottomNavUiModel) {
        var categoryId = ""
        var itemCategory = ""
        model.categoryTree.forEachIndexed { index, categoryTree ->
            categoryId += if (index != 0) " / " else "" + categoryTree.id
            itemCategory += if (index != 0) " / " else "" + categoryTree.name
        }
        analyticManager.sendEEProduct(
            event = Event.selectContent,
            eventAction = "click - bottom nav",
            eventLabel = productId,
            itemList = "/product detail page - unified content viewing",
            products = listOf(
                ContentEnhanceEcommerce.Product(
                    itemId = productId,
                    itemName = model.title,
                    itemBrand = "",
                    itemCategory = itemCategory,
                    itemVariant = model.hasVariant.toString(),
                    price = model.price.finalPrice,
                    index = "1",
                    customFields = mapOf(
                        Key.categoryId to categoryId,
                        Key.itemShopId to model.shop.id,
                        Key.itemShopName to model.shop.name,
                    )
                )
            ),
            mainAppTrackerId = "50664",
            customFields = mapOf(Key.productId to productId)
        )
    }
}
