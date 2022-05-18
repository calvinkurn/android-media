package com.tokopedia.homenav.mainnav.view.analytics

import android.os.Bundle
import com.tokopedia.homenav.common.TrackingConst.CATEGORY_GLOBAL_MENU
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_BUSINESS_UNIT
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_CURRENT_SITE
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_EMPTY
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_PAGE_SOURCE
import com.tokopedia.homenav.common.TrackingConst.EVENT_CLICK_NAVIGATION_DRAWER
import com.tokopedia.homenav.common.TrackingConst.PAGE_SOURCE
import com.tokopedia.homenav.mainnav.domain.model.NavReviewOrder
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object TrackingTransactionSection: BaseTrackerConst() {
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

    fun clickOnAllTransaction(userId: String) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
                event = EVENT_CLICK_NAVIGATION_DRAWER,
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
                event = EVENT_CLICK_NAVIGATION_DRAWER,
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
                promotions = listOf(Promotion(
                        creative = orderLabel,
                        id = String.format("%s - %s", bannerId, orderId),
                        name = TEMPLATE_GLOBAL_MENU,
                        creativeUrl = "",
                        position = (position + 1).toString()
                )))
                .appendCurrentSite(DEFAULT_CURRENT_SITE)
                .appendUserId(userId)
                .appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
                .build() as HashMap<String, Any>
    }

    fun getImpressionOnReviewProduct(position: Int, userId: String, element: NavReviewOrder) : HashMap<String, Any>  {
        val trackingBuilder = BaseTrackerBuilder()
        val positionCard = (position + 1).toString()
        return trackingBuilder.constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = CATEGORY_GLOBAL_MENU,
                eventAction = IMPRESSION_ON_REVIEW_CARD,
                eventLabel = DEFAULT_EMPTY,
                promotions = listOf(Promotion(
                        creative = DEFAULT_EMPTY,
                        id = PROMOTION_ID_FORMAT.format(element.bannerId, element.reputationId, element.productId),
                        name = PROMOTION_NAME_FORMAT.format(GLOBAL_MENU, REVIEW_CARD),
                        creativeUrl = DEFAULT_EMPTY,
                        position = positionCard
                )))
                .appendCurrentSite(DEFAULT_CURRENT_SITE)
                .appendUserId(userId)
                .appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
                .appendCustomKeyValue(PAGE_SOURCE, DEFAULT_PAGE_SOURCE)
                .build() as HashMap<String, Any>
    }

    fun getClickReviewStars(position: Int, userId: String, element: NavReviewOrder, starRating: String) : Pair<String, Bundle> {
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
                element.bannerId,
                element.reputationId,
                element.productId
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        return Pair(Ecommerce.PROMO_CLICK, bundle)
    }

    fun getClickReviewCard(position: Int, userId: String, element: NavReviewOrder) : Pair<String, Bundle> {
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
                element.bannerId,
                element.reputationId,
                element.productId
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        return Pair(Ecommerce.PROMO_CLICK, bundle)
    }

    fun getClickViewAllTransaction() : Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Action.KEY, ACTION_CLICK_VIEW_ALL_TRANSACTION)
        bundle.putString(Category.KEY, CATEGORY_GLOBAL_MENU)
        bundle.putString(Label.KEY, DEFAULT_EMPTY)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        return Pair(Event.CLICK_HOMEPAGE, bundle)
    }
}