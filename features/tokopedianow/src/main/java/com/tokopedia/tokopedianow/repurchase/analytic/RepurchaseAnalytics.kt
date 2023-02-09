package com.tokopedia.tokopedianow.repurchase.analytic

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.productcard_compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_ADD_TO_CART_REPURCHASE
import com.tokopedia.productcard_compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_CLICK_CLOSE_BOTTOMSHEET_REPURCHASE
import com.tokopedia.productcard_compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_CLICK_PRODUCT_REPURCHASE
import com.tokopedia.productcard_compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_CLICK_SIMILAR_PRODUCT_BUTTON_REPURCHASE
import com.tokopedia.productcard_compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_VIEW_EMPTY_STATE_REPURCHASE
import com.tokopedia.productcard_compact.similarproduct.analytic.ProductCardCompactSimilarProductAnalyticsConstants.TRACKER_ID_VIEW_SIMILAR_PRODUCT_BOTTOMSHEET_REPURCHASE
import com.tokopedia.productcard_compact.similarproduct.presentation.uimodel.ProductCardCompactSimilarProductUiModel
import com.tokopedia.tokopedianow.category.analytics.CategoryTracking
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_ADD_TO_WISHLIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_CATEGORY_MENU_WIDGET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_REMOVE_FROM_WISHLIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_CLICK_SEE_ALL_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.ACTION.EVENT_ACTION_IMPRESSION_CATEGORY_MENU_WIDGET
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOKOPEDIA_CATEGORY_PAGE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOKOPEDIA_REPURCHASE_PAGE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_GROCERIES
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_TOKONOW_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_INDEX
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_BRAND
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_VARIANT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRICE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRODUCT_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_QUANTITY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_TYPE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_TRACKER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.DEFAULT_HEADER_CATEGORY_MENU
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getDataLayer
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getEcommerceDataLayerCategoryMenu
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getEcommerceDataLayerCategoryMenuPromotion
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.getTracker
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalytics.hitCommonTracker
import com.tokopedia.tokopedianow.common.util.TrackerUtil.getTrackerPosition
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_ADD_TO_CART
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_APPLY_CATEGORY_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_APPLY_DATE_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_APPLY_MOST_PURCHASE_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_CART_NAV
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_CATEGORY_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_CHANGE_ADDRESS
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_DATE_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_MOST_PURCHASE_FILTER
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_SIMILAR_PRODUCT
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_CLICK_SUBMIT_SEARCH
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.ACTION.EVENT_ACTION_IMPRESSION_NO_RESULT_REPURCHASE
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.CATEGORY.EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.TRACKER_ID.TRACKER_ID_CLICK_CATEGORY_MENU_WIDGET
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.TRACKER_ID.TRACKER_ID_CLICK_SEE_ALL_CATEGORY
import com.tokopedia.tokopedianow.repurchase.analytic.RepurchaseAnalytics.TRACKER_ID.TRACKER_ID_IMPRESSION_CATEGORY_MENU_WIDGET
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.constant.TrackerConstant
import com.tokopedia.user.session.UserSessionInterface
import java.io.Serializable
import javax.inject.Inject

class RepurchaseAnalytics @Inject constructor(@Transient private val userSession: UserSessionInterface): Serializable {

    object CATEGORY{
        const val EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW = "repurchase page tokonow"
    }

    object ACTION {
        const val EVENT_ACTION_CLICK_ADD_TO_CART = "click add to cart"
        const val EVENT_ACTION_CLICK_PRODUCT = "click product"
        const val EVENT_ACTION_CLICK_SIMILAR_PRODUCT = "click produk serupa"
        const val EVENT_ACTION_IMPRESSION_NO_RESULT_REPURCHASE = "view no result repurchase"
        const val EVENT_ACTION_CLICK_MOST_PURCHASE_FILTER = "click most purchase filter"
        const val EVENT_ACTION_CLICK_APPLY_MOST_PURCHASE_FILTER = "click terapkan on most purchase filter"
        const val EVENT_ACTION_CLICK_CATEGORY_FILTER = "click category filter"
        const val EVENT_ACTION_CLICK_APPLY_CATEGORY_FILTER = "click terapkan on category filter"
        const val EVENT_ACTION_CLICK_DATE_FILTER = "click date filter"
        const val EVENT_ACTION_CLICK_APPLY_DATE_FILTER = "click terapkan on date filter"
        const val EVENT_ACTION_CLICK_SUBMIT_SEARCH = "submit search from cari product"
        const val EVENT_ACTION_CLICK_CART_NAV = "click cart nav"
        const val EVENT_ACTION_CLICK_CHANGE_ADDRESS = "click change address"
    }

