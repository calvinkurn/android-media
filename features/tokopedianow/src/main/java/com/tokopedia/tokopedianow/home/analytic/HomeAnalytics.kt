package com.tokopedia.tokopedianow.home.analytic

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_ALL_CATEGORY
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CART_BUTTON
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SEARCH_BAR
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SHARE_BUTTON
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SLIDER_BANNER
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_SLIDER_BANNER
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_HOME_PAGE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.NAME_PROMOTION
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.CATEGORY.EVENT_CATEGORY_TOP_NAV
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ADD_TO_CART
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_AFFINITY_LABEL
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_SLOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_104
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_38
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_40
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_45
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_49
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_79
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_82
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_INDEX
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_BRAND
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_VARIANT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PAGE_SOURCE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRICE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRODUCT_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PROMOTIONS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_QUANTITY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_TYPE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT_RECOM_ADD_TO_CART
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_RECOM_HOME_PAGE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.PRODUCT_RECOM_PAGE_SOURCE
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics

class HomeAnalytics {

    object CATEGORY{
        const val EVENT_CATEGORY_HOME_PAGE = "tokonow - homepage"
        const val EVENT_CATEGORY_RECOM_HOME_PAGE = "tokonow - recom homepage"
    }

    object ACTION{
        const val EVENT_ACTION_CLICK_SEARCH_BAR = "click search bar on homepage"
        const val EVENT_ACTION_CLICK_CART_BUTTON = "click cart button on homepage"
        const val EVENT_ACTION_CLICK_SHARE_BUTTON = "click share button on homepage"
        const val EVENT_ACTION_CLICK_SLIDER_BANNER = "click slider banner"
        const val EVENT_ACTION_CLICK_ALL_CATEGORY = "click view all category widget"
        const val EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY = "click category on category widget"
        const val EVENT_ACTION_IMPRESSION_SLIDER_BANNER = "impression slider banner"
        const val EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM = "click view all on tokonow recom homepage"
        const val EVENT_ACTION_CLICK_PRODUCT_RECOM = "click product on tokonow product recom homepage"
        const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOM = "impression on tokonow product recom homepage"
        const val EVENT_ACTION_CLICK_PRODUCT_RECOM_ADD_TO_CART = "click add to cart on tokonow product recom homepage"
    }

    object VALUE {
        const val NAME_PROMOTION = "tokonow - p1 - promo"
        const val PRODUCT_RECOM_PAGE_SOURCE = "tokonow homepage.tokonow homepage"
    }

    fun onClickSearchBar() {
        hitCommonHomeTracker(
                getDataLayer(
                        event = EVENT_CLICK_TOKONOW,
                        action = EVENT_ACTION_CLICK_SEARCH_BAR,
                        category = EVENT_CATEGORY_TOP_NAV
                )
        )
    }

    fun onClickCartButton() {
        hitCommonHomeTracker(
                getDataLayer(
                        event = EVENT_CLICK_TOKONOW,
                        action = EVENT_ACTION_CLICK_CART_BUTTON,
                        category = EVENT_CATEGORY_TOP_NAV
                )
        )
    }

    //this is not P0 and right now we will not implement it
    fun onClickShareButton() {
        hitCommonHomeTracker(
                getDataLayer(
                        event = EVENT_CLICK_TOKONOW,
                        action = EVENT_ACTION_CLICK_SHARE_BUTTON,
                        category = EVENT_CATEGORY_TOP_NAV
                )
        )
    }

    fun onClickAllCategory() {
        hitCommonHomeTracker(
                getDataLayer(
                        event = EVENT_CLICK_TOKONOW,
                        action = EVENT_ACTION_CLICK_ALL_CATEGORY,
                        category = EVENT_CATEGORY_HOME_PAGE
                )
        )
    }

