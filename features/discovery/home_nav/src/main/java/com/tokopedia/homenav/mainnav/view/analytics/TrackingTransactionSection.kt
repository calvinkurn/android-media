package com.tokopedia.homenav.mainnav.view.analytics

import android.os.Bundle
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_ALL_TRANSACTION
import com.tokopedia.homenav.common.util.ClientMenuGenerator.Companion.ID_REVIEW
import com.tokopedia.homenav.mainnav.domain.model.NavReviewModel
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.KEY_PAGE_SOURCE
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.asTrackingPageSource
import com.tokopedia.searchbar.navigation_component.NavSource
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object TrackingTransactionSection : BaseTrackerConst() {
    private const val PROMOTION_NAME_REVIEW = "/global_menu - review_card"
    private const val PROMOTION_NAME_ORDER_STATUS = "/global_menu - order_status_card"
    private const val REVIEW_PROMOTION_ID_FORMAT = "%s - %s - %s - %s"
    private const val ORDER_PROMOTION_ID_FORMAT = "%s - %s"
    private const val ITEM_NAME_WISHLIST = "/global_menu - wishlist_card"

    /**
     * Tracker ID: 18484
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnOrderStatus(
        orderId: String,
        orderLabel: String,
        pageSource: NavSource,
        pageSourcePath: String = "",
        position: Int,
        userId: String,
    ) {
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Action.KEY, "click on order status")
            putString(Category.KEY, MainNavTrackingConst.GLOBAL_MENU)
            putString(Label.KEY, orderLabel)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            putString(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
            putString(UserId.KEY, userId)
            putString(TrackerId.KEY, "18484")
            putParcelableArrayList(
                Promotion.KEY, arrayListOf(
                    Bundle().apply {
                        putString(Promotion.CREATIVE_NAME, orderLabel)
                        putString(Promotion.CREATIVE_SLOT, (position + 1).toString())
                        putString(Promotion.ITEM_NAME, PROMOTION_NAME_ORDER_STATUS)
                        putString(Promotion.ITEM_ID, ORDER_PROMOTION_ID_FORMAT.format("0", orderId))
                    }
                )
            )
        }
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    /**
     * Tracker ID: 30622
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun getImpressionOnOrderStatus(
        userId: String,
        orderLabel: String,
        bannerId: String = "0",
        orderId: String,
        position: Int,
        pageSource: NavSource,
        pageSourcePath: String = ""
    ): HashMap<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = MainNavTrackingConst.GLOBAL_MENU,
            eventAction = "impression on order status",
            eventLabel = Label.NONE,
            promotions = listOf(
                Promotion(
                    creative = orderLabel,
                    id = ORDER_PROMOTION_ID_FORMAT.format(bannerId, orderId),
                    name = PROMOTION_NAME_ORDER_STATUS,
                    position = (position + 1).toString()
                )
            )
        )
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCustomKeyValue(TrackerId.KEY, "30622")
            .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
            .build() as HashMap<String, Any>
    }

    /**
     * Tracker ID: 30836
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun getImpressionOnWishlist(
        userId: String,
        position: Int,
        wishlistModel: NavWishlistModel,
        pageSource: NavSource,
        pageSourcePath: String = ""
    ): HashMap<String, Any> {
        return BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = MainNavTrackingConst.GLOBAL_MENU,
            eventAction = "impression wishlist card",
            eventLabel = wishlistModel.id,
            promotions = listOf(
                Promotion(
                    creative = Value.EMPTY,
                    id = "0",
                    name = ITEM_NAME_WISHLIST,
                    creativeUrl = Value.EMPTY,
                    position = (position + 1).toString()
                )
            )
        )
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCustomKeyValue(TrackerId.KEY, "30836")
            .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
            .build() as HashMap<String, Any>
    }

    /**
     * Tracker ID: 30837
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnWishlistItem(
        userId: String,
        wishlistModel: NavWishlistModel,
        position: Int,
        pageSource: NavSource,
        pageSourcePath: String = ""
    ) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Category.KEY, MainNavTrackingConst.GLOBAL_MENU)
        bundle.putString(Action.KEY, "click wishlist card")
        bundle.putString(Label.KEY, wishlistModel.id)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        val promotions = arrayListOf(
            Bundle().apply {
                putString(Promotion.CREATIVE_NAME, Value.EMPTY)
                putString(Promotion.CREATIVE_SLOT, (position + 1).toString())
                putString(Items.ITEM_ID, "0")
                putString(Items.ITEM_NAME, ITEM_NAME_WISHLIST)
            }
        )
        bundle.putParcelableArrayList(Promotion.KEY, promotions)
        bundle.putString(TrackerId.KEY, "30837")
        bundle.putString(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    /**
     * Tracker ID: 30859
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnWishlistViewAll(
        pageSource: NavSource,
        pageSourcePath: String = ""
    ) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = MainNavTrackingConst.GLOBAL_MENU,
            eventAction = "click view all wishlist",
            eventLabel = Label.NONE
        )
        trackingBuilder.appendCurrentSite(CurrentSite.DEFAULT)
        trackingBuilder.appendBusinessUnit(BusinessUnit.DEFAULT)
        trackingBuilder.appendCustomKeyValue(TrackerId.KEY, "30859")
        trackingBuilder.appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    /**
     * Tracker ID: 30840
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun getImpressionOnReviewProduct(
        position: Int,
        userId: String,
        element: NavReviewModel,
        pageSource: NavSource,
        pageSourcePath: String = ""
    ): HashMap<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val positionCard = (position + 1).toString()
        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = MainNavTrackingConst.GLOBAL_MENU,
            eventAction = "impression review card",
            eventLabel = Label.NONE,
            promotions = listOf(
                Promotion(
                    creative = element.productName,
                    id = REVIEW_PROMOTION_ID_FORMAT.format("0", element.reputationId, "0", element.productId),
                    name = PROMOTION_NAME_REVIEW,
                    position = positionCard
                )
            )
        )
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCustomKeyValue(TrackerId.KEY, "30840")
            .appendCustomKeyValue(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
            .build() as HashMap<String, Any>
    }

    /**
     * Tracker ID: 30843
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickReviewCard(
        position: Int,
        userId: String,
        element: NavReviewModel,
        starRating: String,
        pageSource: NavSource,
        pageSourcePath: String = ""
    ) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, "click on review card")
        bundle.putString(Category.KEY, MainNavTrackingConst.GLOBAL_MENU)
        bundle.putString(
            Label.KEY,
            "%s - %s - %s".format(
                element.reputationId,
                starRating,
                element.productId
            )
        )
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
        bundle.putString(UserId.KEY, userId)
        bundle.putString(TrackerId.KEY, "30843")
        val promotion = Bundle()
        promotion.putString(
            Promotion.CREATIVE_NAME,
            element.productName
        )
        val horizontalPosition = (position + 1).toString()
        promotion.putString(Promotion.CREATIVE_SLOT, horizontalPosition)
        promotion.putString(
            Promotion.ITEM_NAME,
            PROMOTION_NAME_REVIEW
        )
        promotion.putString(
            Promotion.ITEM_ID,
            REVIEW_PROMOTION_ID_FORMAT.format(
                "0",
                element.reputationId,
                starRating,
                element.productId
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    @Deprecated("old global menu tracker")
    /**
     * Tracker ID: 30860
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun getClickViewAllTransaction(
        pageSource: NavSource,
        pageSourcePath: String = ""
    ) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Action.KEY, "click view all transaction")
        bundle.putString(Category.KEY, MainNavTrackingConst.GLOBAL_MENU)
        bundle.putString(Label.KEY, Label.NONE)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
        bundle.putString(TrackerId.KEY, "30860")
        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }

    /**
     * Tracker ID: 30854
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnViewAllCard(
        sectionId: Int,
        pageSource: NavSource,
        pageSourcePath: String = ""
    ) {
        val userMenu = when(sectionId) {
            ID_REVIEW -> "review"
            ID_ALL_TRANSACTION -> "order status"
            else -> ""
        }
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Category.KEY, MainNavTrackingConst.GLOBAL_MENU)
        bundle.putString(Action.KEY, "click view all card")
        bundle.putString(Label.KEY, userMenu)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(KEY_PAGE_SOURCE, pageSource.asTrackingPageSource(pageSourcePath))
        bundle.putString(TrackerId.KEY, "30854")
        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }
}
