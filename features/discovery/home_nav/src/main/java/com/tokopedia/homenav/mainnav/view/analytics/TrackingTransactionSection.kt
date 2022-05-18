package com.tokopedia.homenav.mainnav.view.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.homenav.common.TrackingConst.CATEGORY_GLOBAL_MENU
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_BUSINESS_UNIT
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_CURRENT_SITE
import com.tokopedia.homenav.common.TrackingConst.DEFAULT_EMPTY
import com.tokopedia.homenav.common.TrackingConst.EVENT_CLICK_NAVIGATION_DRAWER
import com.tokopedia.homenav.mainnav.domain.model.NavFavoriteShopModel
import com.tokopedia.homenav.mainnav.domain.model.NavWishlistModel
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
    private const val TEMPLATE_GLOBAL_MENU = "/global_menu - order_status_card"

    // Tracker for Global Menu Revamp (Me Page)
    private const val DIMENSION_125 = "dimension125"
    private const val DIMENSION_40 = "dimension40"
    private const val LIST_WISHLIST = "/global_menu - wishlist_card"
    private const val IMPRESSION_ON_WISHLIST_CARD = "impression on wishlist card"
    private const val ACTION_CLICK_ON_WISHLIST_CARD = "click wishlist card"
    private const val ACTION_CLICK_ON_WISHLIST_VIEW_ALL = "click view all wishlist"
    private const val ITEM_NAME_FAVORITE_SHOP = "/global_menu - favorite_shop_card"
    private const val IMPRESSION_ON_FAVORITE_SHOP_CARD = "impression on favorite shop card"
    private const val ACTION_CLICK_ON_FAVORITE_SHOP_CARD = "click favorite shop card"
    private const val ACTION_CLICK_ON_FAVORITE_SHOP_VIEW_ALL = "click view all favorite shop"

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

    fun getImpressionOnWishlist(userId: String, position: Int, wishlistModel: NavWishlistModel): HashMap<String, Any> {
        return DataLayer.mapOf(
            Event.KEY, Event.PRODUCT_VIEW,
            Category.KEY, CATEGORY_GLOBAL_MENU,
            Action.KEY, IMPRESSION_ON_WISHLIST_CARD,
            Label.KEY, Label.NONE,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            UserId.KEY, userId,
            ItemList.KEY, LIST_WISHLIST,
            Items.KEY, convertWishlistTracking(
                index = position+1,
                wishlistId = wishlistModel.wishlistId,
                brand = Value.NONE_OTHER,
                category = wishlistModel.categoryBreadcrumb,
                productId = wishlistModel.productId,
                productName = wishlistModel.productName,
                productVariant = wishlistModel.variant,
                price = wishlistModel.priceFmt
            )
        ) as HashMap<String, Any>
    }

    fun clickOnWishlistItem(userId: String, wishlistModel: NavWishlistModel, position: Int) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = ACTION_CLICK_ON_WISHLIST_CARD,
            eventLabel = Label.FORMAT_2_ITEMS.format(wishlistModel.wishlistId, wishlistModel.productId)
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        trackingBuilder.appendCustomKeyValue(ItemList.KEY, LIST_WISHLIST)
        val items = convertWishlistTracking(
            index = position+1,
            wishlistId = wishlistModel.wishlistId,
            brand = Value.NONE_OTHER,
            category = wishlistModel.categoryBreadcrumb,
            productId = wishlistModel.productId,
            productName = wishlistModel.productName,
            productVariant = wishlistModel.variant,
            price = wishlistModel.priceFmt
        )
        trackingBuilder.appendCustomKeyValue(Items.KEY, items)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    fun clickOnWishlistViewAll() {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = ACTION_CLICK_ON_WISHLIST_VIEW_ALL,
            eventLabel = Label.NONE
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    private fun convertWishlistTracking(
        index: Int,
        wishlistId: String,
        brand: String,
        category: String,
        productId: String,
        productName: String,
        productVariant: String,
        price: String
    ) : List<Map<String, Any>> {
        return listOf(
            DataLayer.mapOf(
                DIMENSION_125, wishlistId,
                DIMENSION_40, LIST_WISHLIST,
                Items.INDEX, index+1,
                Items.ITEM_BRAND, brand,
                Items.ITEM_CATEGORY, category,
                Items.ITEM_ID, productId,
                Items.ITEM_NAME, productName,
                Items.ITEM_VARIANT, productVariant,
                Items.PRICE, price
            )
        )
    }

    fun getImpressionOnFavoriteShop(userId: String, position: Int, favoriteShopModel: NavFavoriteShopModel): HashMap<String, Any> {
        return DataLayer.mapOf(
            Event.KEY, Event.PRODUCT_VIEW,
            Category.KEY, CATEGORY_GLOBAL_MENU,
            Action.KEY, IMPRESSION_ON_FAVORITE_SHOP_CARD,
            Label.KEY, Label.NONE,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            UserId.KEY, userId,
            Promotion.KEY, convertFavoriteShopTracking(
                index = position+1,
                name = favoriteShopModel.name,
                itemId = favoriteShopModel.id
            )
        ) as HashMap<String, Any>
    }

    fun clickOnFavoriteShopItem(userId: String, favoriteShopModel: NavFavoriteShopModel, position: Int) {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = Event.PROMO_CLICK,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = ACTION_CLICK_ON_FAVORITE_SHOP_CARD,
            eventLabel = favoriteShopModel.id
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendUserId(userId)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        trackingBuilder.appendCustomKeyValue(ItemList.KEY, LIST_WISHLIST)
        val promotions = convertFavoriteShopTracking(position, favoriteShopModel.name, favoriteShopModel.id)
        trackingBuilder.appendCustomKeyValue(Promotion.KEY, promotions)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    fun clickOnFavoriteShopViewAll() {
        val trackingBuilder = BaseTrackerBuilder()
        trackingBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = CATEGORY_GLOBAL_MENU,
            eventAction = ACTION_CLICK_ON_FAVORITE_SHOP_VIEW_ALL,
            eventLabel = Label.NONE
        )
        trackingBuilder.appendCurrentSite(DEFAULT_CURRENT_SITE)
        trackingBuilder.appendBusinessUnit(DEFAULT_BUSINESS_UNIT)
        getTracker().sendGeneralEvent(trackingBuilder.build())
    }

    private fun convertFavoriteShopTracking(
        index: Int,
        name: String,
        itemId: String
    ) : List<Map<String, Any>> {
        return listOf(
            DataLayer.mapOf(
                Promotion.CREATIVE_NAME, name,
                Promotion.CREATIVE_SLOT, index,
                Items.ITEM_ID, itemId,
                Items.ITEM_NAME, ITEM_NAME_FAVORITE_SHOP
            )
        )
    }

}