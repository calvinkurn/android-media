package com.tokopedia.wishlistcollection.analytics

import android.os.Bundle
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.wishlist.data.model.WishlistV2UiModel

object WishlistCollectionAnalytics {

    private const val TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
    private const val PURCHASE_PLATFORM = "purchase platform"
    private const val SHARING_EXPERIENCE = "sharingexperience"

    private const val EVENT = "event"
    private const val EVENT_CATEGORY = "eventCategory"
    private const val EVENT_ACTION = "eventAction"
    private const val EVENT_LABEL = "eventLabel"
    private const val CURRENT_SITE = "currentSite"
    private const val BUSINESS_UNIT = "businessUnit"
    private const val IS_LOGGED_IN_STATUS = "isLoggedInStatus"
    private const val SCREEN_NAME = "screenName"
    private const val USER_ID = "userId"

    private const val SCREEN_NAME_WISHLIST_HOME_PAGE = "Wishlist Home Page"
    private const val SCREEN_NAME_ALL_WISHLIST_PAGE = "Semua Wishlist Page"
    private const val SCREEN_NAME_COLLECTION_PAGE = "Collection Page"
    private const val WISHLIST_PAGE = "wishlist page"

    // event
    private const val OPEN_SCREEN = "openScreen"
    private const val CLICK_PP = "clickPP"
    private const val CLICK_COMMUNICATION = "clickCommunication"
    private const val VIEW_COMMUNICATION_IRIS = "viewCommunicationIris"
    private const val VIEW_PP_IRIS = "viewPPIris"
    private const val SELECT_CONTENT = "select_content"
    private const val VIEW_ITEM_LIST = "view_item_list"
    private const val ADD_TO_CART = "add_to_cart"

    // event action
    private const val CREATE_NEW_COLLECTION_ON_WISHLIST_PAGE = "create new collection on wishlist page"
    private const val CLICK_BUAT_KOLEKSI_ON_CREATE_NEW_COLLECTION_BOTTOMSHEET = "click buat koleksi on create new collection bottomsheet"
    private const val CLICK_X_ON_CREATE_NEW_COLLECTION_BOTTOMSHEET = "click x on create new collection bottomsheet"
    private const val CLICK_X_ON_INTRODUCTION_SECTION = "click x on introduction section"
    private const val CLICK_THREE_DOTS_ON_COLLECTION_FOLDER = "click three dots on collection folder"
    private const val CLICK_UBAH_NAMA_KOLEKSI_ON_THREE_DOTS_BOTTOMSHEET = "click ubah nama koleksi on three dots bottomsheet"
    private const val CLICK_HAPUS_ON_COLLECTION_FOLDER = "click hapus on collection folder"
    private const val CLICK_CARI_BARANG_BUTTON_ON_EMPTY_STATE_NO_WISHLIST_ITEMS = "click cari barang button on empty state no wishlist items"
    private const val CLICK_BUAT_KOLEKSI_BUTTON_ON_EMPTY_STATE_NO_WISHLIST_ITEMS = "click buat koleksi button on empty state no wishlist items"
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
    private const val CLICK_GEAR_ICON = "click gear icon"
    private const val CLICK_OPTION_ON_GEAR_ICON = "click option on gear icon"
    private const val CLICK_SIMPAN_ON_CHANGE_COLLECTION_NAME_BOTTOMSHEET = "click simpan on change collection name bottomsheet"
    private const val CLICK_HAPUS_DARI_KOLEKSI_SAJA_ON_MULTIPLE_WISHLISTED_PRODUCTS = "click hapus dari koleksi saja on multiple wishlisted products"
    private const val CLICK_HAPUS_DARI_SEMUA_WISHLIST_ON_MULITPLE_WISHLISTED_PRODUCTS = "click hapus dari semua wishlist on mulitple wishlisted products"
    private const val CLICK_HAPUS_ON_WISHLIST_PRODUCT = "click hapus on wishlist product"
    private const val CLICK_PRODUCT_CARD_ON_WISHLIST_PAGE = "click product card on wishlist page"
    private const val CLICK_ADD_TO_CART_ON_WISHLIST_PAGE = "click add to cart on wishlist page"
    private const val CLICK_LIHAT_BUTTON_ON_ATC_SUCCESS_TOASTER = "click lihat button on atc success toaster"
    private const val CLICK_LIHAT_BARANG_SERUPA_ON_PRODUCT_CARD = "click lihat barang serupa on product card"
    private const val CLICK_THREE_DOTS_ON_PRODUCT_CARD = "click three dots on product card"
    private const val CLICK_OPTION_ON_THREE_DOT_MENU = "click option on three dot menu"
    private const val CLICK_TAMBAH_BARANG_KE_KOLEKSI_ON_EMPTY_STATE_NO_COLLECTION_ITEMS = "click tambah barang ke koleksi on empty state no collection items"
    private const val CLICK_UBAH_NAMA_KOLEKSI_BUTTON_ON_EMPTY_STATE_NO_COLLECTION_ITEMS = "click ubah nama koleksi button on empty state no collection items"
    private const val CLICK_CARI_DI_TOKOPEDIA_BUTTON_ON_EMPTY_STATE_NO_SEARCH_RESULT = "click cari di Tokopedia button on empty state no search result"
    private const val CLICK_RESET_FILTER_BUTTON_ON_EMPTY_STATE_NO_FILTER_RESULT = "click reset filter button on empty state no filter result"
    private const val CLICK_LIHAT_BUTTON_ON_MAX_QTY_TICKER_ON_WISHLIST_PAGE = "click lihat button on max qty ticker on wishlist page"
    private const val CLICK_LIHAT_BARANG_BUTTON_ON_CLEANER_BOTTOMSHEET = "click lihat barang button on cleaner bottomsheet"
    private const val CLICK_HAPUS_BUTTON_ON_CONFIRMATION_DELETION_POPUP = "click hapus button on confirmation deletion pop-up"
    private const val VIEW_MAX_QTY_TICKER_ON_WISHLIST_PAGE = "view max qty ticker on wishlist page"
    private const val CLICK_CHECK_WISHLIST = "click - check wishlist"
    private const val CLICK_COLLECTION_FOLDER = "click - collection folder"
    private const val CLICK_NEW_COLLECTION = "click - + koleksi baru"
    private const val CLICK_SHARE_BUTTON_COLLECTION = "click - share button collection"
    private const val CLICK_CLOSE_SHARE_BUTTON_COLLECTION = "click - close share button collection"
    private const val CLICK_SHARING_CHANNEL_COLLECTION = "click - sharing channel collection"
    private const val VIEW_SHARING_CHANNEL_COLLECTION = "view on sharing channel collection"

