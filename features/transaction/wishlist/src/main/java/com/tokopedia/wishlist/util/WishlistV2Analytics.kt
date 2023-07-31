package com.tokopedia.wishlist.util

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.recommendation_widget_common.extension.hasLabelGroupFulfillment
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.wishlist.data.model.WishlistV2UiModel
import java.util.HashMap

object WishlistV2Analytics {
    private const val EVENT = "event"
    private const val EVENT_CATEGORY = "eventCategory"
    private const val EVENT_ACTION = "eventAction"
    private const val EVENT_LABEL = "eventLabel"
    private const val SELECT_CONTENT = "select_content"
    private const val ADD_TO_CART = "add_to_cart"
    private const val VIEW_ITEM_LIST = "view_item_list"
    private const val SCREEN_NAME = "screenName"
    private const val PRODUCT_VIEW = "productView"
    private const val PRODUCT_CLICK = "productClick"
    private const val PROMO_VIEW = "promoView"
    private const val PROMO_CLICK = "promoClick"
    private const val ITEM_LIST = "item_list"
    private const val ITEM_NAME = "item_name"
    private const val ITEM_BRAND = "item_brand"
    private const val ITEM_ID = "item_id"
    private const val CATEGORY_ID = "category_id"
    private const val PRICE = "price"
    private const val QUANTITY = "quantity"
    private const val SHOP_ID = "shop_id"
    private const val SHOP_NAME = "shop_name"
    private const val SHOP_TYPE = "shop_type"
    private const val ECOMMERCE = "ecommerce"
    private const val PROMOTIONS = "promotions"
    private const val IMPRESSIONS = "impressions"
    private const val CURRENCY_CODE = "currencyCode"
    private const val IDR = "IDR"
    private const val DIMENSION_38 = "dimension38"
    private const val DIMENSION_40 = "dimension40"
    private const val DIMENSION_45 = "dimension45"
    private const val DIMENSION_79 = "dimension79"
    private const val DIMENSION_83 = "dimension83"
    private const val INDEX = "index"
    private const val ITEM_VARIANT = "item_variant"
    private const val ITEM_CATEGORY = "item_category"
    private const val WISHLIST = "/wishlist"
    private const val SCREEN_NAME_WISHLIST = "wishlist"
    private const val USER_ID = "userId"
    private const val WISHLIST_ID = "wishlistId"
    private const val ITEMS = "items"
    private const val CLICK = "click"
    private const val CLICK_WISHLIST = "clickWishlist"
    private const val WISHLIST_PAGE = "wishlist page"
    private const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    private const val PURCHASE_PLATFORM = "purchase platform"
    private const val CURRENT_SITE = "currentSite"
    private const val BUSINESS_UNIT = "businessUnit"
    private const val FIELD_ACTION_FIELD = "actionField"
    private const val EVENT_WISHLIST_PAGE = "wishlist page"
    private const val FIELD_PRODUCT_NAME = "name"
    private const val FIELD_PRODUCT_ID = "id"
    private const val FIELD_PRODUCT_PRICE = "price"
    private const val FIELD_PRODUCT_BRAND = "brand"
    private const val FIELD_PRODUCT_VARIANT = "variant"
    private const val FIELD_PRODUCT_CATEGORY = "category"
    private const val FIELD_PRODUCT_LIST = "list"
    private const val FIELD_PRODUCTS = "products"
    private const val FIELD_PRODUCT_POSITION = "position"
    private const val SUBMIT_SEARCH_FROM_CARI_PRODUK = "submit search from cari produk"
    private const val CLICK_URUTKAN_FILTER_CHIPS = "click urutkan filter chips"
    private const val CLICK_OPTION_ON_URUTKAN_FILTER_CHIPS = "click option on urutkan filter chips"
    private const val CLICK_KATEGORI_FILTER_CHIPS = "click kategori filter chips"
    private const val CLICK_OPTION_ON_KATEGORI_FILTER_CHIPS = "click option on kategori filter chips"
    private const val CLICK_PENAWARAN_FILTER_CHIPS = "click penawaran filter chips"
    private const val CLICK_SIMPAN_ON_PENAWARAN_FILTER_CHIPS = "click simpan on penawaran filter chips"
    private const val CLICK_STOK_FILTER_CHIPS = "click stok filter chips"
    private const val CLICK_OPTION_ON_STOK_FILTER_CHIPS = "click option on stok filter chips"
    private const val CLICK_X_CHIPS_TO_CLEAR_FILTER = "click x chips to clear filter"
    private const val CLICK_LAYOUT_SETTINGS = "click layout settings"
    private const val CLICK_ATUR_ON_WISHLIST_PAGE = "click atur on wishlist page"
    private const val CLICK_HAPUS_ON_POP_UP_MULTIPLE_WISHLIST_PRODUCT = "click hapus on pop up multiple wishlist product"
    private const val CLICK_BATAL_ON_POP_UP_MULTIPLE_WISHLIST_PRODUCT = "click batal on pop up multiple wishlist product"
    private const val CLICK_PRODUCT_CARD_ON_WISHLIST_PAGE = "click product card on wishlist page"
    private const val CLICK_ATC_ON_WISHLIST_PAGE = "click add to cart on wishlist page"
    private const val CLICK_LIHAT_ON_ATC_SUCCESS_TOASTER = "click lihat button on atc success toaster"
    private const val CLICK_LIHAT_BARANG_SERUPA_ON_PRODUCT_CARD = "click lihat barang serupa on product card"
    private const val CLICK_THREE_DOTS_ON_PRODUCT_CARD = "click three dots on product card"
    private const val CLICK_OPTION_ON_THREE_DOT_MENU = "click option on three dot menu"
    private const val CLICK_CARI_BARANG_ON_EMPTY_STATE_NO_ITEMS = "click cari barang on empty state no items"
    private const val CLICK_CARI_DI_TOKOPEDIA_ON_EMPTY_STATE_NO_SEARCH_RESULT = "click cari di tokopedia on empty state no search result"
    private const val CLICK_RESET_FILTER_ON_EMPTY_STATE_NO_FILTER_RESULT = "click reset filter on empty state no filter result"
    private const val EVENT_ACTION_IMPRESSION_BANNER_ADS = "impression - banner ads"
    private const val FIELD_PROMOTION_ID = "id"
    private const val FIELD_PROMOTION_NAME = "name"
    private const val FIELD_PROMOTION_CREATIVE = "creative"
    private const val FIELD_PROMOTION_CREATIVE_URL = "creative_url"
    private const val FIELD_PROMOTION_POSITION = "position"
    private const val FIELD_DIMENSION_83 = "dimension83"
    private const val IMPRESSION_TOPADS_LIST = "/wishlist - p%s - banner ads"
    private const val EVENT_ACTION_CLICK_BANNER_ADS = "click - banner ads"
    private const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN = "impression on product recommendation"
    private const val VALUE_NONE_OTHER = "none / other"
    private const val VALUE_BEBAS_ONGKIR = "bebas ongkir"
    private const val VALUE_BEBAS_ONGKIR_EXTRA = "bebas ongkir extra"
    private const val IMPRESSION_EMPTY_LIST_TOPADS = "/wishlist - rekomendasi untuk anda - empty_wishlist - %s - product topads"
    private const val IMPRESSION_EMPTY_LIST = "/wishlist - rekomendasi untuk anda - empty_wishlist - %s"
    private const val EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION = "click on product recommendation"
    private const val IMPRESSION_LIST_RECOMMENDATION = "/wishlist - rekomendasi untuk anda - %s%s"
    private const val RECOMMENDATION_CLICK_ITEM_TRACK = "/wishlist - rekomendasi untuk anda"
    private const val RECOMMENDATION_CLICK_TOPADS_ITEM_TRACK = " - product topads"
    private const val RECOMMENDATION_CLICK_HORIZONTAL_ITEM_TRACK = " - custom horizontal"
    private const val CLICK_COMMUNICATION = "clickCommunication"
    private const val VIEW_COMMUNICATION_IRIS = "viewCommunicationIris"
    private const val CLICK_SHARE_BUTTON = "click - share button"
    private const val CLICK_CLOSE_SHARE_BOTTOM_SHEET = "click - close share bottom sheet"
    private const val CLICK_SHARING_CHANNEL = "click - sharing channel"
    private const val VIEW_ON_SHARING_CHANNEL = "view on sharing channel"
    private const val SHARING_EXPERIENCE = "sharingexperience"
    private const val PRODUCT_ID = "productId"