    object VALUE {
        const val REPURCHASE_TOKONOW = "repurchase page tokonow"
        const val SHOP_NAME = "Tokopedia NOW!"
        const val SHOP_TYPE = "tokonow"
    }

    object TRACKER_ID {
        const val TRACKER_ID_IMPRESSION_CATEGORY_MENU_WIDGET = "40828"
        const val TRACKER_ID_CLICK_CATEGORY_MENU_WIDGET = "40829"
        const val TRACKER_ID_CLICK_SEE_ALL_CATEGORY = "40830"
    }

    fun onClickChangeAddress(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_CHANGE_ADDRESS,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickCartNav(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_CART_NAV,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickSimilarProduct(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_SIMILAR_PRODUCT,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onImpressNoHistoryRepurchase(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_VIEW_TOKONOW_IRIS,
            action = EVENT_ACTION_IMPRESSION_NO_RESULT_REPURCHASE,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickMostPurchaseFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_MOST_PURCHASE_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickApplyMostPurchaseFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_APPLY_MOST_PURCHASE_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickCategoryFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_CATEGORY_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickApplyCategoryFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_APPLY_CATEGORY_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickDateFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_DATE_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickApplyDateFilter(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_APPLY_DATE_FILTER,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    // the feature is not ready yet
    fun onClickSubmitSearch(userId: String) {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_SUBMIT_SEARCH,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW
        )
        dataLayer[KEY_USER_ID] = userId
        hitCommonTracker(
            dataLayer
        )
    }

    fun onClickAddToCart(userId: String, quantity: Int, model: RepurchaseProductUiModel) {
        val item = ecommerceDataLayerAddToCartClicked(model, quantity)

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_ADD_TO_CART,
            action = EVENT_ACTION_CLICK_ADD_TO_CART,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW,
            userId = userId,
            items = arrayListOf(item)
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_ADD_TO_CART, dataLayer)
    }

    fun onClickProduct(userId: String, model: RepurchaseProductUiModel, position: Int) {
        val item = ecommerceDataLayerProductClicked(model, position)

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW,
            userId = userId,
            items = arrayListOf(item)
        )
        dataLayer.putString(KEY_ITEM_LIST, "")
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    private fun getEcommerceDataLayer(event: String, action: String, category: String, userId: String, items: ArrayList<Bundle>, label: String = ""): Bundle {
        return Bundle().apply {
            putString(EVENT, event)
            putString(EVENT_ACTION, action)
            putString(EVENT_CATEGORY, category)
            putString(EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_USER_ID, userId)
            putParcelableArrayList(KEY_ITEMS, items)
        }
    }

    private fun ecommerceDataLayerAddToCartClicked(model: RepurchaseProductUiModel, quantity: Int): Bundle {
        return Bundle().apply {
            putString(KEY_CATEGORY_ID, model.categoryId)
            putString(KEY_ITEM_BRAND, "")
            putString(KEY_ITEM_CATEGORY, model.category)
            putString(KEY_ITEM_ID, model.productCardModel.productId)
            putString(KEY_ITEM_NAME, model.productCardModel.name)
            putString(KEY_ITEM_VARIANT, "")
            putLong(KEY_PRICE, model.productCardModel.price.filter { it.isDigit() }.toLongOrZero())
            putString(KEY_QUANTITY, quantity.toString())
            putString(KEY_SHOP_ID, model.shopId)
            putString(KEY_SHOP_NAME, VALUE.SHOP_NAME)
            putString(KEY_SHOP_TYPE, VALUE.SHOP_TYPE)
        }
    }

    private fun ecommerceDataLayerProductClicked(model: RepurchaseProductUiModel, position: Int): Bundle {
        return Bundle().apply {
            putString(KEY_INDEX, position.toString())
            putString(KEY_ITEM_BRAND, "")
            putString(KEY_ITEM_CATEGORY, model.category)
            putString(KEY_ITEM_ID, model.productCardModel.productId)
            putString(KEY_ITEM_NAME, model.productCardModel.name)
            putString(KEY_ITEM_VARIANT, "")
            putString(KEY_PRICE, model.productCardModel.price)
        }
    }

    fun trackClickAddToWishlist(warehouseId: String, productId: String){
        val label = "$warehouseId - $productId"

        val dataLayer = getDataLayer(
            EVENT_CLICK_GROCERIES,
            EVENT_ACTION_CLICK_ADD_TO_WISHLIST,
            EVENT_CATEGORY_TOKOPEDIA_REPURCHASE_PAGE,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_TRACKER_ID] = TokoNowCommonAnalyticConstants.TRACKER_ID.TRACKER_ID_ADD_TO_WISHLIST_REPURCHASE
        dataLayer[KEY_PRODUCT_ID] = productId

        getTracker().sendGeneralEvent(dataLayer)
    }

    fun trackClickRemoveFromWishlist(warehouseId: String, productId: String){
        val label = "$warehouseId - $productId"

        val dataLayer = getDataLayer(
            EVENT_CLICK_GROCERIES,
            EVENT_ACTION_CLICK_REMOVE_FROM_WISHLIST,
            EVENT_CATEGORY_TOKOPEDIA_CATEGORY_PAGE,
            label
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_TRACKER_ID] = TokoNowCommonAnalyticConstants.TRACKER_ID.TRACKER_ID_REMOVE_FROM_WISHLIST_REPURCHASE
        dataLayer[KEY_PRODUCT_ID] = productId

        getTracker().sendGeneralEvent(dataLayer)
    }

    /* Similar Product Bottomsheet Trackers*/

    fun trackClickSimilarProductBtn(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_GROCERIES,
            action = CategoryTracking.Action.EVENT_ACTION_CLICK_SIMILAR_PRODUCT_BTN,
            label = "$warehouseId - $productIdTriggered",
            userId = userId
        ).apply {
            putString(KEY_PRODUCT_ID, productIdTriggered)
            putString(KEY_TRACKER_ID, TRACKER_ID_CLICK_SIMILAR_PRODUCT_BUTTON_REPURCHASE)
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_GROCERIES,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionBottomSheet(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String
    ) {
        val items =  arrayListOf(
            createProductItemDataLayer(
                index = similarProduct.position,
                id = similarProduct.id,
                name = similarProduct.name,
                price = similarProduct.priceFmt.getDigits().orZero().toFloat(),
                category = similarProduct.categoryName
            )
        )

        val dataLayer = createGeneralDataLayer(
            event = TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST,
            action = CategoryTracking.Action.EVENT_ACTION_IMPRESSION_BOTTOMSHEET,
            label = "$warehouseId - $productIdTriggered - ${similarProduct.id}",
            userId = userId
        ).apply {
            putString(KEY_PRODUCT_ID, similarProduct.id)
            putString(KEY_TRACKER_ID, TRACKER_ID_VIEW_SIMILAR_PRODUCT_BOTTOMSHEET_REPURCHASE)
            putString(KEY_ITEM_LIST, "/tokonow - product card - similar product recom")
            putParcelableArrayList(KEY_ITEMS, items)
        }

        sendEnhanceEcommerceEvent(
            eventName = TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_PG_IRIS,
            dataLayer = dataLayer
        )
    }

    fun trackClickProduct(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String
    ) {
        val items = arrayListOf(
            createProductItemDataLayer(
                index = similarProduct.position,
                id = similarProduct.id,
                name = similarProduct.name,
                price = similarProduct.priceFmt.getDigits().orZero().toFloat(),
                category = similarProduct.categoryName
            )
        )

        val dataLayer = createGeneralDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = CategoryTracking.Action.EVENT_ACTION_CLICK_PRODUCT,
            label = "$warehouseId - $productIdTriggered - ${similarProduct.id}",
            userId = userId
        ).apply {
            putString(KEY_PRODUCT_ID, similarProduct.id)
            putString(KEY_TRACKER_ID, TRACKER_ID_CLICK_PRODUCT_REPURCHASE)
            putString(KEY_ITEM_LIST, "/tokonow - product card - similar product recom")
            putParcelableArrayList(KEY_ITEMS, items)
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_SELECT_CONTENT,
            dataLayer = dataLayer
        )
    }

    fun trackClickAddToCart(
        userId: String,
        warehouseId: String,
        similarProduct: ProductCardCompactSimilarProductUiModel,
        productIdTriggered: String,
        newQuantity: Int
    ) {
        val items = arrayListOf(
            createAtcProductItemDataLayer(
                id = similarProduct.id,
                name = similarProduct.name,
                price = similarProduct.priceFmt.getDigits().orZero().toFloat(),
                categoryName = similarProduct.categoryName,
                categoryId = similarProduct.categoryId,
                quantity = newQuantity.toString(),
                shopId = similarProduct.shopId,
                shopName = similarProduct.shopName
            )
        )

        val dataLayer = createGeneralDataLayer(
            event = EVENT_ADD_TO_CART,
            action = CategoryTracking.Action.EVENT_ACTION_CLICK_ADD_TO_CART,
            label = "$warehouseId - $productIdTriggered - ${similarProduct.id}",
            userId = userId
        ).apply {
            putString(KEY_USER_ID, userId)
            putString(KEY_PRODUCT_ID, similarProduct.id)
            putString(KEY_TRACKER_ID, TRACKER_ID_ADD_TO_CART_REPURCHASE)
            putParcelableArrayList(KEY_ITEMS, items)
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_ADD_TO_CART,
            dataLayer = dataLayer
        )
    }

    fun trackClickCloseBottomsheet(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        val dataLayer = createGeneralDataLayer(
            event = EVENT_CLICK_GROCERIES,
            action = CategoryTracking.Action.EVENT_ACTION_CLICK_CLOSE_BOTTOMSHEET,
            label = "$warehouseId - $productIdTriggered",
            userId = userId
        ).apply {
            putString(KEY_TRACKER_ID, TRACKER_ID_CLICK_CLOSE_BOTTOMSHEET_REPURCHASE)
        }

        sendEnhanceEcommerceEvent(
            eventName = EVENT_CLICK_GROCERIES,
            dataLayer = dataLayer
        )
    }

    fun trackImpressionEmptyState(
        userId: String,
        warehouseId: String,
        productIdTriggered: String
    ) {
        val dataLayer = createGeneralDataLayer(
            event = TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES,
            action = CategoryTracking.Action.EVENT_ACTION_IMPRESSION_EMPTY_STATE,
            label = "$warehouseId - $productIdTriggered",
            userId = userId
        ).apply {
            putString(KEY_TRACKER_ID, TRACKER_ID_VIEW_EMPTY_STATE_REPURCHASE)
        }

        sendEnhanceEcommerceEvent(
            eventName = TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_GROCERIES,
            dataLayer = dataLayer
        )
    }

    /**
     * NOW! SeeAllCategory Page Tracker
     * https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3695
     */

    fun trackImpressCategoryMenu(
        categoryId: String,
        categoryName: String,
        warehouseId: String,
        position: Int
    ) {
        val newPosition = position.getTrackerPosition()
        val dataLayer = getEcommerceDataLayerCategoryMenu(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_CATEGORY_MENU_WIDGET,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW,
            label = "",
            trackerId = TRACKER_ID_IMPRESSION_CATEGORY_MENU_WIDGET,
            promotions = arrayListOf(
                getEcommerceDataLayerCategoryMenuPromotion(
                    categoryId = categoryId,
                    categoryName = categoryName,
                    warehouseId = warehouseId,
                    position = newPosition,
                    itemName = getCategoryMenuItemName(
                        position = newPosition,
                        headerName = DEFAULT_HEADER_CATEGORY_MENU
                    )
                )
            ),
            warehouseId = warehouseId,
            userId = userSession.userId
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun trackClickCategoryMenu(
        categoryId: String,
        categoryName: String,
        warehouseId: String,
        position: Int
    ) {
        val newPosition = position.getTrackerPosition()
        val dataLayer = getEcommerceDataLayerCategoryMenu(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_CATEGORY_MENU_WIDGET,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW,
            label = "",
            trackerId = TRACKER_ID_CLICK_CATEGORY_MENU_WIDGET,
            promotions = arrayListOf(
                getEcommerceDataLayerCategoryMenuPromotion(
                    categoryId = categoryId,
                    categoryName = categoryName,
                    warehouseId = warehouseId,
                    position = newPosition,
                    itemName = getCategoryMenuItemName(
                        position = newPosition,
                        headerName = DEFAULT_HEADER_CATEGORY_MENU
                    )
                )
            ),
            warehouseId = warehouseId,
            userId = userSession.userId
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackClickSeeAllCategory() {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_GROCERIES,
            action = EVENT_ACTION_CLICK_SEE_ALL_CATEGORY,
            category = EVENT_CATEGORY_REPURCHASE_PAGE_TOKONOW,
            label = ""
        )

        dataLayer[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_CURRENT_SITE] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        dataLayer[KEY_TRACKER_ID] = TRACKER_ID_CLICK_SEE_ALL_CATEGORY

        getTracker().sendGeneralEvent(dataLayer)
    }

    private fun getCategoryMenuItemName(position: Int, headerName: String): String {
        return "/ - p$position - repurchase page - category widget - $headerName"
    }

    private fun createGeneralDataLayer(event: String, action: String, label: String = TokoNowCommonAnalyticConstants.VALUE.DEFAULT_EMPTY_VALUE, userId: String): Bundle {
        return Bundle().apply {
            putString(TrackerConstant.EVENT, event)
            putString(TrackerConstant.EVENT_ACTION, action)
            putString(TrackerConstant.EVENT_CATEGORY, TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOKOPEDIA_REPURCHASE_PAGE)
            putString(TrackerConstant.EVENT_LABEL, label)
            putString(
                TrackerConstant.BUSINESS_UNIT,
                BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
            )
            putString(TrackerConstant.CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(TrackerConstant.USERID, userId)
        }
    }

    private fun createProductItemDataLayer(
        index: Int = 0,
        id: String = "",
        name: String = "",
        price: Float = 0f,
        brand: String = "none/other",
        category: String = "",
        variant: String = "none/other"
    ): Bundle {
        return Bundle().apply {
            putInt(KEY_INDEX, index)
            putString(KEY_ITEM_BRAND, brand)
            putString(KEY_ITEM_CATEGORY, category)
            putString(KEY_ITEM_ID, id)
            putString(KEY_ITEM_NAME, name)
            putString(KEY_ITEM_VARIANT, variant)
            putFloat(KEY_PRICE, price)
        }
    }

    private fun createAtcProductItemDataLayer(
        id: String = "",
        name: String = "",
        price: Float = 0f,
        brand: String = "none/other",
        categoryName: String = "",
        categoryId: String = "",
        quantity: String = "",
        variant: String = "none/other",
        shopId: String = "",
        shopName: String = DEFAULT_EMPTY_VALUE
    ): Bundle {
        return Bundle().apply {
            putString(KEY_ITEM_BRAND, brand)
            putString(KEY_ITEM_CATEGORY, categoryName)
            putString(KEY_ITEM_ID, id)
            putString(KEY_ITEM_NAME, name)
            putString(KEY_ITEM_VARIANT, variant)
            putFloat(KEY_PRICE, price)
            putString(KEY_CATEGORY_ID, categoryId)
            putString(KEY_QUANTITY, quantity)
            putString(KEY_SHOP_ID, shopId)
            putString(KEY_SHOP_NAME, DEFAULT_EMPTY_VALUE)
            putString(KEY_SHOP_TYPE, DEFAULT_EMPTY_VALUE)
        }
    }

    private fun sendEnhanceEcommerceEvent(eventName: String, dataLayer: Bundle) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(eventName, dataLayer)
    }

}
