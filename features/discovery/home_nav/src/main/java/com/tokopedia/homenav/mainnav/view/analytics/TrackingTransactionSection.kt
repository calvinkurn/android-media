package com.tokopedia.homenav.mainnav.view.analytics

import android.os.Bundle
import com.tokopedia.homenav.common.TrackingConst.CATEGORY_GLOBAL_MENU
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_BANNER
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_BUSINESS_UNIT
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_CURRENT_SITE
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_EMPTY
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_PAGE_SOURCE
import com.tokopedia.homenav.common.TrackingConst.EVENT_CLICK_NAVIGATION_DRAWER
import com.tokopedia.homenav.common.TrackingConst.PAGE_SOURCE
import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.homenav.mainnav.domain.model.NavReviewModel
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object TrackingTransactionSection : BaseTrackerConst() {
    private const val ACTION_CLICK_ON_ALL_TRANSACTION = "click on all transaction"
    private const val ACTION_CLICK_ON_TICKET = "click on e-ticket and e-voucher"
    private const val ACTION_CLICK_ON_REVIEW = "click on review"
    private const val ACTION_CLICK_ON_WISHLIST = "click on Wishlist"
    private const val ACTION_CLICK_ON_FAVOURITE_SHOP = "click on Favorite Shop"
    private const val ACTION_CLICK_ON_ORDER_STATUS = "click on order status"
    private const val ACTION_CLICK_ON_REVIEW_STAR_RATING = "click review card - product star rating"
    private const val ACTION_CLICK_ON_REVIEW_CARD = "click review card - product card"
    private const val ACTION_CLICK_VIEW_ALL_TRANSACTION = "click view all transaction"
    private const val IMPRESSION_ON_ORDER_STATUS = "impression on order status"
    private const val IMPRESSION_ON_REVIEW_CARD = "impression review card"
    private const val TEMPLATE_GLOBAL_MENU = "/global_menu - order_status_card"
    private const val PROMOTION_NAME_FORMAT = "/%s - %s"
    private const val GLOBAL_MENU = "global_menu"
    private const val REVIEW_CARD = "review_card"
    private const val PROMOTION_ID_FORMAT = "%s_%s_%s"
    private const val EVENT_LABEL_CLICK_REVIEW_STAR_RATING_FORMAT = "%s - %s - %s - %s"
    private const val EVENT_LABEL_CLICK_REVIEW_FORMAT = "%s - %s - %s"
    private const val STAR_RATING = "star rating"
    private const val PRODUCT_CARD = "product card"
    private const val CREATIVE_NAME_CLICK_REVIEW_FORMAT = "%s_%s"
    private const val ITEM_NAME_WISHLIST = "/global_menu - wishlist_card"
    private const val IMPRESSION_ON_WISHLIST_CARD = "impression wishlist card"
    private const val ACTION_CLICK_ON_WISHLIST_CARD = "click wishlist card"
    private const val ACTION_CLICK_ON_WISHLIST_VIEW_ALL = "click view all wishlist"
    private const val ITEM_NAME_FAVORITE_SHOP = "/global_menu - favorite_shop_card"
    private const val IMPRESSION_ON_FAVORITE_SHOP_CARD = "impression favorite shop card"
    private const val ACTION_CLICK_ON_FAVORITE_SHOP_CARD = "click favorite shop card"
    private const val ACTION_CLICK_ON_FAVORITE_SHOP_VIEW_ALL = "click view all favorite shop"
    private const val ACTION_CLICK_ON_REVIEW_VIEW_ALL = "click view all review"
    private const val ITEM_ID_FORMAT = "0_%s"
    private const val FORMAT_DASH_TWO_VALUES = "%s - %s"
    private const val WISHLIST_IMPRESSION_TRACKER_ID = "30836"
    private const val WISHLIST_CLICK_TRACKER_ID = "30837"

    fun clickOnAllTransaction(userId: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = ACTION_CLICK_ON_ALL_TRANSACTION,
            eventLabel = DEFAULT_EMPTY
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    fun clickOnTicket(userId: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = EVENT_CLICK_NAVIGATION_DRAWER,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = ACTION_CLICK_ON_TICKET,
            eventLabel = DEFAULT_EMPTY
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    fun clickOnReview(userId: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = EVENT_CLICK_NAVIGATION_DRAWER,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = ACTION_CLICK_ON_REVIEW,
            eventLabel = DEFAULT_EMPTY
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    fun clickOnWishlist(userId: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = EVENT_CLICK_NAVIGATION_DRAWER,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = ACTION_CLICK_ON_WISHLIST,
            eventLabel = DEFAULT_EMPTY
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    fun clickOnTokoFavorit(userId: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = EVENT_CLICK_NAVIGATION_DRAWER,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = ACTION_CLICK_ON_FAVOURITE_SHOP,
            eventLabel = DEFAULT_EMPTY
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    fun clickOnOrderStatus(userId: String, orderLabel: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = ACTION_CLICK_ON_ORDER_STATUS,
            eventLabel = orderLabel
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    fun getImpressionOnOrderStatus(userId: String, orderLabel: String, position: Int, bannerId: String = "0", orderId: String): HashMap<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = IMPRESSION_ON_ORDER_STATUS,
            eventLabel = "",
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
            .appendCurrentSite(DEFAULT_CURRENT_SITE)
            .appendUserId(userId)
            .appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .build() as HashMap<String, Any>
    }

    fun getImpressionOnWishlist(userId: String, position: Int, wishlistModel: NavWishlistModel): HashMap<String, Any> {
        return BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = IMPRESSION_ON_WISHLIST_CARD,
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
            .appendCurrentSite(DEFAULT_CURRENT_SITE)
            .appendUserId(userId)
            .appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .appendCustomKeyValue(TrackerId.KEY, WISHLIST_IMPRESSION_TRACKER_ID)
            .build() as HashMap<String, Any>
    }

    fun clickOnWishlistItem(userId: String, wishlistModel: NavWishlistModel, position: Int) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Category.KEY, CATEGORY_GLOBAL_MENU)
        bundle.putString(Action.KEY, ACTION_CLICK_ON_WISHLIST_CARD)
        bundle.putString(Label.KEY, wishlistModel.id)
        bundle.putString(CurrentSite.KEY, DEFAULT_CURRENT_SITE)
        bundle.putString(UserId.KEY, userId)
        bundle.putString(BusinessUnit.KEY, DEFAULT_BUSINESS_UNIT)
        val promotions = arrayListOf(
            Bundle().apply {
                putString(Promotion.CREATIVE_NAME, Value.EMPTY)
                putString(Promotion.CREATIVE_SLOT, (position + 1).toString())
                putString(Items.ITEM_ID, "0")
                putString(Items.ITEM_NAME, ITEM_NAME_WISHLIST)
            }
        )
        bundle.putParcelableArrayList(Promotion.KEY, promotions)
        bundle.putString(TrackerId.KEY, WISHLIST_CLICK_TRACKER_ID)
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    fun clickOnWishlistViewAll() {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Category.KEY, CATEGORY_GLOBAL_MENU)
        bundle.putString(Action.KEY, ACTION_CLICK_ON_WISHLIST_VIEW_ALL)
        bundle.putString(Label.KEY, Label.NONE)
        bundle.putString(CurrentSite.KEY, DEFAULT_CURRENT_SITE)
        bundle.putString(BusinessUnit.KEY, DEFAULT_BUSINESS_UNIT)
        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }

    fun getImpressionOnFavoriteShop(userId: String, position: Int, favoriteShopModel: NavFavoriteShopModel): HashMap<String, Any> {
        return BaseTrackerBuilder().constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = IMPRESSION_ON_FAVORITE_SHOP_CARD,
            eventLabel = Value.EMPTY,
            promotions = listOf(
                Promotion(
                    creative = Value.EMPTY,
                    id = ITEM_ID_FORMAT.format(favoriteShopModel.id),
                    name = ITEM_NAME_FAVORITE_SHOP,
                    creativeUrl = Value.EMPTY,
                    position = (position + 1).toString()
                )
            )
        )
            .appendCurrentSite(DEFAULT_CURRENT_SITE)
            .appendUserId(userId)
            .appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .build() as HashMap<String, Any>
    }

    fun clickOnFavoriteShopItem(userId: String, favoriteShopModel: NavFavoriteShopModel, position: Int) {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Category.KEY, CATEGORY_GLOBAL_MENU)
        bundle.putString(Action.KEY, ACTION_CLICK_ON_FAVORITE_SHOP_CARD)
        bundle.putString(Label.KEY, favoriteShopModel.id)
        bundle.putString(CurrentSite.KEY, DEFAULT_CURRENT_SITE)
        bundle.putString(UserId.KEY, userId)
        bundle.putString(BusinessUnit.KEY, DEFAULT_BUSINESS_UNIT)
        val promotions = arrayListOf(
            Bundle().apply {
                putString(Promotion.CREATIVE_NAME, Value.EMPTY)
                putString(Promotion.CREATIVE_SLOT, (position + 1).toString())
                putString(Items.ITEM_ID, ITEM_ID_FORMAT.format(favoriteShopModel.id))
                putString(Items.ITEM_NAME, ITEM_NAME_FAVORITE_SHOP)
            }
        )
        bundle.putParcelableArrayList(Promotion.KEY, promotions)
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    fun clickOnFavoriteShopViewAll() {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Category.KEY, CATEGORY_GLOBAL_MENU)
        bundle.putString(Action.KEY, ACTION_CLICK_ON_FAVORITE_SHOP_VIEW_ALL)
        bundle.putString(Label.KEY, Label.NONE)
        bundle.putString(CurrentSite.KEY, DEFAULT_CURRENT_SITE)
        bundle.putString(BusinessUnit.KEY, DEFAULT_BUSINESS_UNIT)
        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }

    fun getImpressionOnReviewProduct(position: Int, userId: String, element: NavReviewModel): HashMap<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val positionCard = (position + 1).toString()
        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = IMPRESSION_ON_REVIEW_CARD,
            eventLabel = DEFAULT_EMPTY,
            promotions = listOf(
                Promotion(
                    creative = DEFAULT_EMPTY,
                    id = PROMOTION_ID_FORMAT.format(DEFAULT_BANNER, element.reputationId, element.productId),
                    name = PROMOTION_NAME_FORMAT.format(GLOBAL_MENU, REVIEW_CARD),
                    creativeUrl = DEFAULT_EMPTY,
                    position = positionCard
                )
            )
        )
            .appendCurrentSite(DEFAULT_CURRENT_SITE)
            .appendUserId(userId)
            .appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
            .appendCustomKeyValue(PAGE_SOURCE, DEFAULT_PAGE_SOURCE)
            .build() as HashMap<String, Any>
    }

    fun getClickReviewStars(position: Int, userId: String, element: NavReviewModel, starRating: String): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, ACTION_CLICK_ON_REVIEW_STAR_RATING)
        bundle.putString(Category.KEY, CATEGORY_GLOBAL_MENU)
        bundle.putString(
            Label.KEY,
            EVENT_LABEL_CLICK_REVIEW_STAR_RATING_FORMAT.format(
                STAR_RATING,
                element.reputationId,
                starRating,
                element.productId
            )
        )
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(PAGE_SOURCE, DEFAULT_PAGE_SOURCE)
        bundle.putString(UserId.KEY, userId)
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
                DEFAULT_BANNER,
                element.reputationId,
                element.productId
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        return Pair(Event.SELECT_CONTENT, bundle)
    }

    fun getClickReviewCard(position: Int, userId: String, element: NavReviewModel): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(Action.KEY, ACTION_CLICK_ON_REVIEW_CARD)
        bundle.putString(Category.KEY, CATEGORY_GLOBAL_MENU)
        bundle.putString(
            Label.KEY,
            EVENT_LABEL_CLICK_REVIEW_FORMAT.format(
                PRODUCT_CARD,
                element.reputationId,
                element.productId
            )
        )
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(PAGE_SOURCE, DEFAULT_PAGE_SOURCE)
        bundle.putString(UserId.KEY, userId)
        val promotion = Bundle()
        promotion.putString(
            Promotion.CREATIVE_NAME,
            CREATIVE_NAME_CLICK_REVIEW_FORMAT.format(PRODUCT_CARD, DEFAULT_EMPTY)
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
                DEFAULT_BANNER,
                element.reputationId,
                element.productId
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        return Pair(Event.SELECT_CONTENT, bundle)
    }

    fun getClickViewAllTransaction() {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Action.KEY, ACTION_CLICK_VIEW_ALL_TRANSACTION)
        bundle.putString(Category.KEY, CATEGORY_GLOBAL_MENU)
        bundle.putString(Label.KEY, DEFAULT_EMPTY)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }

    fun clickOnReviewViewAll() {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Category.KEY, CATEGORY_GLOBAL_MENU)
        bundle.putString(Action.KEY, ACTION_CLICK_ON_REVIEW_VIEW_ALL)
        bundle.putString(Label.KEY, Label.NONE)
        bundle.putString(CurrentSite.KEY, DEFAULT_CURRENT_SITE)
        bundle.putString(BusinessUnit.KEY, DEFAULT_BUSINESS_UNIT)
        getTracker().sendEnhanceEcommerceEvent(Event.CLICK_HOMEPAGE, bundle)
    }
}
