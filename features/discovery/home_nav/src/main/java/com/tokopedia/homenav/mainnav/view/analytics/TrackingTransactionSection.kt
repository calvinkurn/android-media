package com.tokopedia.homenav.mainnav.view.analytics

import android.os.Bundle
import com.tokopedia.homenav.mainnav.domain.model.NavReviewModel
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.CLICK_NAVIGATION_DRAWER
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.KEY_PAGE_SOURCE
import com.tokopedia.homenav.mainnav.view.analytics.MainNavTrackingConst.FORMAT_PAGE_SOURCE
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object TrackingTransactionSection : BaseTrackerConst() {
    private const val ACTION_CLICK_ON_FAVOURITE_SHOP = "click on Favorite Shop"
    private const val TEMPLATE_GLOBAL_MENU = "/global_menu - order_status_card"
    private const val PROMOTION_NAME_FORMAT = "/%s - %s"
    private const val GLOBAL_MENU = "global_menu"
    private const val REVIEW_CARD = "review_card"
    private const val PROMOTION_ID_FORMAT = "%s_%s_%s"
    private const val STAR_RATING = "star rating"
    private const val PRODUCT_CARD = "product card"
    private const val CREATIVE_NAME_CLICK_REVIEW_FORMAT = "%s_%s"
    private const val ITEM_NAME_WISHLIST = "/global_menu - wishlist_card"
    private const val ACTION_CLICK_ON_WISHLIST_VIEW_ALL = "click view all wishlist"
    private const val ACTION_CLICK_ON_REVIEW_VIEW_ALL = "click view all review"
    private const val FORMAT_DASH_TWO_VALUES = "%s - %s"

    /**
     * Tracker ID: 18481
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnAllTransaction(pageSource: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = GLOBAL_MENU,
            eventAction = "click on all transaction",
            eventLabel = Label.NONE
        )
        trackingBuilder.appendCurrentSite(CurrentSite.DEFAULT)
        trackingBuilder.appendBusinessUnit(BusinessUnit.DEFAULT)
        trackingBuilder.appendCustomKeyValue(TrackerId.KEY, "18481")
        trackingBuilder.appendCustomKeyValue(KEY_PAGE_SOURCE, MainNavTrackingConst.FORMAT_PAGE_SOURCE.format(pageSource))
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    /**
     * Tracker ID: 18482
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnTicket(pageSource: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = CLICK_NAVIGATION_DRAWER,
            eventCategory = GLOBAL_MENU,
            eventAction = "click on e-ticket and e-voucher",
            eventLabel = Label.NONE
        )
        trackingBuilder.appendCurrentSite(CurrentSite.DEFAULT)
        trackingBuilder.appendBusinessUnit(BusinessUnit.DEFAULT)
        trackingBuilder.appendCustomKeyValue(TrackerId.KEY, "18482")
        trackingBuilder.appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    /**
     * Tracker ID: 18483
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnReview(pageSource: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = CLICK_NAVIGATION_DRAWER,
            eventCategory = GLOBAL_MENU,
            eventAction = "click on review",
            eventLabel = Label.NONE
        )
        trackingBuilder.appendCurrentSite(CurrentSite.DEFAULT)
        trackingBuilder.appendBusinessUnit(BusinessUnit.DEFAULT)
        trackingBuilder.appendCustomKeyValue(TrackerId.KEY, "18483")
        trackingBuilder.appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    /**
     * Tracker ID: 18484
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnOrderStatus(
        orderLabel: String,
        pageSource: String
    ) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = GLOBAL_MENU,
            eventAction = "click on order status",
            eventLabel = orderLabel
        )
        trackingBuilder.appendCurrentSite(CurrentSite.DEFAULT)
        trackingBuilder.appendBusinessUnit(BusinessUnit.DEFAULT)
        trackingBuilder.appendCustomKeyValue(TrackerId.KEY, "18484")
        trackingBuilder.appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
        getTracker().sendGeneralEvent(trackingBuilder.build())
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
        pageSource: String
    ): HashMap<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = GLOBAL_MENU,
            eventAction = "impression on order status",
            eventLabel = Label.NONE,
            promotions = listOf(
                Promotion(
                    creative = orderLabel,
                    id = FORMAT_DASH_TWO_VALUES.format(bannerId, orderId),
                    name = TEMPLATE_GLOBAL_MENU,
                    creativeUrl = "",
                    position = (position + 1).toString()
                )
            )
        )
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCustomKeyValue(TrackerId.KEY, "30622")
            .appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
            .build() as HashMap<String, Any>
    }

    fun clickOnWishlist(
        userId: String,
        pageSource: String
    ) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = CLICK_NAVIGATION_DRAWER,
            eventCategory = GLOBAL_MENU,
            eventAction = "click on Wishlist",
            eventLabel = Label.NONE
        )
        trackingBuilder.appendCurrentSite(CurrentSite.DEFAULT)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(BusinessUnit.DEFAULT)
        trackingBuilder.appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    fun clickOnTokoFavorit(
        userId: String,
        pageSource: String
    ) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = CLICK_NAVIGATION_DRAWER,
            eventCategory = GLOBAL_MENU,
            eventAction = ACTION_CLICK_ON_FAVOURITE_SHOP,
            eventLabel = Label.NONE
        )
        trackingBuilder.appendCurrentSite(CurrentSite.DEFAULT)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(BusinessUnit.DEFAULT)
        trackingBuilder.appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    /**
     * Tracker ID: 30836
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun getImpressionOnWishlist(
        userId: String,
        position: Int,
        wishlistModel: NavWishlistModel,
        pageSource: String
    ): HashMap<String, Any> {
        return BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = GLOBAL_MENU,
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
            .appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
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
        pageSource: String
    ) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Category.KEY, GLOBAL_MENU)
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
        bundle.putString(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    /**
     * Tracker ID: 30859
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnWishlistViewAll(pageSource: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = GLOBAL_MENU,
            eventAction = "click view all wishlist",
            eventLabel = Label.NONE
        )
        trackingBuilder.appendCurrentSite(CurrentSite.DEFAULT)
        trackingBuilder.appendBusinessUnit(BusinessUnit.DEFAULT)
        trackingBuilder.appendCustomKeyValue(TrackerId.KEY, "30859")
        trackingBuilder.appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
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
        pageSource: String
    ): HashMap<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val positionCard = (position + 1).toString()
        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = GLOBAL_MENU,
            eventAction = "impression review card",
            eventLabel = Label.NONE,
            promotions = listOf(
                Promotion(
                    creative = Value.EMPTY,
                    id = PROMOTION_ID_FORMAT.format("0", element.reputationId, element.productId),
                    name = PROMOTION_NAME_FORMAT.format(GLOBAL_MENU, REVIEW_CARD),
                    creativeUrl = Value.EMPTY,
                    position = positionCard
                )
            )
        )
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCustomKeyValue(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
            .build() as HashMap<String, Any>
    }

    /**
     * Tracker ID: 30845
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickReviewStars(
        position: Int,
        userId: String,
        element: NavReviewModel,
        starRating: String,
        pageSource: String
    ) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, "click review card - product star rating")
        bundle.putString(Category.KEY, GLOBAL_MENU)
        bundle.putString(
            Label.KEY,
            "%s - %s - %s - %s".format(
                STAR_RATING,
                element.reputationId,
                starRating,
                element.productId
            )
        )
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
        bundle.putString(UserId.KEY, userId)
        bundle.putString(TrackerId.KEY, "30845")
        val promotion = Bundle()
        promotion.putString(
            Promotion.CREATIVE_NAME,
            CREATIVE_NAME_CLICK_REVIEW_FORMAT.format(STAR_RATING, starRating)
        )
        val horizontalPosition = (position + 1).toString()
        promotion.putString(Promotion.CREATIVE_SLOT, horizontalPosition)
        promotion.putString(
            Promotion.ITEM_NAME,
            PROMOTION_NAME_FORMAT.format(GLOBAL_MENU, REVIEW_CARD)
        )
        promotion.putString(
            Promotion.ITEM_ID,
            String.format(
                PROMOTION_ID_FORMAT,
                "0",
                element.reputationId,
                element.productId
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    /**
     * Tracker ID: 31120
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickReviewCard(
        position: Int,
        userId: String,
        element: NavReviewModel,
        pageSource: String
    ) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, "click review card - product card")
        bundle.putString(Category.KEY, GLOBAL_MENU)
        bundle.putString(
            Label.KEY,
            "%s - %s - %s".format(
                PRODUCT_CARD,
                element.reputationId,
                element.productId
            )
        )
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
        bundle.putString(UserId.KEY, userId)
        bundle.putString(TrackerId.KEY, "31120")
        val promotion = Bundle()
        promotion.putString(
            Promotion.CREATIVE_NAME,
            CREATIVE_NAME_CLICK_REVIEW_FORMAT.format(PRODUCT_CARD, Value.EMPTY)
        )
        val horizontalPosition = (position + 1).toString()
        promotion.putString(Promotion.CREATIVE_SLOT, horizontalPosition)
        promotion.putString(
            Promotion.ITEM_NAME,
            PROMOTION_NAME_FORMAT.format(GLOBAL_MENU, REVIEW_CARD)
        )
        promotion.putString(
            Promotion.ITEM_ID,
            PROMOTION_ID_FORMAT.format(
                "0",
                element.reputationId,
                element.productId
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    /**
     * Tracker ID: 30860
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun getClickViewAllTransaction(pageSource: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Action.KEY, "click view all transaction")
        bundle.putString(Category.KEY, GLOBAL_MENU)
        bundle.putString(Label.KEY, Label.NONE)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
        bundle.putString(TrackerId.KEY, "30860")
        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }

    /**
     * Tracker ID: 40850
     * Thanos: https://mynakama.tokopedia.com/datatracker/requestdetail/view/1890
     */
    fun clickOnReviewViewAll(pageSource: String) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Category.KEY, GLOBAL_MENU)
        bundle.putString(Action.KEY, ACTION_CLICK_ON_REVIEW_VIEW_ALL)
        bundle.putString(Label.KEY, Label.NONE)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(KEY_PAGE_SOURCE, FORMAT_PAGE_SOURCE.format(pageSource))
        bundle.putString(TrackerId.KEY, "40850")
        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }
}
