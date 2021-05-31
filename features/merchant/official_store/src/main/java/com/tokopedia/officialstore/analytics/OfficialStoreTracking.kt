package com.tokopedia.officialstore.analytics

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.officialstore.DynamicChannelIdentifiers
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Banner
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue

/*
OS HOME REVAMP
Data layer docs
https://docs.google.com/spreadsheets/d/19l7m_uveuFB6YLVLqSTaclLFf13qmtCB9jQKVwzE38o/edit?usp=sharing
*/

class OfficialStoreTracking(context: Context) {

    val trackerObj
        get() = tracker
    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    val trackingQueueObj
        get() = trackingQueue
    private val trackingQueue = TrackingQueue(context)

    private var trackingIris = IrisAnalytics.getInstance(context)

    private val EVENT = "event"
    private val EVENT_CATEGORY = "eventCategory"
    private val EVENT_ACTION = "eventAction"
    private val EVENT_LABEL = "eventLabel"

    private val ATTRIBUTION = "attribution"
    private val AFFINITY_LABEL = "affinityLabel"
    private val CATEGORY_ID = "categoryId"
    private val SHOP_ID = "shopId"
    private val ECOMMERCE = "ecommerce"
    private val CLICK = "click"
    private val IMPRESSION = "impression"
    private val ECOMMERCE_IMPRESSIONS = "impressions"
    private val ECOMMERCE_CURRENCY_CODE = "currencyCode"
    private val FIELD_BUSINESS_UNIT = "businessUnit"
    private val FIELD_CURRENT_SITE = "currentSite"
    private val USER_ID = "userId"

    private val CLICK_OS_MICROSITE = "clickOSMicrosite"
    private val PROMO_CLICK = "promoClick"
    private val PROMO_VIEW = "promoView"
    private val CATEGORY = "category"

    private val CAMPAIGN_CODE = "campaignCode"

    private val OS_MICROSITE = "os microsite - "
    private val OS_MICROSITE_SINGLE = "os microsite"

    private val FIELD_PRODUCTS = "products"
    private val FIELD_PRODUCT_NAME = "name"
    private val FIELD_PRODUCT_ID = "id"
    private val FIELD_PRODUCT_PRICE = "price"
    private val FIELD_PRODUCT_BRAND = "brand"
    private val FIELD_PRODUCT_VARIANT = "variant"
    private val FIELD_PRODUCT_CATEGORY = "category"
    private val FIELD_PRODUCT_LIST = "list"
    private val FIELD_PRODUCT_QUANTITY = "quantity"
    private val FIELD_PRODUCT_POSITION = "position"
    private val FIELD_ACTION_FIELD = "actionField"
    private val FIELD_CATEGORY_ID = "category_id"
    private val FIELD_SHOP_ID = "shop_id"
    private val FIELD_SHOP_TYPE = "shop_type"
    private val FIELD_SHOP_NAME = "shop_name"

    private val VALUE_NONE_OTHER = "none / other"
    private val VALUE_NONE = "none"
    private val VALUE_IDR = "IDR"
    private val VALUE_EMPTY = ""
    private val VALUE_NON_LOGIN = ""
    private val VALUE_NON_LOGIN_NEW = "non login"
    private val VALUE_LOGIN_NEW = "login"
    private val VALUE_DYNAMIC_MIX_TOP_CAROUSEL = "dynamic channel top carousel"
    private val VALUE_DYNAMIC_MIX_LEFT_CAROUSEL = "dynamic channel left carousel"
    private val VALUE_BUSINESS_UNIT_DEFAULT = "home & browse"
    private val VALUE_CURRENT_SITE_DEFAULT = "tokopediamarketplace"


    private val EVENT_PRODUCT_VIEW = "productView"
    private val EVENT_PRODUCT_CLICK = "productClick"


    private val PRODUCT_EVENT_ACTION = "product recommendation"

    private val EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID = "recommendation page with product id"
    private val SLASH_OFFICIAL_STORE = "/official-store"
    private val SLASH_OFFICIAL_STORE_WITHCATEGORY = "/official-store/%s"
    private val SKEL_APPLINK = "{&data}"
    private val SKEL_APPLINK_DATA = "&data"

    fun sendScreen(categoryName: String) {
        val screenName = "/official-store/$categoryName"
        val customDimension = HashMap<String, String>()
        // ask requested Dini Praptiwi at 6/11/2019 3:25 PM
        // @mzennis jadi seharusnya pake event
        customDimension["event"] = "openScreen"
        customDimension["cd35"] = "/official-store"
        tracker.sendScreenAuthenticated(screenName, customDimension)
    }