    // event category
    private const val ALL_WISHLIST_PAGE = "all wishlist page"
    private const val COLLECTION_PAGE = "collection page"
    private const val WISHLIST_PAGEALL_WISHLIST_PAGE = "wishlist pageall wishlist page"

    // custom
    private const val TRACKER_ID = "trackerId"
    private const val PAGE_SOURCE = "pageSource"
    private const val ITEM_LIST = "item_list"
    private const val ITEMS = "items"
    private const val WISHLIST_ID = "wishlistId"

    private const val CATEGORY_ID = "category_id"
    private const val DIMENSION_38 = "dimension38"
    private const val DIMENSION_40 = "dimension40"
    private const val DIMENSION_45 = "dimension45"
    private const val DIMENSION_79 = "dimension79"
    private const val DIMENSION_83 = "dimension83"
    private const val INDEX = "index"
    private const val ITEM_VARIANT = "item_variant"
    private const val ITEM_CATEGORY = "item_category"
    private const val WISHLIST_COLLECTION = "/wishlist - collection"
    private const val ITEM_NAME = "item_name"
    private const val ITEM_BRAND = "item_brand"
    private const val ITEM_ID = "item_id"
    private const val PRICE = "price"
    private const val QUANTITY = "quantity"
    private const val SHOP_ID = "shop_id"
    private const val SHOP_NAME = "shop_name"
    private const val SHOP_TYPE = "shop_type"
    private const val COLLECTION_BOTTOMSHEET = "- collection bottomsheet"
    private const val PRODUCT_ID = "productId"

    // tracker id
    private const val OPEN_WISHLIST_HOME_PAGE_TRACKER_ID = "32118"
    private const val OPEN_ALL_WISHLIST_PAGE_TRACKER_ID = "32119"
    private const val OPEN_COLLECTION_DETAIL_PAGE_TRACKER_ID = "32120"

