package com.tokopedia.minicart.common.analytics

import android.os.Bundle
import com.tokopedia.minicart.cartlist.uimodel.MiniCartProductUiModel
import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class MiniCartAnalytics @Inject constructor(val userSession: UserSessionInterface) {

    enum class Page {
        HOME_PAGE, SEARCH_PAGE, CATEGORY_PAGE, DISCOVERY_PAGE, RECOMMENDATION_INFINITE, MVC_PAGE
    }

    companion object {
        const val AB_TEST_BUY = "beli"
        const val AB_TEST_DIRECT_BUY = "beli langsung"

        // HOST PAGE
        const val TOKONOW_HOME_PAGE = "landing"
        const val SEARCH_PAGE = "search"
        const val CATEGORY_PAGE = "category"

        // EXTRA KEY
        const val KEY_BUSINESS_UNIT = "businessUnit"
        const val KEY_CURRENT_SITE = "currentSite"
        const val KEY_USER_ID = "userId"
        const val KEY_CHECKOUT_OPTION = "checkout_option"
        const val KEY_CHECKOUT_STEP = "checkout step"
        const val KEY_ITEMS = "items"
        const val KEY_SHOP_ID = "shopId"
        const val KEY_PROMO_CODE = "promoCode"
        const val KEY_PAGE_SOURCE = "pageSource"

        // EXTRA KEY'S VALUE
        const val VALUE_BUSINESS_UNIT_PURCHASE_PLATFORM = "purchase platform"
        const val VALUE_BUSINESS_UNIT_HOME_AND_BROWSE = "home & browse"
        const val VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE = "tokopediamarketplace"
        const val VALUE_CHECKOUT_OPTION_CLICK_BUY_IN_MINICART = "click beli in minicart"
        const val VALUE_CHECKOUT_OPTION_VIEW_MINI_CART_PAGE = "view minicart page"
        const val VALUE_CHECKOUT_STEP_ZERO = "0"
        const val VALUE_CHECKOUT_STEP_ONE = "1"
        const val VALUE_PROMO_CODE_FULFILLED = "fulfilled"
        const val VALUE_PROMO_CODE_NOT_FULFILLED = "not_fulfilled"
        const val VALUE_PAGE_SOURCE_MVC_PAGE = "mvcpage"

        // EVENT NAME
        const val EVENT_NAME_CLICK_MINICART = "clickMinicart"
        const val EVENT_NAME_VIEW_MINICART_IRIS = "viewMinicartIris"
        const val EVENT_NAME_BEGIN_CHECKOUT = "begin_checkout"
        const val EVENT_NAME_CHECKOUT_PROGRESS = "checkout_progress"
        const val EVENT_NAME_CHECKOUT = "checkout"
        const val EVENT_NAME_CLICK_PG = "clickPG"
        const val EVENT_NAME_VIEW_PG_IRIS = "viewPGIris"

        // EVENT CATEGORY
        const val EVENT_CATEGORY_MINICART = "minicart"
        const val EVENT_CATEGORY_CLICK_BUY = "tokonow %s"
        const val EVENT_CATEGORY_SHOP_PAGE_BUYER = "shop page - buyer"

        // EVENT ACTION
        const val EVENT_ACTION_CLICK_PRODUCT_NAME = "click product name"
        const val EVENT_ACTION_CLICK_BUTTON_PLUS = "click button +"
        const val EVENT_ACTION_CLICK_BUTTON_MINUS = "click button -"
        const val EVENT_ACTION_CLICK_INPUT_QUANTITY = "click input quantity"
        const val EVENT_ACTION_CLICK_DELETE_FROM_TRASH_BIN = "click hapus from trash bin"
        const val EVENT_ACTION_CLICK_UNDO_AFTER_DELETE_PRODUCT = "click undo after delete product"
        const val EVENT_ACTION_CLICK_WRITE_NOTES = "click tulis catatan"
        const val EVENT_ACTION_CLICK_CHANGE_NOTES = "click ubah catatan"
        const val EVENT_ACTION_CLICK_CHAT_ON_MINICART = "click chat on minicart"
        const val EVENT_ACTION_CLICK_BUY = "click %s on minicart in %s page"
        const val EVENT_ACTION_CLICK_CHEVRON_TO_SHOW_BOTTOMSHEET = "click arrow icon on minicart bottomsheet"
        const val EVENT_ACTION_CLICK_CHEVRON_TO_SHOW_SUMMARY_TRANSACTION = "click arrow icon in total price"
        const val EVENT_VIEW_ERROR_TICKER_IN_MINICART = "view error ticker in minicart"
        const val EVENT_CLICK_DELETE_ALL_UNAVAILABLE_PRODUCT = "click delete all unavailable product"
        const val EVENT_CLICK_SEE_SIMILAR_PRODUCT_ON_UNAVAILABLE_SECTION = "click lihat produk serupa on unavailable section"
        const val EVENT_CLICK_BUY_THEN_GET_TOASTER_ERROR = "click %s - toaster not success"
        const val EVENT_CLICK_BUY_THEN_GET_BOTTOM_SHEET_ERROR = "click %s - bottomsheet not success"
        const val EVENT_CLICK_BUTTON_ON_ERROR_TOASTER = "click button on error toaster"
        const val EVENT_VIEW_MINI_CART_WITH_UNAVAILABLE_PRODUCT = "view minicart with unavailable product"
        const val EVENT_ACTION_VIEW_MINI_CART_PAGE = "view minicart page"
        const val EVENT_ACTION_CLICK_KNOB_MINI_CART_BOTTOM_SHEET = "click knob action"
        const val EVENT_ACTION_CLICK_DIRECT_CHAT_ON_BOTTOM_SHEET = "click langsung chat on minicart chat attachment"
        const val EVENT_ACTION_CLICK_PRODUCT_CARD_TICK_BOX_CHAT_ON_BOTTOM_SHEET = "click product tickbox on minicart chat attachment"
        const val EVENT_ACTION_CLICK_ASK_PRODUCT_CHAT_ON_BOTTOM_SHEET = "click tanya soal produk on minicart chat attachment"
        const val EVENT_ACTION_CLICK_CHECK_CART = "click check cart"
        const val EVENT_ACTION_MVC_PROGRESS_BAR_IMPRESSION = "mvc progress bar impression"

        // EVENT LABEL
        const val EVENT_LABEL_SUCCESS = "success"
        const val EVENT_LABEL_TICK = "tick"
        const val EVENT_LABEL_UNTICK = "untick"
        const val EVENT_LABEL_MINICART  = "minicart"

        // EE CUSTOM DIMENSION
        const val DIMENSION_104 = "dimension104" // Campaign id
        const val DIMENSION_38 = "dimension38" // Attribution
        const val DIMENSION_45 = "dimension45" // Cart id
        const val DIMENSION_48 = "dimension48" // Product weight
        const val DIMENSION_49 = "dimension49" // Promo code
        const val DIMENSION_53 = "dimension53" // Discounted price
        const val DIMENSION_56 = "dimension56" // Warehouse id
        const val DIMENSION_59 = "dimension59" // Promo details
        const val DIMENSION_79 = "dimension79" // Shop id
        const val DIMENSION_80 = "dimension80" // Shop name
        const val DIMENSION_81 = "dimension81" // Shop type
        const val DIMENSION_82 = "dimension82" // Category id
        const val DIMENSION_83 = "dimension83" // Product type
        const val ITEM_BRAND = "item_brand"
        const val ITEM_CATEGORY = "item_category"
        const val ITEM_ID = "item_id"
        const val ITEM_NAME = "item_name"
        const val ITEM_VARIANT = "item_variant"
        const val PRICE = "price"
        const val SHOP_ID = "shop_id"
        const val QUANTITY = "quantity"
        const val SHOP_NAME = "shop_name"
        const val SHOP_TYPE = "shop_type"
    }

    fun String.toEnhancedEcommerceDefaultValueIfEmpty(): String {
        return if (this.isBlank()) "none/other" else this
    }

    private fun sendGeneralEvent(value: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(value)
    }

    private fun sendEnhanceEcommerceEvent(eventName: String, bundle: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, bundle)
    }

    private fun getGtmData(eventName: String,
                           eventAction: String,
                           eventLabel: String = ""
    ): Map<String, Any> {
        val trackingData = TrackAppUtils.gtmData(eventName, EVENT_CATEGORY_MINICART, eventAction, eventLabel)
        trackingData[KEY_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT_PURCHASE_PLATFORM
        trackingData[KEY_CURRENT_SITE] = VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE

        return trackingData
    }

    private fun getBundleProduct(product: MiniCartProductUiModel): Bundle {
        return Bundle().apply {
            putString(DIMENSION_104, product.campaignId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_38, product.attribution.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_45, product.cartId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_48, product.productWeight.toString().toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_49, "".toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_53, product.productSlashPriceLabel.isNotEmpty().toString().toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_56, product.warehouseId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_59, "".toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_79, product.shopId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_80, product.shopName.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_81, product.shopType.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_82, product.categoryId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_83, product.freeShippingType.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(ITEM_BRAND, "".toEnhancedEcommerceDefaultValueIfEmpty())
            putString(ITEM_CATEGORY, product.category.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(ITEM_ID, product.productId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(ITEM_NAME, product.productName.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(ITEM_VARIANT, product.productVariantName.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(PRICE, product.productPrice.toString().toEnhancedEcommerceDefaultValueIfEmpty())
            putString(SHOP_ID, product.shopId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(QUANTITY, product.productQty.toString().toEnhancedEcommerceDefaultValueIfEmpty())
            putString(SHOP_NAME, product.shopName.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(SHOP_TYPE, product.shopType.toEnhancedEcommerceDefaultValueIfEmpty())
        }
    }

    private fun getBundleProduct(product: MiniCartItem): Bundle {
        return Bundle().apply {
            putString(DIMENSION_104, product.campaignId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_38, product.attribution.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_45, product.cartId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_48, product.productWeight.toString().toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_49, "".toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_53, product.productSlashPriceLabel.isNotEmpty().toString().toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_56, product.warehouseId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_59, "".toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_79, product.shopId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_80, product.shopName.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_81, product.shopType.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_82, product.categoryId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(DIMENSION_83, product.freeShippingType.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(ITEM_BRAND, "".toEnhancedEcommerceDefaultValueIfEmpty())
            putString(ITEM_CATEGORY, product.category.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(ITEM_ID, product.productId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(ITEM_NAME, product.productName.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(ITEM_VARIANT, product.productVariantName.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(PRICE, product.productPrice.toString().toEnhancedEcommerceDefaultValueIfEmpty())
            putString(SHOP_ID, product.shopId.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(QUANTITY, product.quantity.toString().toEnhancedEcommerceDefaultValueIfEmpty())
            putString(SHOP_NAME, product.shopName.toEnhancedEcommerceDefaultValueIfEmpty())
            putString(SHOP_TYPE, product.shopType.toEnhancedEcommerceDefaultValueIfEmpty())
        }
    }

    // 1 - DONE
    fun eventClickProductName(productId: String) {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_PRODUCT_NAME,
                eventLabel = productId
        )

        sendGeneralEvent(data)
    }

    // 2 - DONE
    fun eventClickQuantityPlus() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_BUTTON_PLUS
        )

        sendGeneralEvent(data)
    }

    // 3 - DONE
    fun eventClickQuantityMinus() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_BUTTON_MINUS
        )

        sendGeneralEvent(data)
    }

    // 4 - DONE
    fun eventClickInputQuantity(quantity: Int) {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_INPUT_QUANTITY,
                eventLabel = quantity.toString()
        )

        sendGeneralEvent(data)
    }

    // 5 - DONE
    fun eventClickDeleteFromTrashBin() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_DELETE_FROM_TRASH_BIN
        )

        sendGeneralEvent(data)
    }

    // 6 - DONE
    fun eventClickUndoDelete() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_UNDO_AFTER_DELETE_PRODUCT
        )

        sendGeneralEvent(data)
    }

    // 7 - DONE
    fun eventClickWriteNotes() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_WRITE_NOTES
        )

        sendGeneralEvent(data)
    }

    // 8 - DONE
    fun eventClickChangeNotes() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_CHANGE_NOTES
        )

        sendGeneralEvent(data)
    }

    // 9 - DONE
    fun eventClickChatOnMiniCart() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_CHAT_ON_MINICART
        )

        sendGeneralEvent(data)
    }

    // 10, 11, 12 Bottom Sheet - DONE
    @JvmName("eventClickBuyFromBottomSheet")
    fun eventClickBuy(page: Page, products: List<MiniCartProductUiModel>, isOCCFlow: Boolean) {
        var eventAction = ""
        var eventCategory = ""
        when (page) {
            Page.HOME_PAGE -> {
                eventAction = String.format(EVENT_ACTION_CLICK_BUY, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY, "landing")
                eventCategory = String.format(EVENT_CATEGORY_CLICK_BUY, "- homepage")
            }
            Page.SEARCH_PAGE -> {
                eventAction = String.format(EVENT_ACTION_CLICK_BUY, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY, "search")
                eventCategory = String.format(EVENT_CATEGORY_CLICK_BUY, "- search result")
            }
            Page.CATEGORY_PAGE -> {
                eventAction = String.format(EVENT_ACTION_CLICK_BUY, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY, "category")
                eventCategory = String.format(EVENT_CATEGORY_CLICK_BUY, "category page")
            }
            Page.DISCOVERY_PAGE -> {
                eventAction = String.format(EVENT_ACTION_CLICK_BUY, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY, "discovery")
                eventCategory = "discovery page"
            }
            Page.RECOMMENDATION_INFINITE -> {
                eventAction = String.format(EVENT_ACTION_CLICK_BUY, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY, "recommendation")
                eventCategory = String.format(EVENT_CATEGORY_CLICK_BUY, "recommendation infinite page")
            }
        }

        val dataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, if (isOCCFlow) EVENT_NAME_CHECKOUT else EVENT_NAME_BEGIN_CHECKOUT)
            putString(TrackAppUtils.EVENT_CATEGORY, eventCategory)
            putString(TrackAppUtils.EVENT_ACTION, eventAction)
            putString(TrackAppUtils.EVENT_LABEL, getLabelForBuyEvent(page))
            putString(KEY_BUSINESS_UNIT, getBusinessUnitForBuyEvent(page))
            putString(KEY_CURRENT_SITE, VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            putString(KEY_USER_ID, userSession.userId)
            putString(KEY_CHECKOUT_OPTION, VALUE_CHECKOUT_OPTION_CLICK_BUY_IN_MINICART)
            putString(KEY_CHECKOUT_STEP, VALUE_CHECKOUT_STEP_ONE)
            val items = ArrayList<Bundle>()
            products.forEach { product ->
                val bundle = getBundleProduct(product)
                items.add(bundle)
            }
            putParcelableArrayList(KEY_ITEMS, items)
        }

        sendEnhanceEcommerceEvent(
                eventName = EVENT_NAME_BEGIN_CHECKOUT,
                bundle = dataLayer
        )
    }

    // 10, 11, 12 Widget - DONE
    @JvmName("eventClickBuyFromWidget")
    fun eventClickBuy(page: Page, products: List<MiniCartItem>, isOCCFlow: Boolean) {
        var eventAction = ""
        var eventCategory = ""
        when (page) {
            Page.HOME_PAGE -> {
                eventAction = String.format(EVENT_ACTION_CLICK_BUY, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY, "landing")
                eventCategory = String.format(EVENT_CATEGORY_CLICK_BUY, "- homepage")
            }
            Page.SEARCH_PAGE -> {
                eventAction = String.format(EVENT_ACTION_CLICK_BUY, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY, "search")
                eventCategory = String.format(EVENT_CATEGORY_CLICK_BUY, "- search result")
            }
            Page.CATEGORY_PAGE -> {
                eventAction = String.format(EVENT_ACTION_CLICK_BUY, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY, "category")
                eventCategory = String.format(EVENT_CATEGORY_CLICK_BUY, "category page")
            }
            Page.DISCOVERY_PAGE -> {
                eventAction = String.format(EVENT_ACTION_CLICK_BUY, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY, "discovery")
                eventCategory = "discovery page"
            }
            Page.RECOMMENDATION_INFINITE -> {
                eventAction = String.format(EVENT_ACTION_CLICK_BUY, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY, "recommendation")
                eventCategory = String.format(EVENT_CATEGORY_CLICK_BUY, "recommendation infinite page")
            }
        }

        val dataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, if (isOCCFlow) EVENT_NAME_CHECKOUT else EVENT_NAME_BEGIN_CHECKOUT)
            putString(TrackAppUtils.EVENT_CATEGORY, eventCategory)
            putString(TrackAppUtils.EVENT_ACTION, eventAction)
            putString(TrackAppUtils.EVENT_LABEL, getLabelForBuyEvent(page))
            putString(KEY_BUSINESS_UNIT, getBusinessUnitForBuyEvent(page))
            putString(KEY_CURRENT_SITE, VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            putString(KEY_USER_ID, userSession.userId)
            putString(KEY_CHECKOUT_OPTION, VALUE_CHECKOUT_OPTION_CLICK_BUY_IN_MINICART)
            putString(KEY_CHECKOUT_STEP, VALUE_CHECKOUT_STEP_ONE)
            val items = ArrayList<Bundle>()
            products.forEach { product ->
                val bundle = getBundleProduct(product)
                items.add(bundle)
            }
            putParcelableArrayList(KEY_ITEMS, items)
        }

        sendEnhanceEcommerceEvent(
                eventName = EVENT_NAME_BEGIN_CHECKOUT,
                bundle = dataLayer
        )
    }

    fun getLabelForBuyEvent(page:Page):String{
        return when(page){
            Page.DISCOVERY_PAGE-> EVENT_LABEL_MINICART
            else->EVENT_LABEL_SUCCESS
        }
    }

    fun getBusinessUnitForBuyEvent(page:Page):String{
        return when(page){
            Page.DISCOVERY_PAGE-> VALUE_BUSINESS_UNIT_HOME_AND_BROWSE
            else->VALUE_BUSINESS_UNIT_PURCHASE_PLATFORM
        }
    }

    // 13 - DONE
    fun eventClickChevronToShowMiniCartBottomSheet() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_CHEVRON_TO_SHOW_BOTTOMSHEET
        )

        sendGeneralEvent(data)
    }

    // 14 - DONE
    fun eventClickChevronToShowSummaryTransaction() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_CHEVRON_TO_SHOW_SUMMARY_TRANSACTION
        )

        sendGeneralEvent(data)
    }

    // 15 - DONE
    fun eventViewErrorTickerOverweightInMiniCart(errorMessage: String) {
        val data = getGtmData(
                eventName = EVENT_NAME_VIEW_MINICART_IRIS,
                eventAction = EVENT_VIEW_ERROR_TICKER_IN_MINICART,
                eventLabel = errorMessage
        )

        sendGeneralEvent(data)
    }

    // 16 - DONE
    fun eventClickDeleteAllUnavailableProduct() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_CLICK_DELETE_ALL_UNAVAILABLE_PRODUCT
        )

        sendGeneralEvent(data)
    }

    // 17 - DONE
    fun eventClickSeeSimilarProductOnUnavailableSection(productId: String, errorType: String) {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_CLICK_SEE_SIMILAR_PRODUCT_ON_UNAVAILABLE_SECTION,
                eventLabel = "$productId - $errorType"
        )

        sendGeneralEvent(data)
    }

    // 18 - DONE
    fun eventClickBuyThenGetToasterError(errorMessage: String, isOCCFlow: Boolean) {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = String.format(EVENT_CLICK_BUY_THEN_GET_TOASTER_ERROR, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY),
                eventLabel = errorMessage
        )

        sendGeneralEvent(data)
    }

    // 19 - DONE
    fun eventClickAtcToasterErrorCta(errorMessage: String, ctaWording: String) {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_CLICK_BUTTON_ON_ERROR_TOASTER,
                eventLabel = "$errorMessage - $ctaWording"
        )

        sendGeneralEvent(data)
    }


    // 20 - DONE
    fun eventClickBuyThenGetBottomSheetError(errorMessage: String, isOCCFlow: Boolean) {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = String.format(EVENT_CLICK_BUY_THEN_GET_BOTTOM_SHEET_ERROR, if (isOCCFlow) AB_TEST_DIRECT_BUY else AB_TEST_BUY),
                eventLabel = errorMessage
        )

        sendGeneralEvent(data)
    }

    // 21 - DONE
    fun eventViewTickerErrorUnavailableProduct(shopId: String, errorMessage: String) {
        val data = getGtmData(
                eventName = EVENT_NAME_VIEW_MINICART_IRIS,
                eventAction = EVENT_VIEW_MINI_CART_WITH_UNAVAILABLE_PRODUCT,
                eventLabel = "$shopId - $errorMessage"
        )

        sendGeneralEvent(data)
    }

    // 22, 23 - DONE
    fun eventLoadMiniCartBottomSheetSuccess(products: List<MiniCartProductUiModel>) {
        val dataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT, EVENT_NAME_CHECKOUT_PROGRESS)
            putString(TrackAppUtils.EVENT_CATEGORY, EVENT_CATEGORY_MINICART)
            putString(TrackAppUtils.EVENT_ACTION, EVENT_ACTION_VIEW_MINI_CART_PAGE)
            putString(TrackAppUtils.EVENT_LABEL, EVENT_LABEL_SUCCESS)
            putString(KEY_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_PURCHASE_PLATFORM)
            putString(KEY_CURRENT_SITE, VALUE_CURRENT_SITE_TOKOPEDIA_MARKETPLACE)
            putString(KEY_USER_ID, userSession.userId)
            putString(KEY_CHECKOUT_OPTION, VALUE_CHECKOUT_OPTION_VIEW_MINI_CART_PAGE)
            putString(KEY_CHECKOUT_STEP, VALUE_CHECKOUT_STEP_ZERO)
            val items = ArrayList<Bundle>()
            products.forEach { product ->
                val bundle = getBundleProduct(product)
                items.add(bundle)
            }
            putParcelableArrayList(KEY_ITEMS, items)
        }

        sendEnhanceEcommerceEvent(
                eventName = EVENT_NAME_CHECKOUT_PROGRESS,
                bundle = dataLayer
        )
    }

    // 24 - DONE
    fun eventClickKnobToExpandMiniCartBottomSheet() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_KNOB_MINI_CART_BOTTOM_SHEET
        )

        sendGeneralEvent(data)
    }

    /* CHAT BOTTOM SHEET : https://mynakama.tokopedia.com/datatracker/requestdetail/view/1995 */
    // 1 - DONE
    fun eventClickBtnDirectChatBottomSheet() {
        val data = getGtmData(
                eventName = EVENT_NAME_CLICK_MINICART,
                eventAction = EVENT_ACTION_CLICK_DIRECT_CHAT_ON_BOTTOM_SHEET
        )
        sendGeneralEvent(data)
    }

    // 2 - DONE
    fun eventClickTickBoxChatBottomSheet(isChecked: Boolean) {
        val data = getGtmData(
            eventName = EVENT_NAME_CLICK_MINICART,
            eventAction = EVENT_ACTION_CLICK_PRODUCT_CARD_TICK_BOX_CHAT_ON_BOTTOM_SHEET,
            eventLabel = if (isChecked) EVENT_LABEL_TICK else EVENT_LABEL_UNTICK
        )
        sendGeneralEvent(data)
    }

    // 3 - DONE
    fun eventClickBtnAskProductChatBottomSheet() {
        val data = getGtmData(
            eventName = EVENT_NAME_CLICK_MINICART,
            eventAction = EVENT_ACTION_CLICK_ASK_PRODUCT_CHAT_ON_BOTTOM_SHEET
        )
        sendGeneralEvent(data)
    }

    /* MINI CART SIMPLIFIED MVC Page : https://mynakama.tokopedia.com/datatracker/requestdetail/view/2549 */
    // 6
    fun eventClickCheckCart(basketSize: String, isFulfilled: Boolean, shopId: String,
                            pageSource: Page?, businessUnit: String, currentSite: String) {
        val trackingData = TrackAppUtils.gtmData(EVENT_NAME_CLICK_PG, EVENT_CATEGORY_SHOP_PAGE_BUYER,
                EVENT_ACTION_CLICK_CHECK_CART, basketSize)
        trackingData[KEY_BUSINESS_UNIT] = businessUnit
        trackingData[KEY_CURRENT_SITE] = currentSite
        trackingData[KEY_PAGE_SOURCE] = if (pageSource == Page.MVC_PAGE) VALUE_PAGE_SOURCE_MVC_PAGE else ""
        trackingData[KEY_PROMO_CODE] = if (isFulfilled) VALUE_PROMO_CODE_FULFILLED else VALUE_PROMO_CODE_NOT_FULFILLED
        trackingData[KEY_SHOP_ID] = shopId
        trackingData[KEY_USER_ID] = userSession.userId
        sendGeneralEvent(trackingData)
    }

    // 8
    fun eventMvcProgressBarImpression(basketSize: String, promoPercentage: String, shopId: String,
                                      businessUnit: String, currentSite: String) {
        val trackingData = TrackAppUtils.gtmData(EVENT_NAME_VIEW_PG_IRIS, EVENT_CATEGORY_SHOP_PAGE_BUYER,
                EVENT_ACTION_MVC_PROGRESS_BAR_IMPRESSION, "$basketSize - $promoPercentage%")
        trackingData[KEY_BUSINESS_UNIT] = businessUnit
        trackingData[KEY_CURRENT_SITE] = currentSite
        trackingData[KEY_SHOP_ID] = shopId
        trackingData[KEY_USER_ID] = userSession.userId
        sendGeneralEvent(trackingData)
    }
}