    fun eventClickCategory(categoryPosition: Int, categorySelected: Category) {
        val data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, "$OS_MICROSITE${categorySelected.title}",
                EVENT_ACTION, "$CATEGORY - $CLICK",
                EVENT_LABEL, "$CLICK $CATEGORY",
                ECOMMERCE, DataLayer.mapOf(
                PROMO_CLICK, DataLayer.mapOf(
                        "promotions",DataLayer.listOf(
                            DataLayer.mapOf(
                                    "id", categorySelected.categoryId,
                                    "name", "/official-store/${categorySelected.title} - category navigation",
                                    "position", "$categoryPosition",
                                    "creative", categorySelected.title,
                                    "creative_url", categorySelected.icon,
                                    "promo_id", null,
                                    "promo_code", null
                            )
                        )
                    )
                )
            )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun eventImpressionCategory(categoryName: String, categoryId: String, categoryPosition: Int, imageUrl: String) {
        val data = DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, "$CATEGORY - $IMPRESSION",
                EVENT_LABEL, "$IMPRESSION of $CATEGORY",
                    ECOMMERCE, DataLayer.mapOf(
                        PROMO_VIEW, DataLayer.mapOf(
                        "promotions",DataLayer.listOf(
                        DataLayer.mapOf(
                                "id", categoryId,
                                "name", "/official-store/$categoryName - category navigation",
                                "position", "$categoryPosition",
                                "creative", categoryName,
                                "creative_url", imageUrl,
                                "promo_id", null,
                                "promo_code", null
                        )
                    )
                )
            )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun eventClickBanner(categoryName: String, bannerPosition: Int,
                         bannerItem: com.tokopedia.officialstore.official.data.model.Banner) {
        val data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, "banner - $CLICK",
                EVENT_LABEL, "$CLICK banner",
                CAMPAIGN_CODE, bannerItem.campaignCode,
                ATTRIBUTION, bannerItem.galaxyAttribution,
                AFFINITY_LABEL, bannerItem.persona,
                CATEGORY_ID, bannerItem.categoryPersona,
                SHOP_ID, bannerItem.brandId,
                ECOMMERCE, DataLayer.mapOf(
                PROMO_CLICK, DataLayer.mapOf(
                "promotions",DataLayer.listOf(
                DataLayer.mapOf(
                        "id", bannerItem.bannerId,
                        "name", "/official-store/$categoryName - slider banner",
                        "position", "$bannerPosition",
                        "creative", bannerItem.title,
                        "creative_url", bannerItem.applink,
                        "promo_id", null,
                        "promo_code", null
                        )
                    )
                )
            )
        )
        tracker.sendEnhanceEcommerceEvent(data as HashMap<String, Any>)
    }