    private const val CREATE_NEW_COLLECTION_ON_WISHLIST_PAGE_TRACKER_ID = "32121"
    private const val CLICK_BUAT_KOLEKSI_ON_CREATE_NEW_COLLECTION_BOTTOMSHEET_TRACKER_ID = "32122"
    private const val CLICK_X_ON_CREATE_NEW_COLLECTION_BOTTOMSHEET_TRACKER_ID = "32123"
    private const val CLICK_X_ON_INTRODUCTION_SECTION_TRACKER_ID = "32124"
    private const val CLICK_THREE_DOTS_ON_COLLECTION_FOLDER_TRACKER_ID = "32125"
    private const val CLICK_UBAH_NAMA_KOLEKSI_ON_THREE_DOTS_BOTTOMSHEET_TRACKER_ID = "32126"
    private const val CLICK_HAPUS_ON_COLLECTION_FOLDER_TRACKER_ID = "32127"
    private const val CLICK_CARI_BARANG_BUTTON_ON_EMPTY_STATE_NO_WISHLIST_ITEMS_TRACKER_ID = "32128"
    private const val CLICK_BUAT_KOLEKSI_BUTTON_ON_EMPTY_STATE_NO_WISHLIST_ITEMS_TRACKER_ID = "32129"
    private const val SUBMIT_SEARCH_FROM_CARI_PRODUK_TRACKER_ID = "32130"
    private const val CLICK_URUTKAN_FILTER_CHIPS_TRACKER_ID = "32131"
    private const val CLICK_OPTION_ON_URUTKAN_FILTER_CHIPS_TRACKER_ID = "32132"
    private const val CLICK_KATEGORI_FILTER_CHIPS_TRACKER_ID = "32133"
    private const val CLICK_OPTION_ON_KATEGORI_FILTER_CHIPS_TRACKER_ID = "32134"
    private const val CLICK_PENAWARAN_FILTER_CHIPS_TRACKER_ID = "32135"
    private const val CLICK_SIMPAN_ON_PENAWARAN_FILTER_CHIPS_TRACKER_ID = "32136"
    private const val CLICK_STOK_FILTER_CHIPS_TRACKER_ID = "32137"
    private const val CLICK_OPTION_ON_STOK_FILTER_CHIPS_TRACKER_ID = "32138"
    private const val CLICK_X_CHIPS_TO_CLEAR_FILTER_TRACKER_ID = "32139"
    private const val CLICK_LAYOUT_SETTINGS_TRACKER_ID = "32140"
    private const val CLICK_GEAR_ICON_TRACKER_ID = "32141"
    private const val CLICK_OPTION_ON_GEAR_ICON_TRACKER_ID = "32142"
    private const val CLICK_SIMPAN_ON_CHANGE_COLLECTION_NAME_BOTTOMSHEET_TRACKER_ID = "32143"
    private const val CLICK_HAPUS_DARI_KOLEKSI_SAJA_ON_MULTIPLE_WISHLISTED_PRODUCTS_TRACKER_ID = "32144"
    private const val CLICK_HAPUS_DARI_SEMUA_WISHLIST_ON_MULITPLE_WISHLISTED_PRODUCTS_TRACKER_ID = "32145"
    private const val CLICK_HAPUS_ON_WISHLIST_PRODUCT_TRACKER_ID = "32146"
    private const val CLICK_PRODUCT_CARD_ON_WISHLIST_PAGE_TRACKER_ID = "32147"
    private const val VIEW_PRODUCT_CARD_ON_WISHLIST_PAGE_TRACKER_ID = "32148"
    private const val CLICK_ADD_TO_CART_ON_WISHLIST_PAGE_TRACKER_ID = "32149"
    private const val CLICK_LIHAT_BUTTON_ON_ATC_SUCCESS_TOASTER_TRACKER_ID = "32150"
    private const val CLICK_LIHAT_BARANG_SERUPA_ON_PRODUCT_CARD_TRACKER_ID = "32151"
    private const val CLICK_THREE_DOTS_ON_PRODUCT_CARD_TRACKER_ID = "32152"
    private const val CLICK_OPTION_ON_THREE_DOT_MENU_TRACKER_ID = "32153"
    private const val CLICK_TAMBAH_BARANG_KE_KOLEKSI_ON_EMPTY_STATE_NO_COLLECTION_ITEMS_TRACKER_ID = "32154"
    private const val CLICK_UBAH_NAMA_KOLEKSI_BUTTON_ON_EMPTY_STATE_NO_COLLECTION_ITEMS_TRACKER_ID = "32155"
    private const val CLICK_CARI_DI_TOKOPEDIA_BUTTON_ON_EMPTY_STATE_NO_SEARCH_RESULT_TRACKER_ID = "32156"
    private const val CLICK_RESET_FILTER_BUTTON_ON_EMPTY_STATE_NO_FILTER_RESULT_TRACKER_ID = "32157"
    private const val CLICK_LIHAT_BUTTON_ON_MAX_QTY_TICKER_ON_WISHLIST_PAGE_TRACKER_ID = "32705"
    private const val CLICK_LIHAT_BARANG_BUTTON_ON_CLEANER_BOTTOMSHEET_TRACKER_ID = "32706"
    private const val CLICK_HAPUS_BUTTON_ON_CONFIRMATION_DELETION_POPUP_TRACKER_ID = "32707"
    private const val VIEW_MAX_QTY_TICKER_ON_WISHLIST_PAGE_TRACKER_ID = "32708"
    private const val CLICK_CHECK_WISHLIST_ON_ADD_BOTTOMSHEET_TRACKER_ID = "36182"
    private const val CLICK_COLLECTION_FOLDER_BOTTOMSHEET_TRACKER_ID = "36183"
    private const val CLICK_NEW_COLLECTION_BOTTOMSHEET_TRACKER_ID = "36184"
    private const val CLICK_SHARE_BUTTON_COLLECTION_TRACKER_ID = "36010"
    private const val CLICK_CLOSE_SHARE_BUTTON_COLLECTION_TRACKER_ID = "36011"
    private const val CLICK_SHARING_CHANNEL_COLLECTION_TRACKER_ID = "36012"
    private const val VIEW_ON_SHARING_CHANNEL_COLLECTION_TRACKER_ID = "36013"

