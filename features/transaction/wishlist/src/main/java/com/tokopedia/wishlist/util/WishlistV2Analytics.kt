package com.tokopedia.wishlist.util

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils

object WishlistV2Analytics {
    private const val CLICK_WISHLIST = "clickWishlist"
    private const val WISHLIST_PAGE = "wishlist page"
    private const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    private const val ORDER_MANAGEMENT = "order management"
    private const val CURRENT_SITE = "currentSite"
    private const val BUSINESS_UNIT = "businessUnit"
    private const val SUBMIT_SEARCH_FROM_CARI_PRODUK = "submit search from cari produk"
    private const val CLICK_URUTKAN_FILTER_CHIPS = "click urutkan filter chips"
    private const val CLICK_OPTION_ON_URUTKAN_FILTER_CHIPS = "click option on urutkan filter chips"

    fun submitSearchFromCariProduk(keyword: String) {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, SUBMIT_SEARCH_FROM_CARI_PRODUK, keyword)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickUrutkanFilterChips() {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_URUTKAN_FILTER_CHIPS, "")
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickOptionOnUrutkanFilterChips(optionChosen: String) {
        val event = TrackAppUtils.gtmData(
                CLICK_WISHLIST,
                WISHLIST_PAGE, CLICK_OPTION_ON_URUTKAN_FILTER_CHIPS, optionChosen)
        event[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        event[BUSINESS_UNIT] = ORDER_MANAGEMENT

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }
}