package com.tokopedia.tokopedianow.home.analytic

import android.os.Bundle
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.productcard.ProductCardModel.*
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
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_ATC
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_PG
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_CLICK_TOKONOW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PRODUCT_CLICK
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_PRODUCT_VIEW
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_SELECT_CONTENT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.EVENT.EVENT_VIEW_PG_IRIS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ACTION_FIELD
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ADD
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_AFFINITY_LABEL
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BRAND
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_BUSINESS_UNIT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CATEGORY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CLICK
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CREATIVE_SLOT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENCY_CODE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_CURRENT_SITE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_104
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_38
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_40
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_45
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_49
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_79
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_DIMENSION_82
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ECOMMERCE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_IMPRESSIONS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_INDEX
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEMS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_BRAND
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_CATEGORY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_ITEM_VARIANT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_LIST
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PAGE_SOURCE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_POSITION
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRICE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRODUCTS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PRODUCT_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_PROMOTIONS
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_QUANTITY
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_NAME
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_SHOP_TYPE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_USER_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_VARIANT
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.KEY.KEY_WAREHOUSE_ID
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENCY_CODE_IDR
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
import com.tokopedia.tokopedianow.common.analytics.TokoNowCommonAnalyticConstants.VALUE.LIST_HOME_PAGE_PAST_PURCHASE_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_ATC_PAST_PURCHASE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM_OOC
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PAST_PURCHASE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT_RECOM_ADD_TO_CART
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT_RECOM_OOC
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_PAST_PURCHASE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_PRODUCT_RECOM_OOC
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.CATEGORY.EVENT_CATEGORY_RECOM_HOME_PAGE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.LABEL_GROUP_HALAL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.NORMAL_PRICE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.PRODUCT_RECOM_PAGE_SOURCE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.PRODUCT_TOPADS
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.SLASH_PRICE
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.WITHOUT_HALAL_LABEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.WITHOUT_VARIANT
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.WITH_HALAL_LABEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.WITH_VARIANT
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_CLOSE_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_BANNER_LEFT_CAROUSEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_LEGO_3
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_PRODUCT_LEFT_CAROUSEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_QUEST_CARD_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_REWARD_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SEE_DETAILS_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_SHARE_TO_OTHERS
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_TITLE_CARD_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_USP_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_CLICK_VIEW_ALL_LEFT_CAROUSEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_FINISHED_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_LEFT_CAROUSEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_LEGO_3
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_PRODUCT_LEFT_CAROUSEL
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_QUEST_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.ACTION.EVENT_ACTION_IMPRESSION_USP_WIDGET
import com.tokopedia.tokopedianow.home.analytic.HomeAnalytics.VALUE.ITEM_LIST_LEFT_CAROUSEL
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class HomeAnalytics @Inject constructor(private val userSession: UserSessionInterface) {

    object CATEGORY{
        const val EVENT_CATEGORY_HOME_PAGE = "tokonow - homepage"
        const val EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN = "tokonow homepage"
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
        const val EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM_OOC = "click 'Cek Sekarang' on recom widget on tokonow homepage while the address is out of coverage (OOC)"
        const val EVENT_ACTION_CLICK_PRODUCT_RECOM = "click product on tokonow product recom homepage"
        const val EVENT_ACTION_CLICK_PRODUCT_RECOM_OOC = "click product on recom widget on tokonow homepage while the address is out of coverage (OOC)"
        const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOM = "impression on tokonow product recom homepage"
        const val EVENT_ACTION_IMPRESSION_PRODUCT_RECOM_OOC = "view product on recom widget on tokonow homepage while the address is out of coverage (OOC)"
        const val EVENT_ACTION_IMPRESSION_PAST_PURCHASE = "impression on past purchase widget"
        const val EVENT_ACTION_CLICK_PAST_PURCHASE = "click product on past purchase widget"
        const val EVENT_ACTION_ATC_PAST_PURCHASE = "click atc on past purchase widget"
        const val EVENT_ACTION_CLICK_PRODUCT_RECOM_ADD_TO_CART = "click add to cart on tokonow product recom homepage"
        const val EVENT_ACTION_CLICK_SHARE_TO_OTHERS = "click share to others"
        const val EVENT_ACTION_IMPRESSION_LEFT_CAROUSEL = "impression left carousel widget"
        const val EVENT_ACTION_CLICK_BANNER_LEFT_CAROUSEL = "click left banner on left carousel widget"
        const val EVENT_ACTION_CLICK_VIEW_ALL_LEFT_CAROUSEL = "click view all on left carousel widget"
        const val EVENT_ACTION_IMPRESSION_PRODUCT_LEFT_CAROUSEL = "impression product on left carousel"
        const val EVENT_ACTION_CLICK_PRODUCT_LEFT_CAROUSEL = "click product on left carousel"
        const val EVENT_ACTION_IMPRESSION_LEGO_3 = "impression lego 3 banner"
        const val EVENT_ACTION_CLICK_LEGO_3 = "click lego 3 banner"
        const val EVENT_ACTION_IMPRESSION_QUEST_WIDGET = "impression quest widget"
        const val EVENT_ACTION_CLICK_SEE_DETAILS_QUEST_WIDGET = "click see details quest widget"
        const val EVENT_ACTION_CLICK_TITLE_CARD_QUEST_WIDGET = "click title card on quest widget"
        const val EVENT_ACTION_CLICK_QUEST_CARD_QUEST_WIDGET = "click quest card on quest widget"
        const val EVENT_ACTION_IMPRESSION_FINISHED_QUEST_WIDGET = "impression finish card quest widget"
        const val EVENT_ACTION_CLICK_REWARD_QUEST_WIDGET = "click cek hadiah saya on quest widget"
        const val EVENT_ACTION_CLICK_CLOSE_QUEST_WIDGET = "click close quest widget"
        const val EVENT_ACTION_IMPRESSION_USP_WIDGET = "impression usp widget"
        const val EVENT_ACTION_CLICK_USP_WIDGET = "click drop down on usp widget"
    }

    object VALUE {
        const val NAME_PROMOTION = "tokonow - p1 - promo"
        const val PRODUCT_RECOM_PAGE_SOURCE = "tokonow homepage.tokonow homepage"
        const val LABEL_GROUP_HALAL = "Halal"
        const val WITH_HALAL_LABEL = "with halal label"
        const val WITHOUT_HALAL_LABEL = "without halal label"
        const val SLASH_PRICE = "slash price"
        const val NORMAL_PRICE = "normal price"
        const val WITH_VARIANT = "with variant"
        const val WITHOUT_VARIANT = "without variant"
        const val PRODUCT_TOPADS = "product topads"
        const val HOMEPAGE_TOKONOW = "homepage tokonow"
        const val ITEM_LIST_LEFT_CAROUSEL = "/tokonow - left carousel - carousel"
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

    fun onClickShareButton() {
        hitCommonHomeTracker(
                getDataLayer(
                        event = EVENT_CLICK_TOKONOW,
                        action = EVENT_ACTION_CLICK_SHARE_BUTTON,
                        category = EVENT_CATEGORY_TOP_NAV
                )
        )
    }

    fun onClickShareToOthers() {
        val dataLayer = getDataLayer(
            event = EVENT_CLICK_TOKONOW,
            action = EVENT_ACTION_CLICK_SHARE_TO_OTHERS,
            category = EVENT_CATEGORY_HOME_PAGE
        )
        dataLayer[KEY_USER_ID] = userSession.userId
        hitCommonHomeTracker(
            dataLayer
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

    fun onClickBannerPromo(position: Int, channelModel: ChannelModel, channelGrid: ChannelGrid) {
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_SLIDER_BANNER,
            category = EVENT_CATEGORY_HOME_PAGE,
            affinityLabel = channelModel.trackingAttributionModel.persona,
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

    fun onImpressBannerPromo(channelModel: ChannelModel, warehouseId: String) {
        val promotions = arrayListOf<Bundle>()
        channelModel.channelGrids.forEachIndexed { position, channelGrid ->
            promotions.add(
                    ecommerceDataLayerBanner(
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
            promotions = promotions
        )
        dataLayer.putString(KEY_WAREHOUSE_ID, warehouseId)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun onClickCategory(position: Int, categoryId: String) {
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_CATEGORY_ON_CATEGORY,
            category = EVENT_CATEGORY_HOME_PAGE,
            affinityLabel = "null",
            promotions = arrayListOf(
                    ecommerceDataLayerCategoryClicked(
                        categoryId = categoryId,
                        position = position
                    )
            )
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun onClickAllProductRecom(channelId: String, headerName: String, isOoc: Boolean) {
        hitCommonHomeTracker(
            getDataLayer(
                event = EVENT_CLICK_TOKONOW,
                action = if (isOoc) EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM_OOC else EVENT_ACTION_CLICK_ALL_PRODUCT_RECOM,
                category = if (isOoc) EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN else EVENT_CATEGORY_RECOM_HOME_PAGE,
                label = if (isOoc) " - $headerName" else "$channelId - $headerName"
            )
        )
    }

    fun onClickProductRecom(channelId: String, headerName: String, recommendationItem: RecommendationItem, position: String, isOoc: Boolean) {
        val dataLayer = getEcommerceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = if (isOoc) EVENT_ACTION_CLICK_PRODUCT_RECOM_OOC else EVENT_ACTION_CLICK_PRODUCT_RECOM,
            category = if (isOoc) EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN else EVENT_CATEGORY_RECOM_HOME_PAGE,
            label = if (isOoc) " - $headerName" else "$channelId - $headerName",
            items = arrayListOf(
                productItemDataLayer(
                    index = position,
                    productId = recommendationItem.productId.toString(),
                    productName = recommendationItem.name,
                    price = recommendationItem.price.filter { it.isDigit() },
                    productCategory = recommendationItem.categoryBreadcrumbs
                )
            ),
            productId = recommendationItem.productId.toString(),
        )
        if (isOoc) {
            dataLayer.remove(KEY_PAGE_SOURCE)
            dataLayer.putString(KEY_ITEM_LIST, "{'list': '/tokonow - ${recommendationItem.pageName} - rekomendasi untuk anda - ${recommendationItem.recommendationType} - ${if (recommendationItem.isTopAds) PRODUCT_TOPADS else ""} - ooc'}")
        } else {
            dataLayer.putString(KEY_ITEM_LIST, "{'list': '/tokonow - recomproduct - carousel - ${recommendationItem.recommendationType} - ${recommendationItem.pageName} - $headerName'}")
        }
        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun onImpressProductRecom(channelId: String, headerName: String, recomItems: List<RecommendationItem>, isOoc: Boolean) {
        val items = arrayListOf<Bundle>()
        recomItems.forEachIndexed { position, recomItem ->
            items.add(
                productItemDataLayer(
                    index = position.toString(),
                    productId = recomItem.productId.toString(),
                    productName = recomItem.name,
                    price = recomItem.price.filter { it.isDigit() },
                    productCategory = recomItem.categoryBreadcrumbs
                )
            )
        }

        val dataLayer = getEcommerceDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = if (isOoc) EVENT_ACTION_IMPRESSION_PRODUCT_RECOM_OOC else EVENT_ACTION_IMPRESSION_PRODUCT_RECOM,
            category = if (isOoc) EVENT_CATEGORY_HOME_PAGE_WITHOUT_HYPHEN else EVENT_CATEGORY_RECOM_HOME_PAGE,
            label = if (isOoc) " - $headerName" else "$channelId - $headerName",
            items = items
        )

        if (isOoc) dataLayer.remove(KEY_PAGE_SOURCE)
        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, dataLayer)
    }

    fun onClickProductRecomAddToCart(channelId: String, headerName: String, quantity: String, recommendationItem: RecommendationItem, position: String, cartId: String) {
        val item = productItemDataLayer(
            index = position,
            productId = recommendationItem.productId.toString(),
            productName = recommendationItem.name,
            price = recommendationItem.price.filter { it.isDigit() }
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
            items = arrayListOf(item),
            productId = recommendationItem.productId.toString()
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_ADD_TO_CART, dataLayer)
    }

    fun onImpressRepurchase(data: TokoNowProductCardUiModel, products: List<TokoNowProductCardUiModel>) {
        val productList = arrayListOf<Bundle>().apply {
            products.forEachIndexed { position, item ->
                add(
                    productCardItemDataLayer(
                        position = position.toString(),
                        id = item.productId,
                        name = item.product.productName,
                        price = item.product.formattedPrice,
                        list = LIST_HOME_PAGE_PAST_PURCHASE_WIDGET
                    )
                )
            }
        }

        val eventLabel = getProductCardLabel(data)
        val ecommerceDataLayer = getEcommerceImpressionDataLayer(productList)

        val dataLayer = getProductDataLayer(
            event = EVENT_PRODUCT_VIEW,
            action = EVENT_ACTION_IMPRESSION_PAST_PURCHASE,
            category = EVENT_CATEGORY_HOME_PAGE,
            label = eventLabel,
            ecommerceDataLayer = ecommerceDataLayer
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_PRODUCT_VIEW, dataLayer)
    }

    fun onClickRepurchase(position: Int, data: TokoNowProductCardUiModel) {
        val products = arrayListOf(
            productCardItemDataLayer(
                position = position.toString(),
                id = data.productId,
                name = data.product.productName,
                price = data.product.formattedPrice
            )
        )

        val eventLabel = getProductCardLabel(data)
        val ecommerceDataLayer = getEcommerceClickDataLayer(products)

        val dataLayer = getProductDataLayer(
            event = EVENT_PRODUCT_CLICK,
            action = EVENT_ACTION_CLICK_PAST_PURCHASE,
            category = EVENT_CATEGORY_HOME_PAGE,
            label = eventLabel,
            ecommerceDataLayer = ecommerceDataLayer
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_PRODUCT_CLICK, dataLayer)
    }

    fun onRepurchaseAddToCart(position: Int, quantity: Int, data: TokoNowProductCardUiModel) {
        val products = arrayListOf(
            productCardItemDataLayer(
                position = position.toString(),
                id = data.productId,
                name = data.product.productName,
                price = data.product.formattedPrice
            ).apply {
                putString(KEY_CATEGORY_ID, "")
                putString(KEY_QUANTITY, quantity.toString())
                putString(KEY_SHOP_ID, data.shopId)
                putString(KEY_SHOP_NAME, "")
                putString(KEY_SHOP_TYPE, "")
            }
        )

        val eventLabel = getProductCardLabel(data)
        val ecommerceDataLayer = getEcommerceATCDataLayer(products)

        val dataLayer = getProductDataLayer(
            event = EVENT_ATC,
            action = EVENT_ACTION_ATC_PAST_PURCHASE,
            category = EVENT_CATEGORY_HOME_PAGE,
            label = eventLabel,
            ecommerceDataLayer = ecommerceDataLayer
        )
        getTracker().sendEnhanceEcommerceEvent(EVENT_ATC, dataLayer)
    }

    fun trackImpressionLeftCarousel(channelId: String, headerName: String) {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_LEFT_CAROUSEL,
            label = "$channelId - $headerName"
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_PG_IRIS, dataLayer)
    }

    fun trackClickBannerLeftCarousel(channelId: String, headerName: String) {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_BANNER_LEFT_CAROUSEL,
            label = "$channelId - $headerName"
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_PG, dataLayer)
    }

    fun trackClickViewAllLeftCarousel(channelId: String, headerName: String) {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_VIEW_ALL_LEFT_CAROUSEL,
            label = "$channelId - $headerName"
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_PG, dataLayer)
    }

    fun trackImpressionProductLeftCarousel(position: Int, channelModel: ChannelModel, grid: ChannelGrid) {
        val items = arrayListOf(
            productItemDataLayer(
                index = position.toString(),
                productId = grid.id,
                productName = grid.name,
                price = grid.price,
                productBrand = grid.brandId,
                productCategory = grid.categoryId
            )
        )
        
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_ITEM_LIST,
            action = EVENT_ACTION_IMPRESSION_PRODUCT_LEFT_CAROUSEL,
            label = "${channelModel.id} - ${channelModel.channelHeader.name}"
        ).apply {
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM_LIST, dataLayer)
    }

    fun trackClickProductLeftCarousel(position: Int, channelModel: ChannelModel, grid: ChannelGrid) {
        val headerName = channelModel.channelHeader.name

        val items = arrayListOf(
            productItemDataLayer(
                index = position.toString(),
                productId = grid.id,
                productName = grid.name,
                price = grid.price,
                productBrand = grid.brandId,
                productCategory = grid.categoryId
            )
        )

        val itemList = "$ITEM_LIST_LEFT_CAROUSEL${channelModel.type} - " +
            "${channelModel.pageName} - " + headerName
        
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_PRODUCT_LEFT_CAROUSEL,
            label = "${channelModel.id} - $headerName"
        ).apply {
            putString(KEY_ITEM_LIST, itemList)
            putParcelableArrayList(KEY_ITEMS, items)
            putString(KEY_USER_ID, userSession.userId)
        }


        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackImpressionLego3Banner(channelModel: ChannelModel) {
        val promotions = ArrayList(
            channelModel.channelGrids.mapIndexed { position, channelGrid ->
                ecommerceDataLayerBanner(
                    channelModel = channelModel,
                    channelGrid = channelGrid,
                    position = position
                )
            }
        )

        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_ITEM,
            action = EVENT_ACTION_IMPRESSION_LEGO_3,
            label = "${channelModel.id} - ${channelModel.channelHeader.name}"
        ).apply {
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_ITEM, dataLayer)
    }

    fun trackClickLego3Banner(position: Int, channelModel: ChannelModel, channelGrid: ChannelGrid) {
        val promotions = arrayListOf(
            ecommerceDataLayerBanner(
                channelModel = channelModel,
                channelGrid = channelGrid,
                position = position
            )
        )

        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_SELECT_CONTENT,
            action = EVENT_ACTION_CLICK_LEGO_3,
            label = "${channelModel.id} - ${channelModel.channelHeader.name} - ${channelGrid.name}"
        ).apply {
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_USER_ID, userSession.userId)
        }

        getTracker().sendEnhanceEcommerceEvent(EVENT_SELECT_CONTENT, dataLayer)
    }

    fun trackImpressionQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_PG_IRIS, dataLayer)
    }

    fun trackClickSeeDetailsQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_SEE_DETAILS_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_PG, dataLayer)
    }

    fun trackClickTitleCardQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_TITLE_CARD_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_PG, dataLayer)
    }

    fun trackClickCardQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_QUEST_CARD_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_PG, dataLayer)
    }

    fun trackImpressionFinishedQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_FINISHED_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_PG_IRIS, dataLayer)
    }

    fun trackClickRewardQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_REWARD_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_PG, dataLayer)
    }

    fun trackClickCloseQuestWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_CLOSE_QUEST_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_PG, dataLayer)
    }

    fun trackImpressionUSPWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_USP_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_VIEW_PG_IRIS, dataLayer)
    }

    fun trackClickUSPWidget() {
        val dataLayer = getMarketplaceDataLayer(
            event = EVENT_CLICK_PG,
            action = EVENT_ACTION_CLICK_USP_WIDGET
        )

        getTracker().sendEnhanceEcommerceEvent(EVENT_CLICK_PG, dataLayer)
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

    private fun ecommerceDataLayerBanner(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int): Bundle {
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

    private fun getMarketplaceDataLayer(event: String, action: String, label: String = ""): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, EVENT_CATEGORY_HOME_PAGE)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
        }
    }

    private fun getEcommerceDataLayer(
        event: String,
        action: String,
        category: String,
        label: String = "",
        affinityLabel: String = "",
        promotions: ArrayList<Bundle>
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_AFFINITY_LABEL, affinityLabel)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putParcelableArrayList(KEY_PROMOTIONS, promotions)
            putString(KEY_USER_ID, userSession.userId)
        }
    }

    private fun getEcommerceDataLayer(
        event: String,
        action: String,
        category: String,
        label: String = "",
        items: ArrayList<Bundle>,
        productId: String = ""
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_PAGE_SOURCE, PRODUCT_RECOM_PAGE_SOURCE)
            putString(KEY_USER_ID, userSession.userId)
            putParcelableArrayList(KEY_ITEMS, items)
            if (productId.isNotBlank()) {
                putString(KEY_PRODUCT_ID, productId)
            }
        }
    }

    private fun getProductDataLayer(
        event: String,
        action: String,
        category: String,
        label: String = "",
        affinityLabel: String = "",
        ecommerceDataLayer: Bundle
    ): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(KEY_AFFINITY_LABEL, affinityLabel)
            putString(KEY_BUSINESS_UNIT, BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE)
            putString(KEY_CURRENT_SITE, CURRENT_SITE_TOKOPEDIA_MARKET_PLACE)
            putParcelable(KEY_ECOMMERCE, ecommerceDataLayer)
            putString(KEY_USER_ID, userSession.userId)
        }
    }

    private fun getEcommerceImpressionDataLayer(products: ArrayList<Bundle>): Bundle {
        return Bundle().apply {
            putString(KEY_CURRENCY_CODE, CURRENCY_CODE_IDR)
            putParcelableArrayList(KEY_IMPRESSIONS, products)
        }
    }

    private fun getEcommerceClickDataLayer(products: ArrayList<Bundle>): Bundle {
        val list = Bundle().apply {
            putString(KEY_LIST, LIST_HOME_PAGE_PAST_PURCHASE_WIDGET)
        }

        val click = Bundle().apply {
            putParcelable(KEY_ACTION_FIELD, list)
            putParcelableArrayList(KEY_PRODUCTS, products)
        }

        return Bundle().apply { putParcelable(KEY_CLICK, click) }
    }

    private fun getEcommerceATCDataLayer(products: ArrayList<Bundle>): Bundle {
        val click = Bundle().apply { putParcelableArrayList(KEY_PRODUCTS, products) }
        return Bundle().apply { putParcelable(KEY_ADD, click) }
    }

    private fun productItemDataLayer(index: String, productId: String, productName: String, price: String, productBrand: String = "", productCategory: String = "", productVariant: String = ""): Bundle {
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

    private fun productCardItemDataLayer(position: String, id: String, name: String, price: String, list: String = "", brand: String = "", category: String = "", variant: String = ""): Bundle {
        return Bundle().apply {
            putString(KEY_BRAND, brand)
            putString(KEY_CATEGORY, category)
            putString(KEY_ID, id)

            if(list.isNotEmpty()) {
                putString(KEY_LIST, list)
            }

            putString(KEY_NAME, name)
            putString(KEY_POSITION, position)
            putString(KEY_PRICE, price)
            putString(KEY_VARIANT, variant)
        }
    }

    private fun getTracker(): Analytics {
        return TrackApp.getInstance().gtm
    }

    private fun hitCommonHomeTracker(dataLayer: MutableMap<String, Any>) {
        getTracker().sendGeneralEvent(dataLayer.getHomeGeneralTracker())
    }

    private fun MutableMap<String, Any>.getHomeGeneralTracker(): MutableMap<String, Any> {
        this[TrackAppUtils.EVENT] = EVENT_CLICK_TOKONOW
        this[KEY_CURRENT_SITE] = CURRENT_SITE_TOKOPEDIA_MARKET_PLACE
        this[KEY_BUSINESS_UNIT] = BUSINESS_UNIT_TOKOPEDIA_MARKET_PLACE
        return this
    }

    private fun getHalalLabel(labelGroup: List<LabelGroup>): String {
        val withHalalLabel = labelGroup.firstOrNull { it.title == LABEL_GROUP_HALAL }
        return if(withHalalLabel == null) WITHOUT_HALAL_LABEL else WITH_HALAL_LABEL
    }

    private fun getSlashedPriceLabel(slashedPrice: String?): String {
        return if(slashedPrice.isNullOrEmpty()) NORMAL_PRICE else SLASH_PRICE
    }

    private fun getVariantLabel(parentProductId: String?): String {
        return if(parentProductId.isNullOrEmpty()) WITHOUT_VARIANT else WITH_VARIANT
    }

    private fun getProductCardLabel(data: TokoNowProductCardUiModel): String {
        val halalLabel = getHalalLabel(data.product.labelGroupList)
        val slashedPriceLabel = getSlashedPriceLabel(data.product.slashedPrice)
        val variantLabel = getVariantLabel(data.parentId)
        return "$halalLabel - $slashedPriceLabel - $variantLabel"
    }
}