    private fun wishlistCollectionTrackerBuilder(): Tracker.Builder {
        return Tracker.Builder()
            .setBusinessUnit(PURCHASE_PLATFORM)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
    }

    private fun wishlistSharingTrackerBuilder(): Tracker.Builder {
        return Tracker.Builder()
            .setBusinessUnit(SHARING_EXPERIENCE)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
    }

    fun sendWishListHomePageOpenedEvent(isLoggedInStatus: Boolean, userId: String) {
        val wishlistHomePageBundle = Bundle().apply {
            putString(EVENT, OPEN_SCREEN)
            putString(TRACKER_ID, OPEN_WISHLIST_HOME_PAGE_TRACKER_ID)
            putString(BUSINESS_UNIT, PURCHASE_PLATFORM)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(IS_LOGGED_IN_STATUS, isLoggedInStatus.toString())
            putString(SCREEN_NAME, SCREEN_NAME_WISHLIST_HOME_PAGE)
            putString(USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(OPEN_SCREEN, wishlistHomePageBundle)
    }

    fun sendAllWishListPageOpenedEvent(isLoggedInStatus: Boolean, userId: String) {
        val wishlistHomePageBundle = Bundle().apply {
            putString(EVENT, OPEN_SCREEN)
            putString(TRACKER_ID, OPEN_ALL_WISHLIST_PAGE_TRACKER_ID)
            putString(BUSINESS_UNIT, PURCHASE_PLATFORM)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(IS_LOGGED_IN_STATUS, isLoggedInStatus.toString())
            putString(SCREEN_NAME, SCREEN_NAME_ALL_WISHLIST_PAGE)
            putString(USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(OPEN_SCREEN, wishlistHomePageBundle)
    }

    fun sendWishListCollectionDetailPageOpenedEvent(isLoggedInStatus: Boolean, userId: String) {
        val wishlistHomePageBundle = Bundle().apply {
            putString(EVENT, OPEN_SCREEN)
            putString(TRACKER_ID, OPEN_COLLECTION_DETAIL_PAGE_TRACKER_ID)
            putString(BUSINESS_UNIT, PURCHASE_PLATFORM)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(IS_LOGGED_IN_STATUS, isLoggedInStatus.toString())
            putString(SCREEN_NAME, SCREEN_NAME_COLLECTION_PAGE)
            putString(USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(OPEN_SCREEN, wishlistHomePageBundle)
    }

    fun sendCreateNewCollectionOnWishlistPageEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CREATE_NEW_COLLECTION_ON_WISHLIST_PAGE)
            .setEventCategory(ALL_WISHLIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CREATE_NEW_COLLECTION_ON_WISHLIST_PAGE_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickBuatKoleksiOnCreateNewCollectionBottomsheetEvent(collectionId: String, pageSource: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_BUAT_KOLEKSI_ON_CREATE_NEW_COLLECTION_BOTTOMSHEET)
            .setEventCategory(ALL_WISHLIST_PAGE)
            .setEventLabel(collectionId)
            .setCustomProperty(TRACKER_ID, CLICK_BUAT_KOLEKSI_ON_CREATE_NEW_COLLECTION_BOTTOMSHEET_TRACKER_ID)
            .setCustomProperty(PAGE_SOURCE, pageSource)
            .build()
            .send()
    }

    fun sendClickXOnCreateNewCollectionBottomSheetEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_X_ON_CREATE_NEW_COLLECTION_BOTTOMSHEET)
            .setEventCategory(ALL_WISHLIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_X_ON_CREATE_NEW_COLLECTION_BOTTOMSHEET_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickXOnIntroductionSectionEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_X_ON_INTRODUCTION_SECTION)
            .setEventCategory(ALL_WISHLIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_X_ON_INTRODUCTION_SECTION_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickThreeDotsOnCollectionFolderEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_THREE_DOTS_ON_COLLECTION_FOLDER)
            .setEventCategory(ALL_WISHLIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_THREE_DOTS_ON_COLLECTION_FOLDER_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickUbahNamaKoleksiOnThreeDotsBottomsheetEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_UBAH_NAMA_KOLEKSI_ON_THREE_DOTS_BOTTOMSHEET)
            .setEventCategory(ALL_WISHLIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_UBAH_NAMA_KOLEKSI_ON_THREE_DOTS_BOTTOMSHEET_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickHapusOnCollectionFolderEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_HAPUS_ON_COLLECTION_FOLDER)
            .setEventCategory(ALL_WISHLIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_HAPUS_ON_COLLECTION_FOLDER_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickCariBarangButtonOnEmptyStateNoWishlistItemsEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_CARI_BARANG_BUTTON_ON_EMPTY_STATE_NO_WISHLIST_ITEMS)
            .setEventCategory(ALL_WISHLIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_CARI_BARANG_BUTTON_ON_EMPTY_STATE_NO_WISHLIST_ITEMS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickBuatKoleksiButtonOnEmptyStateNoWishlistItemsEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_BUAT_KOLEKSI_BUTTON_ON_EMPTY_STATE_NO_WISHLIST_ITEMS)
            .setEventCategory(ALL_WISHLIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_BUAT_KOLEKSI_BUTTON_ON_EMPTY_STATE_NO_WISHLIST_ITEMS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendSubmitSearchFromCariProdukEvent(keyword: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(SUBMIT_SEARCH_FROM_CARI_PRODUK)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel(keyword)
            .setCustomProperty(TRACKER_ID, SUBMIT_SEARCH_FROM_CARI_PRODUK_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickUrutkanFilterChipsEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_URUTKAN_FILTER_CHIPS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_URUTKAN_FILTER_CHIPS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickOptionOnUrutkanFilterChipsEvent(eventLabel: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_OPTION_ON_URUTKAN_FILTER_CHIPS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, CLICK_OPTION_ON_URUTKAN_FILTER_CHIPS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickKategoriFilterChipsEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_KATEGORI_FILTER_CHIPS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_KATEGORI_FILTER_CHIPS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickOptionOnKategoriFilterChipsEvent(eventLabel: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_OPTION_ON_KATEGORI_FILTER_CHIPS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, CLICK_OPTION_ON_KATEGORI_FILTER_CHIPS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickPenawaranFilterChipsEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_PENAWARAN_FILTER_CHIPS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_PENAWARAN_FILTER_CHIPS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickSimpanOnPenawaranFilterChipsEvent(eventLabel: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_SIMPAN_ON_PENAWARAN_FILTER_CHIPS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, CLICK_SIMPAN_ON_PENAWARAN_FILTER_CHIPS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickStokFilterChipsEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_STOK_FILTER_CHIPS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_STOK_FILTER_CHIPS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickOptionOnStokFilterChipsEvent(eventLabel: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_OPTION_ON_STOK_FILTER_CHIPS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, CLICK_OPTION_ON_STOK_FILTER_CHIPS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickXChipsToClearFilterEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_X_CHIPS_TO_CLEAR_FILTER)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_X_CHIPS_TO_CLEAR_FILTER_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickLayoutSettingsEvent(eventLabel: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_LAYOUT_SETTINGS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, CLICK_LAYOUT_SETTINGS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickGearIconEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_GEAR_ICON)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_GEAR_ICON_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickOptionOnGearIconEvent(optionChosen: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_OPTION_ON_GEAR_ICON)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel(optionChosen)
            .setCustomProperty(TRACKER_ID, CLICK_OPTION_ON_GEAR_ICON_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickSimpanOnChangeCollectionNameBottomsheetEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_SIMPAN_ON_CHANGE_COLLECTION_NAME_BOTTOMSHEET)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_SIMPAN_ON_CHANGE_COLLECTION_NAME_BOTTOMSHEET_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickHapusDariKoleksiSajaOnMultipleWishlistedProductsEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_HAPUS_DARI_KOLEKSI_SAJA_ON_MULTIPLE_WISHLISTED_PRODUCTS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_HAPUS_DARI_KOLEKSI_SAJA_ON_MULTIPLE_WISHLISTED_PRODUCTS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickHapusDariSemuaWishlistOnMulitpleWishlistedProductsEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_HAPUS_DARI_SEMUA_WISHLIST_ON_MULITPLE_WISHLISTED_PRODUCTS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_HAPUS_DARI_SEMUA_WISHLIST_ON_MULITPLE_WISHLISTED_PRODUCTS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickHapusOnWishlistProductEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_HAPUS_ON_WISHLIST_PRODUCT)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_HAPUS_ON_WISHLIST_PRODUCT_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickProductCardOnWishlistPageEvent(collectionId: String, wishlistItem: WishlistV2UiModel.Item, userId: String, position: Int) {
        val arrayWishlistItems = arrayListOf<Bundle>()
        val bundleProduct = Bundle().apply {
            putString(DIMENSION_38, "")
            putString(DIMENSION_40, WISHLIST_COLLECTION)
            putString(DIMENSION_79, wishlistItem.shop.id)
            putString(DIMENSION_83, "")
            putString(INDEX, position.toString())
            putString(ITEM_BRAND, "")
            putString(ITEM_NAME, wishlistItem.name)
            putString(ITEM_ID, wishlistItem.id)
            putString(ITEM_CATEGORY, "")
            putString(PRICE, wishlistItem.price)
            putString(ITEM_VARIANT, "")
        }
        arrayWishlistItems.add(bundleProduct)

        val bundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_CATEGORY, COLLECTION_PAGE)
            putString(EVENT_ACTION, CLICK_PRODUCT_CARD_ON_WISHLIST_PAGE)
            putString(EVENT_LABEL, collectionId)
            putString(TRACKER_ID, CLICK_PRODUCT_CARD_ON_WISHLIST_PAGE_TRACKER_ID)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(BUSINESS_UNIT, PURCHASE_PLATFORM)
            putString(USER_ID, userId)
            putString(WISHLIST_ID, wishlistItem.wishlistId)
            putString(ITEM_LIST, WISHLIST_COLLECTION)
            putParcelableArrayList(ITEMS, arrayWishlistItems)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    fun sendClickAddToCartOnWishlistPageEvent(collectionId: String, wishlistItem: WishlistV2UiModel.Item, userId: String, position: Int, cartId: String) {
        val arrayWishlistItems = arrayListOf<Bundle>()
        val bundleProduct = Bundle().apply {
            putString(CATEGORY_ID, "")
            putString(DIMENSION_38, "")
            putString(DIMENSION_40, WISHLIST_COLLECTION)
            putString(DIMENSION_45, cartId)
            putString(DIMENSION_79, wishlistItem.shop.id)
            putString(DIMENSION_83, "")
            putString(INDEX, position.toString())
            putString(ITEM_BRAND, "")
            putString(ITEM_NAME, wishlistItem.name)
            putString(ITEM_ID, wishlistItem.id)
            putString(ITEM_CATEGORY, "")
            putString(PRICE, wishlistItem.price)
            putString(ITEM_VARIANT, "")
            putString(QUANTITY, wishlistItem.minOrder)
            putString(SHOP_ID, wishlistItem.shop.id)
            putString(SHOP_NAME, wishlistItem.shop.name)
            putString(SHOP_TYPE, "")
        }
        arrayWishlistItems.add(bundleProduct)

        val bundle = Bundle().apply {
            putString(EVENT, ADD_TO_CART)
            putString(EVENT_CATEGORY, COLLECTION_PAGE)
            putString(EVENT_ACTION, CLICK_ADD_TO_CART_ON_WISHLIST_PAGE)
            putString(EVENT_LABEL, collectionId)
            putString(TRACKER_ID, CLICK_ADD_TO_CART_ON_WISHLIST_PAGE_TRACKER_ID)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(BUSINESS_UNIT, PURCHASE_PLATFORM)
            putString(USER_ID, userId)
            putString(WISHLIST_ID, wishlistItem.wishlistId)
            putParcelableArrayList(ITEMS, arrayWishlistItems)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ADD_TO_CART, bundle)
    }

    fun sendClickLihatButtonOnAtcSuccessToasterEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_LIHAT_BUTTON_ON_ATC_SUCCESS_TOASTER)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_LIHAT_BUTTON_ON_ATC_SUCCESS_TOASTER_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickLihatBarangSerupaOnProductCardEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_LIHAT_BARANG_SERUPA_ON_PRODUCT_CARD)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_LIHAT_BARANG_SERUPA_ON_PRODUCT_CARD_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickThreeDotsOnProductCardEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_THREE_DOTS_ON_PRODUCT_CARD)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_THREE_DOTS_ON_PRODUCT_CARD_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickOptionOnThreeDotMenuEvent(eventLabel: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_OPTION_ON_THREE_DOT_MENU)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, CLICK_OPTION_ON_THREE_DOT_MENU_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickTambahBarangKeKoleksiOnEmptyStateNoCollectionItemsEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_TAMBAH_BARANG_KE_KOLEKSI_ON_EMPTY_STATE_NO_COLLECTION_ITEMS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_TAMBAH_BARANG_KE_KOLEKSI_ON_EMPTY_STATE_NO_COLLECTION_ITEMS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickUbahNamaKoleksiButtonOnEmptyStateNoCollectionItemsEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_UBAH_NAMA_KOLEKSI_BUTTON_ON_EMPTY_STATE_NO_COLLECTION_ITEMS)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_UBAH_NAMA_KOLEKSI_BUTTON_ON_EMPTY_STATE_NO_COLLECTION_ITEMS_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickCariDiTokopediaButtonOnEmptyStateNoSearchResultEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_CARI_DI_TOKOPEDIA_BUTTON_ON_EMPTY_STATE_NO_SEARCH_RESULT)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_CARI_DI_TOKOPEDIA_BUTTON_ON_EMPTY_STATE_NO_SEARCH_RESULT_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickResetFilterButtonOnEmptyStateNoFilterResultEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_RESET_FILTER_BUTTON_ON_EMPTY_STATE_NO_FILTER_RESULT)
            .setEventCategory(COLLECTION_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_RESET_FILTER_BUTTON_ON_EMPTY_STATE_NO_FILTER_RESULT_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickLihatButtonOnMaxQtyTickerOnWishlistPageEvent(isErrorTicker: Boolean) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_LIHAT_BUTTON_ON_MAX_QTY_TICKER_ON_WISHLIST_PAGE)
            .setEventCategory(ALL_WISHLIST_PAGE)
            .setEventLabel("${if (isErrorTicker) "red" else "yellow"} ticker")
            .setCustomProperty(TRACKER_ID, CLICK_LIHAT_BUTTON_ON_MAX_QTY_TICKER_ON_WISHLIST_PAGE_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickLihatBarangButtonOnCleanerBottomsheetEvent(selectedCleanOption: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_LIHAT_BARANG_BUTTON_ON_CLEANER_BOTTOMSHEET)
            .setEventCategory(WISHLIST_PAGEALL_WISHLIST_PAGE)
            .setEventLabel("$selectedCleanOption option")
            .setCustomProperty(TRACKER_ID, CLICK_LIHAT_BARANG_BUTTON_ON_CLEANER_BOTTOMSHEET_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickHapusButtonOnConfirmationDeletionPopupEvent() {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_HAPUS_BUTTON_ON_CONFIRMATION_DELETION_POPUP)
            .setEventCategory(WISHLIST_PAGEALL_WISHLIST_PAGE)
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_HAPUS_BUTTON_ON_CONFIRMATION_DELETION_POPUP_TRACKER_ID)
            .build()
            .send()
    }

    fun sendViewMaxQtyTickerOnWishlistPageEvent(isErrorTicker: Boolean) {
        wishlistCollectionTrackerBuilder()
            .setEvent(VIEW_PP_IRIS)
            .setEventAction(VIEW_MAX_QTY_TICKER_ON_WISHLIST_PAGE)
            .setEventCategory(ALL_WISHLIST_PAGE)
            .setEventLabel("${if (isErrorTicker) "red" else "yellow"} ticker")
            .setCustomProperty(TRACKER_ID, VIEW_MAX_QTY_TICKER_ON_WISHLIST_PAGE_TRACKER_ID)
            .build()
            .send()
    }

    fun sendClickCheckWishlistEvent(productId: String, source: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_CHECK_WISHLIST)
            .setEventCategory("$source $COLLECTION_BOTTOMSHEET")
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_CHECK_WISHLIST_ON_ADD_BOTTOMSHEET_TRACKER_ID)
            .setCustomProperty(PRODUCT_ID, productId)
            .build()
            .send()
    }

    fun sendClickCollectionFolderEvent(collectionId: String, productId: String, source: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_COLLECTION_FOLDER)
            .setEventCategory("$source $COLLECTION_BOTTOMSHEET")
            .setEventLabel(collectionId)
            .setCustomProperty(TRACKER_ID, CLICK_COLLECTION_FOLDER_BOTTOMSHEET_TRACKER_ID)
            .setCustomProperty(PRODUCT_ID, productId)
            .build()
            .send()
    }

