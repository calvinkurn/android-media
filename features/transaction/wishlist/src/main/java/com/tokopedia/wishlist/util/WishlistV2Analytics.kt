package com.tokopedia.wishlist.util

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.wishlist.data.model.response.WishlistV2Response
import java.util.HashMap

object WishlistV2Analytics {
    private const val EVENT = "event"
    private const val EVENT_CATEGORY = "eventCategory"
    private const val EVENT_ACTION = "eventAction"
    private const val EVENT_LABEL = "eventLabel"
    private const val SELECT_CONTENT = "select_content"
    private const val ADD_TO_CART = "add_to_cart"
    private const val VIEW_ITEM_LIST = "view_item_list"
    private const val PRODUCT_VIEW = "productView"
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
    private const val SUBMIT_SEARCH_FROM_CARI_PRODUK = "submit search from cari produk"
    private const val VIEW_PRODUCT_CARD_ON_WISHLIST_PAGE = "view product card on wishlist page"
    private const val CLICK_URUTKAN_FILTER_CHIPS = "click urutkan filter chips"
    private const val CLICK_OPTION_ON_URUTKAN_FILTER_CHIPS = "click option on urutkan filter chips"
    private const val CLICK_KATEGORI_FILTER_CHIPS = "click kategori filter chips"
    private const val CLICK_OPTION_ON_KATEGORI_FILTER_CHIPS = "click option on kategori filter chips"
    private const val CLICK_PENAWARAN_FILTER_CHIPS = "click penawaran filter chips"
    private const val CLICK_STOK_FILTER_CHIPS = "click stok filter chips"
    private const val CLICK_OPTION_ON_STOK_FILTER_CHIPS = "click option on stok filter chips"
    private const val CLICK_X_CHIPS_TO_CLEAR_FILTER = "click x chips to clear filter"
    private const val CLICK_LAYOUT_SETTINGS = "click layout settings"
    private const val CLICK_ATUR_ON_WISHLIST_PAGE = "click atur on wishlist page"
    private const val CLICK_HAPUS_ON_POP_UP_MULTIPLE_WISHLIST_PRODUCT = "click hapus on pop up multiple wishlist product"
    private const val CLICK_BATAL_ON_POP_UP_MULTIPLE_WISHLIST_PRODUCT = "click batal on pop up multiple wishlist product"
    private const val CLICK_PRODUCT_CARD_ON_WISHLIST_PAGE = "click product card on wishlist page"
    private const val CLICK_ATC_ON_WISHLIST_PAGE = "click add to cart on wishlist page"
    private const val CLICK_LIHAT_ON_ATC_SUCCESS_TOASTER = "click lihat on atc success toaster"
    private const val CLICK_LIHAT_BARANG_SERUPA_ON_PRODUCT_CARD = "click lihat barang serupa on product card"
    private const val CLICK_THREE_DOTS_ON_PRODUCT_CARD = "click three dots on product card"
    private const val CLICK_OPTION_ON_THREE_DOT_MENU = "click option on three dot menu"
    private const val CLICK_CARI_BARANG_ON_EMPTY_STATE_NO_ITEMS = "click cari barang on empty state no items"
    private const val CLICK_CARI_DI_TOKOPEDIA_ON_EMPTY_STATE_NO_SEARCH_RESULT = "click cari di tokopedia on empty state no search result"
    private const val CLICK_RESET_FILTER_ON_EMPTY_STATE_NO_FILTER_RESULT = "click reset filter on empty state no filter result"