    fun onClickBannerPromo(position: Int, userId: String, channelModel: ChannelModel, channelGrid: ChannelGrid) {
        val dataLayer = getEcommerceDataLayer(
                event = EVENT_SELECT_CONTENT,
                action = EVENT_ACTION_CLICK_SLIDER_BANNER,
                category = EVENT_CATEGORY_HOME_PAGE,
                affinityLabel = channelModel.trackingAttributionModel.persona,
                userId = userId,
                promotions = arrayListOf(
                        ecommerceDataLayerBannerClicked(
                            channelModel = channelModel,
                            channelGrid = channelGrid,
                            position = position
                        )
                )
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun onImpressBannerPromo(userId: String, channelModel: ChannelModel) {
        val promotions = arrayListOf<Bundle>()
        channelModel.channelGrids.forEachIndexed { position, channelGrid ->
            promotions.add(
                    ecommerceDataLayerBannerImpressed(
                        channelModel = channelModel,
                        channelGrid = channelGrid,
                        position = position
                    )
            )
        }

        val dataLayer = getEcommerceDataLayer(
                event = EVENT_VIEW_ITEM,
                action = EVENT_ACTION_IMPRESSION_SLIDER_BANNER,
                category = EVENT_CATEGORY_HOME_PAGE,
                affinityLabel = channelModel.trackingAttributionModel.persona,
                userId = userId,
                promotions = promotions

        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun onClickCategory(position: Int, userId: String, categoryId: String) {
        val dataLayer = getEcommerceDataLayer(
                event = EVENT_SELECT_CONTENT,
                action = EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY,
                category = EVENT_CATEGORY_HOME_PAGE,
                affinityLabel = "null",
                userId = userId,
                promotions = arrayListOf(
                        ecommerceDataLayerCategoryClicked(
                            categoryId = categoryId,
                            position = position
                        )
                )
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun onClickAllProductRecom(channelId: String, headerName: String) {
        hitCommonHomeTracker(
            getDataLayer(
                event = EVENT_CLICK_TOKONOW,
                action = EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM,
                category = EVENT_CATEGORY_RECOM_HOME_PAGE,
                label = "{$channelId - $headerName}"
            )
        )
    }

    fun onClickProductRecom(channelId: String, headerName: String, userId: String, recommendationItem: RecommendationItem, position: String) {
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT_RECOM,
            category = EVENT_CATEGORY_RECOM_HOME_PAGE,
            label = "$channelId - $headerName",
            userId = userId,
            productId = recommendationItem.productId.toString(),
            needItemList = true,
            recommendationType = recommendationItem.recommendationType,
            pageName = recommendationItem.pageName,
            headerName = headerName,
            items = arrayListOf(
                productRecomItemDataLayer(
                    index = position,
                    productId = recommendationItem.productId.toString(),
                    productName = recommendationItem.name,
                    price = recommendationItem.price
                )
            )
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun onImpressProductRecom(channelId: String, headerName: String, userId: String, recomItems: List<RecommendationItem>, pageName: String) {
        val items = arrayListOf<Bundle>()
        recomItems.forEachIndexed { position, recomItem ->
            items.add(
                productRecomItemDataLayer(
                    index = position.toString(),
                    productId = recomItem.productId.toString(),
                    productName = recomItem.name,
                    price = recomItem.price
                )
            )
        }

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_PRODUCT_RECOM,
            category = EVENT_CATEGORY_RECOM_HOME_PAGE,
            label = "$channelId - $headerName",
            userId = userId,
            needItemList = false,
            pageName = pageName,
            headerName = headerName,
            items = items
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, dataLayer)
    }

    fun onClickProductRecomAddToCart(channelId: String, headerName: String, userId: String, quantity: String, recommendationItem: RecommendationItem, position: String, cartId: String) {
        val item = productRecomItemDataLayer(
            index = position,
            productId = recommendationItem.productId.toString(),
            productName = recommendationItem.name,
            price = recommendationItem.price
        ).apply {
            putString(KEY_DIMENSION_40, "{'list': '/tokonow - recomproduct - carousel - ${recommendationItem.recommendationType} - ${recommendationItem.pageName} - $headerName'}")
            putString(KEY_DIMENSION_45, cartId)
            putString(KEY_QUANTITY, quantity)
            putString(KEY_SHOP_ID, recommendationItem.shopId.toString())
            putString(KEY_SHOP_NAME, recommendationItem.shopName)
            putString(KEY_SHOP_TYPE, recommendationItem.type)
            putString(KEY_CATEGORY_ID, "")
        }

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_ADD_TO_CART,
            action = EVENT_ACTION_CLICK_PRODUCT_RECOM_ADD_TO_CART,
            category = EVENT_CATEGORY_RECOM_HOME_PAGE,
            label = "$channelId - $headerName",
            userId = userId,
            productId = recommendationItem.productId.toString(),
            needItemList = false,
            recommendationType = recommendationItem.recommendationType,
            pageName = recommendationItem.pageName,
            headerName = headerName,
            items = arrayListOf(item)
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    private fun ecommerceDataLayerBannerClicked(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int): Bundle {
        return Bundle().apply {
            putString(KEY_CREATIVE_NAME, channelModel.trackingAttributionModel.galaxyAttribution)
            putString(KEY_CREATIVE_SLOT, (position + 1).toString())
            putString(KEY_DIMENSION_104, channelModel.trackingAttributionModel.campaignCode)
            putString(KEY_DIMENSION_38, channelModel.trackingAttributionModel.galaxyAttribution)
            putString(KEY_DIMENSION_79, channelModel.trackingAttributionModel.brandId)
            putString(KEY_DIMENSION_82, channelModel.trackingAttributionModel.categoryId)
            putString(KEY_ITEM_ID, "0_" + channelGrid.id+ "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId)
            putString(KEY_ITEM_NAME, NAME_PROMOTION)
        }
    }

    private fun ecommerceDataLayerBannerImpressed(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int): Bundle {
        return Bundle().apply {
            putString(KEY_CREATIVE_NAME, channelModel.trackingAttributionModel.galaxyAttribution)
            putString(KEY_CREATIVE_SLOT, (position + 1).toString())
            putString(KEY_ITEM_ID, "0_" + channelGrid.id + "_" + channelModel.trackingAttributionModel.persoType + "_" + channelModel.trackingAttributionModel.categoryId)
            putString(KEY_ITEM_NAME, NAME_PROMOTION)
        }
    }

    private fun ecommerceDataLayerCategoryClicked(position: Int, categoryId: String): Bundle {
        val nullString = "null"
        return Bundle().apply {
            putString(KEY_CREATIVE_NAME, nullString)
            putString(KEY_CREATIVE_SLOT, (position + 1).toString())
            putString(KEY_DIMENSION_49, nullString)
            putString(KEY_DIMENSION_38, nullString)
            putString(KEY_DIMENSION_79, nullString)
            putString(KEY_DIMENSION_82, nullString)
            putString(KEY_ITEM_ID, "0_" + categoryId + "_" + nullString + "_" + nullString)
            putString(KEY_ITEM_NAME, NAME_PROMOTION)
        }
    }

    private fun getDataLayer(event: String, action: String, category: String, label: String = ""): MutableMap<String, Any> {
        return DataLayer.mapOf(
                TrackAppUtils.EVENT, event,
                TrackAppUtils.EVENT_ACTION, action,
                TrackAppUtils.EVENT_CATEGORY, category,
                TrackAppUtils.EVENT_LABEL, label
        )
    }

    private fun getEcommerceDataLayer(event: String, action: String, category: String, label: String = "", affinityLabel: String = "", userId: String, promotions: ArrayList<Bundle>): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_AFFINITY_LABEL, affinityLabel)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_USER_ID, userId)
        }
    }

    private fun getEcommerceDataLayer(event: String, action: String, category: String, label: String = "", productId: String = "", userId: String, items: ArrayList<Bundle>, needItemList: Boolean, recommendationType: String = "", pageName: String, headerName: String,): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_PAGE_SOURCE, PRODUCT_RECOM_PAGE_SOURCE)
            putString(KEY_USER_ID, userId)
            putParcelableArrayList(KEY_ITEMS, items)
            if (productId.isNotBlank()) {
                putString(KEY_PRODUCT_ID, productId)
            }
            if (needItemList) {
                putString(KEY_ITEM_LIST, "{'list': '/tokonow - recomproduct - carousel - $recommendationType - $pageName - $headerName'}")
            }
        }
    }

    private fun productRecomItemDataLayer(index: String, productId: String, productName: String, price: String, productBrand: String = "", productCategory: String = "", productVariant: String = ""): Bundle {
        return Bundle().apply {
            putString(KEY_INDEX, index)
            putString(KEY_ITEM_BRAND, productBrand)
            putString(KEY_ITEM_CATEGORY, productCategory)
            putString(KEY_ITEM_ID, productId)
            putString(KEY_ITEM_NAME, productName)
            putString(KEY_ITEM_VARIANT, productVariant)
            putString(KEY_PRICE, price)
        }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun hitCommonHomeTracker(dataLayer: MutableMap<String, Any>) {
        getTracker().sendGeneralEvent(dataLayer.getHomeGeneralTracker())
    }

    private fun MutableMap<String, Any>.getHomeGeneralTracker(): MutableMap<String, Any>? {
        this[TrackAppUtils.EVENT] = EVENT_CLICK_TOKONOW
        this[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
        this[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        return this
    }
}