    fun sendClickKoleksiBaruEvent(productId: String, source: String) {
        wishlistCollectionTrackerBuilder()
            .setEvent(CLICK_PP)
            .setEventAction(CLICK_NEW_COLLECTION)
            .setEventCategory("$source $COLLECTION_BOTTOMSHEET")
            .setEventLabel("")
            .setCustomProperty(TRACKER_ID, CLICK_NEW_COLLECTION_BOTTOMSHEET_TRACKER_ID)
            .setCustomProperty(PRODUCT_ID, productId)
            .build()
            .send()
    }

    fun sendClickShareButtonCollectionEvent(collectionId: String, typeCollection: String, userId: String) {
        wishlistSharingTrackerBuilder()
            .setEvent(CLICK_COMMUNICATION)
            .setEventAction(CLICK_SHARE_BUTTON_COLLECTION)
            .setEventCategory(WISHLIST_PAGE)
            .setEventLabel("$collectionId - $typeCollection")
            .setCustomProperty(TRACKER_ID, CLICK_SHARE_BUTTON_COLLECTION_TRACKER_ID)
            .setCustomProperty(PRODUCT_ID, "")
            .setUserId(userId)
            .setCustomProperty("wishlistId", "")
            .build()
            .send()
    }

    fun sendClickCloseShareButtonCollectionEvent(collectionId: Long, userId: String) {
        wishlistSharingTrackerBuilder()
            .setEvent(CLICK_COMMUNICATION)
            .setEventAction(CLICK_CLOSE_SHARE_BUTTON_COLLECTION)
            .setEventCategory(WISHLIST_PAGE)
            .setEventLabel("$collectionId")
            .setCustomProperty(TRACKER_ID, CLICK_CLOSE_SHARE_BUTTON_COLLECTION_TRACKER_ID)
            .setCustomProperty(PRODUCT_ID, "")
            .setUserId(userId)
            .setCustomProperty(WISHLIST_ID, "")
            .build()
            .send()
    }

