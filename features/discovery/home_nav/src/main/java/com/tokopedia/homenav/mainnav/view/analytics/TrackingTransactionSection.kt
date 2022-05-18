package com.tokopedia.homenav.mainnav.view.analytics

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
    private const val IMPRESSION_ON_ORDER_STATUS = "impression on order status"
    private const val IMPRESSION_ON_REVIEW_CARD = "impression review card"
    private const val TEMPLATE_GLOBAL_MENU = "/global_menu - order_status_card"
    private const val PROMOTION_NAME_FORMAT = "/%s - %s"
    private const val GLOBAL_MENU = "global_menu"
    private const val REVIEW_CARD = "review_card"
    private const val PROMOTION_ID_FORMAT = "%s_%s_%s"

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

    fun getImpressionOnReviewOrder(position: Int, userId: String, element: NavReviewOrder) : HashMap<String, Any>  {
        val trackingBuilder = BaseTrackerBuilder()
        val positionCard = (position + 1).toString()
        return trackingBuilder.constructBasicPromotionView(
                event = Event.PROMO_VIEW,
                eventCategory = CATEGORY_GLOBAL_MENU,
                eventAction = IMPRESSION_ON_REVIEW_CARD,
                eventLabel = DEFAULT_EMPTY,
                promotions = listOf(Promotion(
                        creative = DEFAULT_EMPTY,
                        id = String.format(PROMOTION_ID_FORMAT, element.bannerId, element.reputationId, element.productId),
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
}