    fun submitSearchFromCariProduk(keyword: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            SUBMIT_SEARCH_FROM_CARI_PRODUK,
            keyword
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickUrutkanFilterChips() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_URUTKAN_FILTER_CHIPS,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickOptionOnUrutkanFilterChips(optionChosen: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_OPTION_ON_URUTKAN_FILTER_CHIPS,
            optionChosen
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickKategoriFilterChips() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_KATEGORI_FILTER_CHIPS,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickOptionOnKategoriFilterChips(optionChosen: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_OPTION_ON_KATEGORI_FILTER_CHIPS,
            optionChosen
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickPenawaranFilterChips() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_PENAWARAN_FILTER_CHIPS,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickSimpanOnPenawaranFilterChips(optionChosen: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_SIMPAN_ON_PENAWARAN_FILTER_CHIPS,
            optionChosen
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickStokFilterChips() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_STOK_FILTER_CHIPS,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickOptionOnStokFilterChips(optionChosen: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_OPTION_ON_STOK_FILTER_CHIPS,
            optionChosen
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickXChipsToClearFilter() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_X_CHIPS_TO_CLEAR_FILTER,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickLayoutSettings(typeLayout: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_LAYOUT_SETTINGS,
            typeLayout
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickAturOnWishlist() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_ATUR_ON_WISHLIST_PAGE,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickHapusOnPopUpMultipleWishlistProduct() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_HAPUS_ON_POP_UP_MULTIPLE_WISHLIST_PRODUCT,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickBatalOnPopUpMultipleWishlistProduct() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_BATAL_ON_POP_UP_MULTIPLE_WISHLIST_PRODUCT,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickProductCard(wishlistItem: WishlistV2UiModel.Item, userId: String, position: Int) {
        val arrayWishlistItems = arrayListOf<Bundle>()
        val bundleProduct = Bundle().apply {
            putString(DIMENSION_38, "")
            putString(DIMENSION_40, WISHLIST)
            putString(DIMENSION_79, wishlistItem.shop.id)
            putString(DIMENSION_83, "")
            putString(INDEX, position.toString())
            putString(ITEM_BRAND, "")
            putString(ITEM_NAME, wishlistItem.name)
            putString(ITEM_ID, wishlistItem.id)
            putString(ITEM_CATEGORY, "")
            putString(PRICE, wishlistItem.price)
            putString(ITEM_VARIANT, "")
            putString(INDEX, position.toString())
        }
        arrayWishlistItems.add(bundleProduct)

        val bundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_CATEGORY, WISHLIST_PAGE)
            putString(EVENT_ACTION, CLICK)
            putString(EVENT_LABEL, CLICK_PRODUCT_CARD_ON_WISHLIST_PAGE)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(USER_ID, userId)
            putString(WISHLIST_ID, wishlistItem.wishlistId)
            putString(BUSINESS_UNIT, PURCHASE_PLATFORM)
            putString(ITEM_LIST, WISHLIST)
            putParcelableArrayList(ITEMS, arrayWishlistItems)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    private fun convertOrderItemToDataImpressionObject(wishlistItem: WishlistV2UiModel.Item, position: String): List<Any> {
        return listOf(
            DataLayer.mapOf(
                ITEM_NAME, wishlistItem.name,
                ITEM_ID, wishlistItem.id,
                PRICE, wishlistItem.price,
                ITEM_BRAND, "",
                ITEM_VARIANT, "",
                ITEM_CATEGORY, "",
                INDEX, position,
                DIMENSION_38, "",
                DIMENSION_40, WISHLIST,
                DIMENSION_79, wishlistItem.shop.id,
                DIMENSION_83, ""
            )
        )
    }

    fun clickAtcOnWishlist(wishlistItem: WishlistV2UiModel.Item, userId: String, position: Int, cartId: String) {
        val arrayWishlistItems = arrayListOf<Bundle>()
        val bundleProduct = Bundle().apply {
            putString(CATEGORY_ID, "")
            putString(DIMENSION_38, "")
            putString(DIMENSION_40, WISHLIST)
            putString(DIMENSION_45, cartId)
            putString(DIMENSION_79, wishlistItem.shop.id)
            putString(DIMENSION_83, "")
            putString(INDEX, position.toString())
            putString(ITEM_BRAND, "")
            putString(ITEM_NAME, wishlistItem.name)
            putString(ITEM_ID, wishlistItem.id)
            putString(ITEM_CATEGORY, "")
            putString(PRICE, wishlistItem.price)
            putString(QUANTITY, wishlistItem.minOrder)
            putString(SHOP_ID, wishlistItem.shop.id)
            putString(SHOP_NAME, wishlistItem.shop.name)
            putString(SHOP_TYPE, "")
            putString(ITEM_VARIANT, "")
        }
        arrayWishlistItems.add(bundleProduct)

        val bundle = Bundle().apply {
            putString(EVENT, ADD_TO_CART)
            putString(EVENT_CATEGORY, WISHLIST_PAGE)
            putString(EVENT_ACTION, CLICK_ATC_ON_WISHLIST_PAGE)
            putString(EVENT_LABEL, "")
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(USER_ID, userId)
            putString(WISHLIST_ID, wishlistItem.wishlistId)
            putString(BUSINESS_UNIT, PURCHASE_PLATFORM)
            putParcelableArrayList(ITEMS, arrayWishlistItems)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    fun clickLihatButtonOnAtcSuccessToaster() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_LIHAT_ON_ATC_SUCCESS_TOASTER,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickLihatBarangSerupaOnProductCard() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_LIHAT_BARANG_SERUPA_ON_PRODUCT_CARD,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickThreeDotsOnProductCard() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_THREE_DOTS_ON_PRODUCT_CARD,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickOptionOnThreeDotsMenu(optionChosen: String) {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_OPTION_ON_THREE_DOT_MENU,
            optionChosen
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickCariBarangOnEmptyStateNoItems() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_CARI_BARANG_ON_EMPTY_STATE_NO_ITEMS,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickCariDiTokopediaOnEmptyStateNoSearchResult() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_CARI_DI_TOKOPEDIA_ON_EMPTY_STATE_NO_SEARCH_RESULT,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickResetFilterOnEmptyStateNoFilterResult() {
        val event = TrackAppUtils.gtmData(
            CLICK_WISHLIST,
            WISHLIST_PAGE,
            CLICK_RESET_FILTER_ON_EMPTY_STATE_NO_FILTER_RESULT,
            ""
        )
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun impressTopAdsBanner(userId: String, topAdsImageViewModel: TopAdsImageViewModel, position: Int) {
        val map = DataLayer.mapOf(
            EVENT, PROMO_VIEW,
            EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
            EVENT_ACTION, EVENT_ACTION_IMPRESSION_BANNER_ADS,
            EVENT_LABEL, "",
            SCREEN_NAME, WISHLIST,
            BUSINESS_UNIT, PURCHASE_PLATFORM,
            CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
            USER_ID, userId,
            ECOMMERCE,
            DataLayer.mapOf(
                PROMO_VIEW,
                DataLayer.mapOf(
                    PROMOTIONS,
                    DataLayer.listOf(
                        convertBannerTopAdsToDataTrackingObject(
                            item = topAdsImageViewModel,
                            position = position
                        )
                    )
                )
            )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map as HashMap<String, Any>)
    }

    fun clickTopAdsBanner(item: TopAdsImageViewModel, userId: String, position: Int) {
        val map = DataLayer.mapOf(
            EVENT, PROMO_CLICK,
            EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
            EVENT_ACTION, EVENT_ACTION_CLICK_BANNER_ADS,
            EVENT_LABEL, "",
            SCREEN_NAME, WISHLIST,
            BUSINESS_UNIT, PURCHASE_PLATFORM,
            CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
            USER_ID, userId,
            ECOMMERCE,
            DataLayer.mapOf(
                PROMO_CLICK,
                DataLayer.mapOf(
                    PROMOTIONS,
                    DataLayer.listOf(
                        convertBannerTopAdsToDataTrackingObject(
                            item = item,
                            position = position
                        )
                    )
                )
            )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map as HashMap<String, Any>)
    }

    private fun convertBannerTopAdsToDataTrackingObject(
        item: TopAdsImageViewModel,
        position: Int
    ): Any {
        return DataLayer.mapOf(
            FIELD_PROMOTION_ID, item.bannerId ?: "0",
            FIELD_PROMOTION_NAME, IMPRESSION_TOPADS_LIST.format((position + 1).toString()),
            FIELD_PROMOTION_CREATIVE, item.imageUrl,
            FIELD_PROMOTION_CREATIVE_URL, item.imageUrl,
            FIELD_PROMOTION_POSITION, "1"
        )
    }

    fun impressionEmptyWishlistRecommendation(trackingQueue: TrackingQueue, item: RecommendationItem, position: Int) {
        val map = DataLayer.mapOf(
            EVENT, PRODUCT_VIEW,
            EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
            EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN,
            EVENT_LABEL, "",
            ECOMMERCE,
            DataLayer.mapOf(
                CURRENCY_CODE,
                IDR,
                IMPRESSIONS,
                DataLayer.listOf(
                    convertRecommendationItemToDataImpressionObject(
                        item = item,
                        list = String.format(if (item.isTopAds) IMPRESSION_EMPTY_LIST_TOPADS else IMPRESSION_EMPTY_LIST, item.recommendationType),
                        position = position
                    )
                )
            )
        )
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun clickRecommendationItem(item: RecommendationItem, position: Int, userId: String) {
        var list = RECOMMENDATION_CLICK_ITEM_TRACK
        if (item.isTopAds) list += RECOMMENDATION_CLICK_TOPADS_ITEM_TRACK

        val arrayListBundleItems = arrayListOf<Bundle>()
        val bundleClick = Bundle().apply {
            putString(ITEM_NAME, item.name)
            putString(ITEM_ID, item.productId.toString())
            putString(PRICE, item.price)
            putString(ITEM_BRAND, VALUE_NONE_OTHER)
            putString(ITEM_CATEGORY, item.categoryBreadcrumbs)
            putString(ITEM_VARIANT, VALUE_NONE_OTHER)
            putString(INDEX, position.toString())
        }
        arrayListBundleItems.add(bundleClick)

        val bundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_CATEGORY, EVENT_WISHLIST_PAGE)
            putString(EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION)
            putString(EVENT_LABEL, "")
            putString(SCREEN_NAME, SCREEN_NAME_WISHLIST)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(USER_ID, userId)
            putString(BUSINESS_UNIT, PURCHASE_PLATFORM)
            putString(CURRENCY_CODE, IDR)
            putString(ITEM_LIST, list)
            putParcelableArrayList(ITEMS, arrayListBundleItems)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    private fun convertRecommendationItemToDataImpressionObject(
        item: RecommendationItem,
        list: String,
        position: Int
    ): Any {
        return DataLayer.mapOf(
            FIELD_PRODUCT_NAME, item.name,
            FIELD_PRODUCT_ID, item.productId.toString(),
            FIELD_PRODUCT_PRICE, item.priceInt.toString(),
            FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
            FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
            FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
            FIELD_PRODUCT_LIST, list,
            FIELD_PRODUCT_POSITION, position,
            FIELD_DIMENSION_83, getBebasOngkirValue(item)
        )
    }

    private fun getBebasOngkirValue(item: RecommendationItem): String {
        val hasFulfillment = item.labelGroupList.hasLabelGroupFulfillment()
        return if (item.isFreeOngkirActive && hasFulfillment) {
            VALUE_BEBAS_ONGKIR_EXTRA
        } else if (item.isFreeOngkirActive && !hasFulfillment) {
            VALUE_BEBAS_ONGKIR
        } else {
            VALUE_NONE_OTHER
        }
    }

    private fun convertRecommendationItemToDataClickObject(
        item: RecommendationItem,
        list: String,
        position: Int
    ): Any {
        return DataLayer.mapOf(
            FIELD_ACTION_FIELD,
            DataLayer.mapOf(
                FIELD_PRODUCT_LIST,
                list
            ),
            FIELD_PRODUCTS,
            DataLayer.listOf(
                DataLayer.mapOf(
                    FIELD_PRODUCT_NAME, item.name,
                    FIELD_PRODUCT_ID, item.productId.toString(),
                    FIELD_PRODUCT_PRICE, item.priceInt.toString(),
                    FIELD_PRODUCT_BRAND, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                    FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                    FIELD_PRODUCT_POSITION, position,
                    FIELD_DIMENSION_83, if (item.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else VALUE_NONE_OTHER
                )
            )
        )
    }

    fun impressionCarouselRecommendationItem(trackingQueue: TrackingQueue, item: RecommendationItem, position: Int) {
        val map = DataLayer.mapOf(
            EVENT, PRODUCT_VIEW,
            EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
            EVENT_ACTION, EVENT_ACTION_IMPRESSION_PRODUCT_RECOMMENDATION_LOGIN,
            EVENT_LABEL, "",
            ECOMMERCE,
            DataLayer.mapOf(
                CURRENCY_CODE,
                IDR,
                IMPRESSIONS,
                DataLayer.listOf(
                    convertRecommendationItemToDataImpressionObject(
                        item = item,
                        list = String.format(IMPRESSION_LIST_RECOMMENDATION, item.recommendationType, if (item.isTopAds) " - product topads" else ""),
                        position = position
                    )
                )
            )
        )
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    fun clickCarouselRecommendationItem(item: RecommendationItem, position: Int, userId: String) {
        var list = RECOMMENDATION_CLICK_ITEM_TRACK
        if (item.isTopAds) list += RECOMMENDATION_CLICK_TOPADS_ITEM_TRACK
        list += RECOMMENDATION_CLICK_HORIZONTAL_ITEM_TRACK

        val arrayListBundleItems = arrayListOf<Bundle>()
        val bundleClick = Bundle().apply {
            putString(ITEM_NAME, item.name)
            putString(ITEM_ID, item.productId.toString())
            putString(PRICE, item.price)
            putString(ITEM_BRAND, VALUE_NONE_OTHER)
            putString(ITEM_CATEGORY, item.categoryBreadcrumbs)
            putString(ITEM_VARIANT, VALUE_NONE_OTHER)
            putString(INDEX, position.toString())
        }
        arrayListBundleItems.add(bundleClick)

        val bundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_CATEGORY, EVENT_WISHLIST_PAGE)
            putString(EVENT_ACTION, EVENT_ACTION_CLICK_PRODUCT_RECOMMENDATION)
            putString(EVENT_LABEL, "")
            putString(SCREEN_NAME, SCREEN_NAME_WISHLIST)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(USER_ID, userId)
            putString(BUSINESS_UNIT, PURCHASE_PLATFORM)
            putString(CURRENCY_CODE, IDR)
            putString(ITEM_LIST, list)
            putParcelableArrayList(ITEMS, arrayListBundleItems)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    fun clickShareLinkProduct(wishlistId: String, productId: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, CLICK_COMMUNICATION,
                EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                EVENT_ACTION, CLICK_SHARE_BUTTON,
                EVENT_LABEL, wishlistId,
                BUSINESS_UNIT, SHARING_EXPERIENCE,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                PRODUCT_ID, productId,
                USER_ID, userId,
                WISHLIST_ID, wishlistId
            )
        )
    }

    fun clickCloseShareBottomSheet(wishlistId: String, productId: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, CLICK_COMMUNICATION,
                EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                EVENT_ACTION, CLICK_CLOSE_SHARE_BOTTOM_SHEET,
                EVENT_LABEL, wishlistId,
                BUSINESS_UNIT, SHARING_EXPERIENCE,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                PRODUCT_ID, productId,
                USER_ID, userId,
                WISHLIST_ID, wishlistId
            )
        )
    }

    fun clickSharingChannel(wishlistId: String, productId: String, userId: String, channel: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, CLICK_COMMUNICATION,
                EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                EVENT_ACTION, CLICK_SHARING_CHANNEL,
                EVENT_LABEL, "$channel - $wishlistId",
                BUSINESS_UNIT, SHARING_EXPERIENCE,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                PRODUCT_ID, productId,
                USER_ID, userId,
                WISHLIST_ID, wishlistId
            )
        )
    }

    fun viewOnSharingChannel(wishlistId: String, productId: String, userId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            DataLayer.mapOf(
                EVENT, VIEW_COMMUNICATION_IRIS,
                EVENT_CATEGORY, EVENT_WISHLIST_PAGE,
                EVENT_ACTION, VIEW_ON_SHARING_CHANNEL,
                EVENT_LABEL, wishlistId,
                BUSINESS_UNIT, SHARING_EXPERIENCE,
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                PRODUCT_ID, productId,
                USER_ID, userId,
                WISHLIST_ID, wishlistId
            )
        )
    }
}