    fun eventImpressionBanner(categoryName: String, bannerPosition: Int,
                              bannerItem: com.tokopedia.officialstore.official.data.model.Banner) {
        val data = DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, "banner - $IMPRESSION",
                EVENT_LABEL, "$IMPRESSION of banner",
                ECOMMERCE, DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                "promotions",DataLayer.listOf(
                DataLayer.mapOf(
                        "id", bannerItem.bannerId,
                        "name", "/official-store/$categoryName - slider banner",
                        "position", "$bannerPosition",
                        "creative", bannerItem.title,
                        "creative_url", bannerItem.applink,
                        "promo_id", null,
                        "promo_code", null
                        )
                    )
                )
            )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun eventClickAllBanner(categoryName: String) {
        tracker.sendGeneralEvent(
                TrackAppUtils
                        .gtmData(CLICK_OS_MICROSITE,
                                "$OS_MICROSITE$categoryName",
                                "banner - $CLICK",
                                "$CLICK view all"))
    }

    fun eventClickAllFeaturedBrand(categoryName: String) {
        tracker.sendGeneralEvent(
                TrackAppUtils
                        .gtmData(CLICK_OS_MICROSITE,
                                "$OS_MICROSITE$categoryName",
                                "all brands - $CLICK",
                                "$CLICK view all"))
    }

    fun eventClickFeaturedBrand(
            categoryName: String,
            shopPosition: Int,
            shopName: String,
            url: String,
            additionalInformation: String,
            featuredBrandId: String,
            isLogin: Boolean,
            shopId: String,
            campaignCode: String
    ) {
        val statusLogin = if (isLogin) "login" else "nonlogin"
        val data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, "$CLICK - shop - all brands - $statusLogin",
                EVENT_LABEL, shopId,
                CAMPAIGN_CODE, campaignCode,
                ECOMMERCE, DataLayer.mapOf(
                PROMO_CLICK, DataLayer.mapOf(
                "promotions",DataLayer.listOf(
                DataLayer.mapOf(
                        "id", featuredBrandId,
                        "name", "/official-store/$categoryName - popular brands",
                        "position", "$shopPosition",
                        "creative", "$shopName - $additionalInformation",
                        "creative_url", url,
                        "promo_id", null,
                        "promo_code", null
                        )
                    )
                )
            )
        )
        tracker.sendEnhanceEcommerceEvent(data as HashMap<String, Any>)
    }

    fun eventImpressionFeatureBrand(
            categoryName: String,
            shopPosition: Int,
            shopName: String,
            url: String,
            additionalInformation: String,
            featuredBrandId: String,
            isLogin: Boolean,
            shopId: String
    ) {
        val statusLogin = if (isLogin) "login" else "nonlogin"
        val data = DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, "all brands - $IMPRESSION - $statusLogin",
                EVENT_LABEL, shopId,
                ECOMMERCE, DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                "promotions",DataLayer.listOf(
                DataLayer.mapOf(
                        "id", featuredBrandId,
                        "name", "/official-store/$categoryName - popular brands",
                        "position", "$shopPosition",
                        "creative", "$shopName - $additionalInformation",
                        "creative_url", url,
                        "promo_id", VALUE_NONE_OTHER,
                        "promo_code", VALUE_NONE_OTHER
                        )
                    )
                )
            )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    // region TRACKER OF DYNAMIC CHANNEL (IntelliJ custom code folding purposes, don't remove)
    fun flashSaleActionTextClick(categoryName: String, headerId: Long) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, "clickOSMicrosite",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "flash sale - click",
                EVENT_LABEL, "click view all - $headerId"
        ))
    }

    fun flashSalePDPClick(categoryName: String, headerName: String, position: String, gridData: Grid, campaignId: Int, campaignCode: String) {
        val ecommerceBody = DataLayer.mapOf(
                "click", DataLayer.mapOf(
                "actionField", DataLayer.mapOf("list", "/official-store/$categoryName - flash sale - $campaignId - $headerName"),
                    "products", DataLayer.listOf(DataLayer.mapOf(
                        "name", gridData.name,
                        "id", gridData.id.toString(10),
                        "price", gridData.price,
                        "brand", "none",
                        "category", "",
                        "variant", "none",
                        "list", "/official-store/$categoryName - flash sale - $campaignId - $headerName",
                        "position", position,
                        "attribution", gridData.attribution
                ))
            )
        )

        tracker.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, "productClick",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "flash sale - product click",
                EVENT_LABEL, "click product picture - $headerName",
                CAMPAIGN_CODE, campaignCode,
                ECOMMERCE, ecommerceBody
        ))
    }

    fun flashSaleImpression(categoryName: String, channelData: Channel, campaignId: Int) {
        val headerName = channelData.header?.name ?: ""
        val impressionBody = DataLayer.listOf()

        channelData.grids.forEachIndexed { index, grid ->
            grid.run {
                impressionBody.add(DataLayer.mapOf(
                        "name", name,
                        "id", id.toString(10),
                        "price", price,
                        "brand", "none",
                        "category", "",
                        "variant", "none",
                        "list", "/official-store/$categoryName - flash sale - $campaignId - $headerName",
                        "position", (index + 1).toString(10),
                        "attribution", attribution
                ))
            }
        }

        trackingQueue.putEETracking(DataLayer.mapOf(
                EVENT, "productView",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "flash sale - product impression",
                EVENT_LABEL, "impression of product - $headerName",
                ECOMMERCE, DataLayer.mapOf(
                    "currencyCode", "IDR",
                    "impressions", impressionBody
            )
        ) as HashMap<String, Any>)
    }

    fun dynamicChannelHomeComponentClick(categoryName: String, headerName: String, position: String, channelGrid: ChannelGrid, channelModel: ChannelModel) {
        val ecommerceBody = DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                    "promotions", DataLayer.listOf(DataLayer.mapOf(
                        "id", channelGrid.id,
                        "name", "/official-store/$categoryName - dynamic channel - $headerName",
                        "position", position,
                        "creative", channelGrid.attribution,
                        "creative_url", channelGrid.applink,
                        "promo_id", null,
                        "promo_code", null
                ))
            )
        )

        val trackingAttributionModel = channelModel.trackingAttributionModel
        tracker.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, "promoClick",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "dynamic channel - click",
                EVENT_LABEL, "click dynamic channel - $headerName",
                ATTRIBUTION, trackingAttributionModel.galaxyAttribution,
                AFFINITY_LABEL, trackingAttributionModel.persona,
                CATEGORY_ID, trackingAttributionModel.categoryPersona,
                SHOP_ID, trackingAttributionModel.brandId,
                CAMPAIGN_CODE, trackingAttributionModel.campaignCode,
                ECOMMERCE, ecommerceBody
        ))
    }

    fun dynamicChannelImageClick(categoryName: String, headerName: String, position: String, gridData: Grid, channelData: Channel) {
        val ecommerceBody = DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                "promotions", DataLayer.listOf(DataLayer.mapOf(
                "id", gridData.id.toString(10),
                "name", "/official-store/$categoryName - dynamic channel - $headerName",
                "position", position,
                "creative", gridData.attribution,
                "creative_url", gridData.applink,
                "promo_id", null,
                "promo_code", null
        ))
        )
        )

        tracker.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, "promoClick",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "dynamic channel - click",
                EVENT_LABEL, "click dynamic channel - $headerName",
                ATTRIBUTION, channelData.galaxyAttribution,
                AFFINITY_LABEL, channelData.persona,
                CATEGORY_ID, channelData.categoryPersona,
                SHOP_ID, channelData.brandId,
                CAMPAIGN_CODE, channelData.campaignCode,
                ECOMMERCE, ecommerceBody
        ))
    }

    fun dynamicChannelImpression(categoryName: String, channelData: Channel) {
        val headerName = channelData.header?.name ?: ""
        val promotionBody = getDynamicChannelImpressionPromotion(categoryName, channelData, "dynamic channel", headerName)

        trackingQueue.putEETracking(DataLayer.mapOf(
                EVENT, "promoView",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "dynamic channel - impression",
                EVENT_LABEL, "impression of dynamic channel - $headerName",
                ECOMMERCE, DataLayer.mapOf(
                    "promoView", DataLayer.mapOf(
                        "promotions", promotionBody
                )
            )
        ) as HashMap<String, Any>)
    }

    fun dynamicChannelHomeComponentImpression(categoryName: String, channelModel: ChannelModel) {
        val headerName = channelModel.channelHeader.name ?: ""
        val promotionBody = getHomeComponentImpressionPromotion(categoryName, channelModel, "dynamic channel", headerName)

        trackingQueue.putEETracking(DataLayer.mapOf(
                EVENT, "promoView",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "dynamic channel - impression",
                EVENT_LABEL, "impression of dynamic channel - $headerName",
                ECOMMERCE, DataLayer.mapOf(
                "promoView", DataLayer.mapOf(
                "promotions", promotionBody
        )
        )
        ) as HashMap<String, Any>)
    }

    fun dynamicChannelMixCardClick(categoryName: String, headerName: String, position: String, gridData: Grid, campaignCode: String, campaignId: String) {
        val ecommerceBody = DataLayer.mapOf(
                "click", DataLayer.mapOf(
                    "actionField", DataLayer.mapOf("list", "/official-store/$categoryName - dynamic channel mix - $headerName"),
                    "products", DataLayer.listOf(DataLayer.mapOf(
                        "name", gridData.name,
                        "id", gridData.id.toString(10),
                        "price", gridData.price,
                        "brand", "none",
                        "category", "",
                        "variant", "none",
                        "list", "/official-store/$categoryName - dynamic channel mix - ${gridData.id} - $campaignId - $headerName",
                        "position", position,
                        "attribution", gridData.attribution
                ))
            )
        )

        tracker.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, "productClick",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "dynamic channel mix - product click",
                EVENT_LABEL, "click product picture - $headerName",
                CAMPAIGN_CODE, campaignCode.toString(),
                ECOMMERCE, ecommerceBody
        ))
    }

    fun dynamicChannelMixCardImpression(categoryName: String, channelData: Channel) {
        val headerName = channelData.header?.name ?: ""
        val impressionBody = DataLayer.listOf()

        channelData.grids?.forEachIndexed { index, grid ->
            grid?.run {
                impressionBody.add(DataLayer.mapOf(
                        "name", name,
                        "id", id.toString(10),
                        "price", price,
                        "brand", "none",
                        "category", "",
                        "variant", "none",
                        "list", "/official-store/$categoryName - dynamic channel mix - $headerName",
                        "position", (index + 1).toString(10),
                        "attribution", attribution
                ))
            }
        }

        trackingQueue.putEETracking(DataLayer.mapOf(
                EVENT, "productView",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "dynamic channel mix - product impression",
                EVENT_LABEL, "impression of product - $headerName",
                ECOMMERCE, DataLayer.mapOf(
                    "currencyCode", "IDR",
                    "impressions", impressionBody
            )
        ) as HashMap<String, Any>)
    }

    fun dynamicChannelMixBannerClick(categoryName: String, headerName: String, bannerData: Banner, channelData: Channel) {
        val ecommerceBody = DataLayer.mapOf(
                "promoClick", DataLayer.mapOf(
                    "promotions", DataLayer.listOf(DataLayer.mapOf(
                        "id", bannerData.id.toString(10),
                        "name", "/official-store/$categoryName - dynamic channel mix - $headerName",
                        "position", "0",
                        "creative", bannerData.attribution,
                        "creative_url", bannerData.applink,
                        "promo_id", null,
                        "promo_code", null
                ))
            )
        )

        tracker.sendEnhanceEcommerceEvent(DataLayer.mapOf(
                EVENT, "promoClick",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "dynamic channel mix - banner click",
                EVENT_LABEL, "click banner dc mix - ${bannerData.applink}",
                CAMPAIGN_CODE, channelData.campaignCode,
                ATTRIBUTION, channelData.galaxyAttribution,
                AFFINITY_LABEL, channelData.persona,
                CATEGORY_ID, channelData.categoryPersona,
                SHOP_ID, channelData.brandId,
                ECOMMERCE, ecommerceBody
        ))
    }

    fun dynamicChannelMixBannerImpression(categoryName: String, channelData: Channel) {
        val headerName = channelData.header?.name ?: ""
        val promotionBody = getDynamicChannelImpressionPromotion(categoryName, channelData, "dynamic channel mix", headerName)

        trackingQueue.putEETracking(DataLayer.mapOf(
                EVENT, "promoView",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "dynamic channel mix - banner impression",
                EVENT_LABEL, "impression banner dc mix - $headerName",
                ECOMMERCE, DataLayer.mapOf(
                    "promoView", DataLayer.mapOf(
                        "promotions", promotionBody
                )
            )
        ) as HashMap<String, Any>)
    }
    // endregion TRACKER OF DYNAMIC CHANNEL (IntelliJ custom code folding purposes, don't remove)

    // No 21
    fun eventClickProductRecommendation(
            item: RecommendationItem,
            position: String,
            recommendationTitle: String,
            isLogin: Boolean,
            categoryName: String
    ) {
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName", // Here
                EVENT_ACTION, "click - $PRODUCT_EVENT_ACTION",
                EVENT_LABEL, recommendationTitle,
                ECOMMERCE, DataLayer.mapOf(
                CLICK, DataLayer.mapOf(
                FIELD_ACTION_FIELD, DataLayer.mapOf(
                    FIELD_PRODUCT_LIST, getListProductClickInsideActionField(categoryName, item.recommendationType)
                ),
                FIELD_PRODUCTS, DataLayer.listOf(
                convertRecommendationItemToDataImpressionObject(item, isLogin, position)
        ))))
        tracker.sendEnhanceEcommerceEvent(data)
    }

    // No 22
    fun eventImpressionProductRecommendation(
            item: RecommendationItem,
            isLogin: Boolean,
            categoryName: String,
            recommendationTitle: String,
            position: String) {
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, String.format(OS_MICROSITE, categoryName),
                EVENT_ACTION, "impression - $PRODUCT_EVENT_ACTION",
                EVENT_LABEL, recommendationTitle,
                ECOMMERCE, DataLayer.mapOf(
                ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                convertRecommendationItemToDataImpressionObject(item, isLogin, position)
        )))
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    private fun getDynamicChannelImpressionPromotion(categoryName: String, channelData: Channel,
                                                     channelType: String, headerName: String): List<Any> {
        val promotionBody: MutableList<Any> = DataLayer.listOf()
        channelData.grids?.forEachIndexed { index, grid ->
            grid?.run {
                promotionBody.add(DataLayer.mapOf(
                        "id", id.toString(10),
                        "name", "/official-store/$categoryName - $channelType - $headerName",
                        "position", (index + 1).toString(10),
                        "creative", attribution,
                        "creative_url", applink,
                        "promo_id", null,
                        "promo_code", null
                ))
            }
        }
        return promotionBody
    }

    private fun getHomeComponentImpressionPromotion(categoryName: String, channelModel: ChannelModel,
                                                     channelType: String, headerName: String): List<Any> {
        val promotionBody: MutableList<Any> = DataLayer.listOf()
        channelModel.channelGrids?.forEachIndexed { index, grid ->
            grid?.run {
                promotionBody.add(DataLayer.mapOf(
                        "id", id,
                        "name", "/official-store/$categoryName - $channelType - $headerName",
                        "position", (index + 1).toString(10),
                        "creative", attribution,
                        "creative_url", applink,
                        "promo_id", null,
                        "promo_code", null
                ))
            }
        }
        return promotionBody
    }

    private fun convertRecommendationItemToDataImpressionObject(item: RecommendationItem, isLogin: Boolean, position: String): Any {
        return DataLayer.mapOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId.toString(),
                FIELD_PRODUCT_PRICE, item.priceInt.toString(),
                FIELD_PRODUCT_BRAND, item.shopName,
                FIELD_PRODUCT_CATEGORY, item.categoryBreadcrumbs,
                FIELD_PRODUCT_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_LIST, getListProductInsideProductField(item.recommendationType, item.isTopAds, isLogin),
                FIELD_PRODUCT_POSITION, position,
                ATTRIBUTION, VALUE_NONE_OTHER
        )
    }

    private fun getListProductClickInsideActionField(categoryName: String, recommendationType: String): String {
        return "/official-store/$categoryName - rekomendasi untuk anda - $recommendationType"
    }

    private fun getListProductInsideProductField(recommendationType: String, isTopAds: Boolean, isLogin: Boolean): String {
        val stringTopAds = if (isTopAds) " - product topads" else ""
        val stringIsLogin = if (isLogin) "" else " - non login"
        return "/official-store$stringIsLogin - rekomendasi untuk anda - $recommendationType$stringTopAds"
    }

    fun eventClickWishlist(categoryName: String, isAddWishlist: Boolean, isLogin: Boolean, productId: Int, isTopAds: Boolean) {
        val action = if (isAddWishlist) "add" else "remove"
        val statusTopads = if (isTopAds) "topads" else  "general"
        var eventAction = "$action wishlist - product recommendation - ${if (isLogin) "login" else "non login"}"
        val eventLabel = "$productId - $statusTopads"

        tracker.sendGeneralEvent(
                TrackAppUtils
                        .gtmData(CLICK_OS_MICROSITE,
                                "$OS_MICROSITE$categoryName",
                                eventAction,
                                eventLabel))
    }

    fun sendAll() {
        trackingQueue.sendAll()
    }

    fun flashSaleCardImpression(
            categoryName: String,
            channel: Channel,
            productItem: Grid,
            productPosition: String,
            isLogin: Boolean
    ) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelIdentifiers.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelIdentifiers.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val eventAction = "$IMPRESSION on product $valueDynamicMix"
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, eventAction,
                EVENT_LABEL, channel.id,
                ECOMMERCE, DataLayer.mapOf(
                        ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                        ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                            createFlashSaleCardProductItemMap(
                                    productItem,
                                    productPosition,
                                    isLogin,
                                    valueDynamicMix
                            )
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun flashSaleCardImpressionComponent(
            categoryName: String,
            channel: ChannelModel,
            productItem: ChannelGrid,
            productPosition: String,
            isLogin: Boolean,
            userId: String
    ) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelIdentifiers.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelIdentifiers.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val eventAction = "$IMPRESSION on product $valueDynamicMix"
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, OS_MICROSITE_SINGLE,
                EVENT_ACTION, eventAction,
                EVENT_LABEL, channel.id + " - " + categoryName,
                FIELD_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_DEFAULT,
                FIELD_CURRENT_SITE, VALUE_CURRENT_SITE_DEFAULT,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                            createFlashSaleCardProductItemMapComponent(
                                    productItem,
                                    productPosition,
                                    isLogin,
                                    valueDynamicMix
                        )
                    )
                )
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    private fun createFlashSaleCardProductItemMap(
            gridData: Grid,
            position: String,
            isLogin: Boolean,
            valueDynamicMix: String
    ): MutableMap<String, Any> {
        val list = mutableListOf(SLASH_OFFICIAL_STORE)
        if (!isLogin)
            list.add(VALUE_NON_LOGIN)
        if (valueDynamicMix.isNotEmpty())
            list.add(valueDynamicMix)
        val listKeyValue = TextUtils.join(" - ", list)
        return DataLayer.mapOf(
                FIELD_PRODUCT_NAME, gridData.name,
                FIELD_PRODUCT_ID, gridData.id.toString(),
                FIELD_PRODUCT_PRICE, formatPrice(gridData.price),
                FIELD_PRODUCT_BRAND, VALUE_NONE,
                FIELD_PRODUCT_CATEGORY, VALUE_NONE,
                FIELD_PRODUCT_VARIANT, VALUE_NONE,
                FIELD_PRODUCT_LIST, listKeyValue,
                FIELD_PRODUCT_POSITION, position
        )
    }

    private fun createFlashSaleCardProductItemMapComponent(
            gridData: ChannelGrid,
            position: String,
            isLogin: Boolean,
            valueDynamicMix: String
    ): MutableMap<String, Any> {
        val list = mutableListOf(SLASH_OFFICIAL_STORE)
        if (!isLogin)
            list.add(VALUE_NON_LOGIN)
        if (valueDynamicMix.isNotEmpty())
            list.add(valueDynamicMix)
        val listKeyValue = TextUtils.join(" - ", list)
        return DataLayer.mapOf(
                FIELD_PRODUCT_NAME, gridData.name,
                FIELD_PRODUCT_ID, gridData.id.toString(),
                FIELD_PRODUCT_PRICE, formatPrice(gridData.price),
                FIELD_PRODUCT_BRAND, VALUE_NONE,
                FIELD_PRODUCT_CATEGORY, VALUE_NONE,
                FIELD_PRODUCT_VARIANT, VALUE_NONE,
                FIELD_PRODUCT_LIST, listKeyValue,
                FIELD_PRODUCT_POSITION, position
        )
    }

    fun flashSaleCardClicked(
            categoryName: String,
            channel: Channel,
            productItem: Grid,
            productPosition: String,
            isLogin: Boolean
    ) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelIdentifiers.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelIdentifiers.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val list = mutableListOf(SLASH_OFFICIAL_STORE)
        if (!isLogin)
            list.add(VALUE_NON_LOGIN)
        if (valueDynamicMix.isNotEmpty())
            list.add(valueDynamicMix)
        val listKeyValue = TextUtils.join(" - ", list)
        val eventAction = "$CLICK on product $valueDynamicMix"
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, eventAction,
                EVENT_LABEL, channel.id,
                CAMPAIGN_CODE, channel.campaignCode,
                ECOMMERCE, DataLayer.mapOf(
                        CLICK , DataLayer.mapOf(
                            FIELD_ACTION_FIELD , DataLayer.mapOf( FIELD_PRODUCT_LIST , listKeyValue),
                            FIELD_PRODUCTS, DataLayer.listOf(
                                createFlashSaleCardProductItemMap(
                                        productItem,
                                        productPosition,
                                        isLogin,
                                        valueDynamicMix
                                )
                            )
                        )
                )
        )
        tracker.sendEnhanceEcommerceEvent(data as HashMap<String, Any>)
    }

    fun flashSaleCardClickedComponent(
            categoryName: String,
            channel: ChannelModel,
            productItem: ChannelGrid,
            productPosition: Int,
            isLogin: Boolean,
            userId: String
    ) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelIdentifiers.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelIdentifiers.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val list = mutableListOf(String.format(SLASH_OFFICIAL_STORE_WITHCATEGORY, categoryName))
        if (valueDynamicMix.isNotEmpty())
            list.add(valueDynamicMix)
        list.add(channel.trackingAttributionModel.campaignId)
        if (!isLogin)
            list.add(VALUE_NON_LOGIN_NEW)
        else list.add(VALUE_LOGIN_NEW)
        val listKeyValue = TextUtils.join(" - ", list)
        val eventAction = "$CLICK on product $valueDynamicMix"
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, OS_MICROSITE_SINGLE,
                EVENT_ACTION, eventAction,
                EVENT_LABEL, channel.id + " - " + categoryName,
                FIELD_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_DEFAULT,
                FIELD_CURRENT_SITE, VALUE_CURRENT_SITE_DEFAULT,
                USER_ID, userId,
                CAMPAIGN_CODE, channel.trackingAttributionModel.campaignCode,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK , DataLayer.mapOf(
                        FIELD_ACTION_FIELD , DataLayer.mapOf( FIELD_PRODUCT_LIST , listKeyValue),
                        FIELD_PRODUCTS, DataLayer.listOf(
                            createFlashSaleCardProductItemMapComponent(productItem,
                                    productPosition.toString(),
                                    isLogin,
                                    valueDynamicMix
                            )
                        )
                    )
                )
        )
        tracker.sendEnhanceEcommerceEvent(data as HashMap<String, Any>)
    }

    fun seeAllMixFlashSaleClicked(categoryName: String, channel: Channel) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelIdentifiers.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelIdentifiers.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val eventActionValue = "click view all on $valueDynamicMix"
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_OS_MICROSITE,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, eventActionValue,
                EVENT_LABEL, channel.id
        ))
    }

    fun seeAllMixFlashSaleClickedComponent(categoryName: String, channel: ChannelModel) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelIdentifiers.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelIdentifiers.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val eventActionValue = "click view all card on $valueDynamicMix"
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_OS_MICROSITE,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, eventActionValue,
                EVENT_LABEL, channel.id + " - " + channel.channelHeader.name
        ))
    }

    fun seeAllBannerFlashSaleClickedComponent(categoryName: String, channel: ChannelModel) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelIdentifiers.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelIdentifiers.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val eventActionValue = "click view all on $valueDynamicMix"
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_OS_MICROSITE,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, eventActionValue,
                EVENT_LABEL, channel.id + " - " + channel.channelHeader.name
        ))
    }

    fun mixTopBannerCtaButtonClicked(categoryName: String, buttonName: String, channelId: String) {
        val eventActionValue = "$CLICK $buttonName on $VALUE_DYNAMIC_MIX_TOP_CAROUSEL"
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_OS_MICROSITE,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, eventActionValue,
                EVENT_LABEL, channelId
        ))
    }

    private fun formatPrice(price: String): String? {
        return if (!TextUtils.isEmpty(price)) {
            price.replace("[^\\d]".toRegex(), "")
        } else {
            ""
        }
    }

    // No 31
    fun eventClickMixLeftImageBanner(channel: Channel, categoryName: String, bannerPosition: Int) {
        val eventDataLayer = Bundle()
        eventDataLayer.putParcelableArrayList("promotions", createMixLeftEcommerceDataLayer(
                channelId = channel.id,
                categoryName = categoryName.toLowerCase(),
                headerName = channel.header?.name.orEmpty(),
                bannerPosition = bannerPosition,
                creative = channel.name,
                creativeUrl = channel.banner?.applink.orEmpty()
        ))
        eventDataLayer.putString(EVENT_CATEGORY, "${OS_MICROSITE}$categoryName")
        eventDataLayer.putString(AFFINITY_LABEL, channel.persona)
        eventDataLayer.putString(ATTRIBUTION, channel.galaxyAttribution)
        eventDataLayer.putString(SHOP_ID, channel.brandId)
        eventDataLayer.putString(EVENT, PROMO_CLICK)
        eventDataLayer.putString(EVENT_ACTION, "$CLICK banner $VALUE_DYNAMIC_MIX_LEFT_CAROUSEL")
        eventDataLayer.putString(EVENT_LABEL, channel.id)
        eventDataLayer.putString(CATEGORY_ID, channel.categoryPersona)
        eventDataLayer.putString(CAMPAIGN_CODE, "${channel.campaignCode.orEmpty()}")

        tracker.sendEnhanceEcommerceEvent("select_content", eventDataLayer)
    }

    // No 32
    fun eventImpressionMixLeftImageBanner(channel: Channel, categoryName: String, bannerPosition: Int) {
        val eventDataLayer = Bundle()
        eventDataLayer.putString(EVENT, "view_item")
        eventDataLayer.putString(EVENT_CATEGORY, "${OS_MICROSITE}$categoryName")
        eventDataLayer.putString(EVENT_ACTION, "$IMPRESSION banner $VALUE_DYNAMIC_MIX_LEFT_CAROUSEL")
        eventDataLayer.putString(EVENT_LABEL, channel.id)
        eventDataLayer.putParcelableArrayList("promotions", createMixLeftEcommerceDataLayer(
                channelId = channel.id,
                categoryName = categoryName.toLowerCase(),
                headerName = channel.header?.name.orEmpty(),
                bannerPosition = bannerPosition,
                creative = channel.name,
                creativeUrl = channel.banner?.applink.orEmpty()
        ))

        tracker.sendEnhanceEcommerceEvent("view_item", eventDataLayer)
    }

    private fun createMixLeftEcommerceDataLayer(channelId: String, categoryName: String, headerName: String, bannerPosition: Int, creative: String, creativeUrl: String): ArrayList<Bundle> {
        val promotion = Bundle()
        promotion.putString("item_id", channelId)
        promotion.putString("item_name", arrayOf("$SLASH_OFFICIAL_STORE/$categoryName", VALUE_DYNAMIC_MIX_LEFT_CAROUSEL, headerName, SKEL_APPLINK.replace(SKEL_APPLINK_DATA, creativeUrl)).joinToString(" - "))
        promotion.putString("creative_slot", "$bannerPosition")
        promotion.putString("creative_name", creative)
        promotion.putString("creative_url", creativeUrl)
        return arrayListOf(promotion)
    }
}