    fun submitSearchFromCariProduk(keyword: String) {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, SUBMIT_SEARCH_FROM_CARI_PRODUK, keyword)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickUrutkanFilterChips() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_URUTKAN_FILTER_CHIPS, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickOptionOnUrutkanFilterChips(optionChosen: String) {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_OPTION_ON_URUTKAN_FILTER_CHIPS, optionChosen)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickKategoriFilterChips() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_KATEGORI_FILTER_CHIPS, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickOptionOnKategoriFilterChips(optionChosen: String) {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_OPTION_ON_KATEGORI_FILTER_CHIPS, optionChosen)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickPenawaranFilterChips() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_PENAWARAN_FILTER_CHIPS, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickSimpanOnPenawaranFilterChips(optionChosen: String) {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_OPTION_ON_KATEGORI_FILTER_CHIPS, optionChosen)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickStokFilterChips() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_STOK_FILTER_CHIPS, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickOptionOnStokFilterChips(optionChosen: String) {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_OPTION_ON_STOK_FILTER_CHIPS, optionChosen)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickXChipsToClearFilter() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_X_CHIPS_TO_CLEAR_FILTER, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickLayoutSettings(typeLayout: String) {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_LAYOUT_SETTINGS, typeLayout)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickAturOnWishlist() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_ATUR_ON_WISHLIST_PAGE, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickHapusOnPopUpMultipleWishlistProduct() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_HAPUS_ON_POP_UP_MULTIPLE_WISHLIST_PRODUCT, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickBatalOnPopUpMultipleWishlistProduct() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_BATAL_ON_POP_UP_MULTIPLE_WISHLIST_PRODUCT, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickProductCard(wishlistItem: WishlistV2Response.Data.WishlistV2.Item, userId: String, position: Int) {
        val arrayWishlistItems = arrayListOf<Bundle>()
        val bundleProduct = Bundle().apply {
            putString(DIMENSION_38, "")
            putString(DIMENSION_40, WISHLIST)
            putString(DIMENSION_79, wishlistItem.shop.id)
            putString(DIMENSION_83, "")
            putString(INDEX, position.toString())
            putString(ITEM_BRAND, "")
            putString(ITEM_NAME, wishlistItem.name)
            putString(ITEM_ID,wishlistItem.id)
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

    fun viewProductCard(trackingQueue: TrackingQueue, wishlistItem: WishlistV2Response.Data.WishlistV2.Item, userId: String, position: String) {
        val map = DataLayer.mapOf(
                EVENT, PRODUCT_VIEW,
                EVENT_CATEGORY, WISHLIST_PAGE,
                EVENT_ACTION, VIEW_PRODUCT_CARD_ON_WISHLIST_PAGE,
                EVENT_LABEL, "${wishlistItem.id} - ${if (wishlistItem.available) "available" else "unavailable"}",
                CURRENT_SITE, TOKOPEDIA_MARKETPLACE,
                USER_ID, userId,
                BUSINESS_UNIT, PURCHASE_PLATFORM,
                ECOMMERCE, DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS, convertOrderItemToDataImpressionObject(wishlistItem, position)))
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    private fun convertOrderItemToDataImpressionObject(wishlistItem: WishlistV2Response.Data.WishlistV2.Item, position: String): List<Any> {
        return listOf(DataLayer.mapOf(
                ITEM_NAME, wishlistItem.name,
                ITEM_ID, wishlistItem.id,
                PRICE, wishlistItem.price,
                ITEM_BRAND, "",
                ITEM_VARIANT, "",
                ITEM_CATEGORY, "",
                INDEX, position
        ))
    }

    fun clickAtcOnWishlist(wishlistItem: WishlistV2Response.Data.WishlistV2.Item, userId: String, position: Int, cartId: String) {
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
            putString(QUANTITY, "1")
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
                WISHLIST_PAGE, CLICK_LIHAT_ON_ATC_SUCCESS_TOASTER, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickLihatBarangSerupaOnProductCard() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_LIHAT_BARANG_SERUPA_ON_PRODUCT_CARD, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickThreeDotsOnProductCard() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_THREE_DOTS_ON_PRODUCT_CARD, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickOptionOnThreeDotsMenu(optionChosen: String) {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_OPTION_ON_THREE_DOT_MENU, optionChosen)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickCariBarangOnEmptyStateNoItems() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_CARI_BARANG_ON_EMPTY_STATE_NO_ITEMS, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickCariDiTokopediaOnEmptyStateNoSearchResult() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_CARI_DI_TOKOPEDIA_ON_EMPTY_STATE_NO_SEARCH_RESULT, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickResetFilterOnEmptyStateNoFilterResult() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_RESET_FILTER_ON_EMPTY_STATE_NO_FILTER_RESULT, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = PURCHASE_PLATFORM

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }
}