    fun sendClickSharingChannelCollectionEvent(channelName: String, collectionId: Long, userId: String) {
        wishlistSharingTrackerBuilder()
            .setEvent(CLICK_COMMUNICATION)
            .setEventAction(CLICK_SHARING_CHANNEL_COLLECTION)
            .setEventCategory(WISHLIST_PAGE)
            .setEventLabel("$channelName - $collectionId")
            .setCustomProperty(TRACKER_ID, CLICK_SHARING_CHANNEL_COLLECTION_TRACKER_ID)
            .setCustomProperty(PRODUCT_ID, "")
            .setUserId(userId)
            .setCustomProperty(WISHLIST_ID, "")
            .build()
            .send()
    }

    fun sendViewOnSharingChannelCollectionEvent(collectionId: Long, userId: String) {
        wishlistSharingTrackerBuilder()
            .setEvent(VIEW_COMMUNICATION_IRIS)
            .setEventAction(VIEW_SHARING_CHANNEL_COLLECTION)
            .setEventCategory(WISHLIST_PAGE)
            .setEventLabel("$collectionId")
            .setCustomProperty(TRACKER_ID, VIEW_ON_SHARING_CHANNEL_COLLECTION_TRACKER_ID)
            .setCustomProperty(PRODUCT_ID, "")
            .setUserId(userId)
            .setCustomProperty(WISHLIST_ID, "")
            .build()
            .send()
    }
}
