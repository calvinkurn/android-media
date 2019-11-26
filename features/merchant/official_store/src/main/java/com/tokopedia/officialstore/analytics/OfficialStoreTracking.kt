package com.tokopedia.officialstore.analytics

import android.content.Context
import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Banner
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.*

/*
OS HOME REVAMP
Data layer docs
https://docs.google.com/spreadsheets/d/19l7m_uveuFB6YLVLqSTaclLFf13qmtCB9jQKVwzE38o/edit?usp=sharing
*/

class OfficialStoreTracking(context: Context) {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }
    private var trackingQueue = TrackingQueue(context)

    private val EVENT = "event"
    private val EVENT_CATEGORY = "eventCategory"
    private val EVENT_ACTION = "eventAction"
    private val EVENT_LABEL = "eventLabel"

    private val ECOMMERCE = "ecommerce"
    private val CLICK = "click"
    private val IMPRESSION = "impression"
    private val ECOMMERCE_IMPRESSIONS = "impressions"
    private val ECOMMERCE_CURRENCY_CODE = "currencyCode"

    private val CLICK_OS_MICROSITE = "clickOSMicrosite"
    private val PROMO_CLICK = "promoClick"
    private val PROMO_VIEW = "promoView"
    private val CATEGORY = "category"

    private val OS_MICROSITE = "os microsite - "

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
    private val VALUE_IDR = "IDR"
    private val VALUE_EMPTY = ""

    private val EVENT_PRODUCT_VIEW = "productView"
    private val EVENT_PRODUCT_CLICK = "productClick"
    private val ATTRIBUTION = "attribution"


    private val PRODUCT_EVENT_ACTION = "product recommendation"

    private val EVENT_CATEGORY_RECOMMENDATION_PAGE_WITH_PRODUCT_ID = "recommendation page with product id"

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
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun eventImpressionBanner(categoryName: String, bannerPosition: Int,
                              bannerItem: com.tokopedia.officialstore.official.data.model.Banner?) {
        val data = DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, "banner - $IMPRESSION",
                EVENT_LABEL, "$IMPRESSION of banner",
                ECOMMERCE, DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                "promotions",DataLayer.listOf(
                DataLayer.mapOf(
                        "id", bannerItem?.bannerId,
                        "name", "/official-store/$categoryName - slider banner",
                        "position", "$bannerPosition",
                        "creative", bannerItem?.title,
                        "creative_url", bannerItem?.applink,
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

    fun eventClickFeaturedBrand(categoryName: String, shopId: String, shopPosition: Int, shopName: String, url: String, additionalInformation: String) {
        val data = DataLayer.mapOf(
                EVENT, PROMO_CLICK,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, "all brands - $CLICK",
                EVENT_LABEL, "$CLICK shop logo",
                ECOMMERCE, DataLayer.mapOf(
                PROMO_CLICK, DataLayer.mapOf(
                "promotions",DataLayer.listOf(
                DataLayer.mapOf(
                        "id", shopId,
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
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    fun eventImpressionFeatureBrand(categoryName: String, shopId: String, shopPosition: Int, shopName: String, url: String, additionalInformation: String) {
        val data = DataLayer.mapOf(
                EVENT, PROMO_VIEW,
                EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
                EVENT_ACTION, "all brands - $IMPRESSION",
                EVENT_LABEL, "$IMPRESSION of brand",
                ECOMMERCE, DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                "promotions",DataLayer.listOf(
                DataLayer.mapOf(
                        "id", shopId,
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
    fun flashSaleActionTextClick(categoryName: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, "clickOSMicrosite",
                EVENT_CATEGORY, "os microsite - $categoryName",
                EVENT_ACTION, "flash sale - click",
                EVENT_LABEL, "click view all"
        ))
    }

    fun flashSalePDPClick(categoryName: String, headerName: String, position: String, gridData: Grid) {
        val ecommerceBody = DataLayer.mapOf(
                "click", DataLayer.mapOf(
                    "actionField", DataLayer.mapOf("list", "/official-store/$categoryName - flash sale - $headerName"),
                    "products", DataLayer.listOf(DataLayer.mapOf(
                        "name", gridData.name,
                        "id", gridData.id.toString(10),
                        "price", gridData.price,
                        "brand", "none",
                        "category", "",
                        "variant", "none",
                        "list", "/official-store/$categoryName - flash sale - $headerName",
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
                ECOMMERCE, ecommerceBody
        ))
    }

    fun flashSaleImpression(categoryName: String, channelData: Channel) {
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
                        "list", "/official-store/$categoryName - flash sale - $headerName",
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

    fun dynamicChannelImageClick(categoryName: String, headerName: String, position: String, gridData: Grid) {
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

    fun dynamicChannelMixCardClick(categoryName: String, headerName: String, position: String, gridData: Grid) {
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
                        "list", "/official-store/$categoryName - dynamic channel mix - $headerName",
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

    fun dynamicChannelMixBannerClick(categoryName: String, headerName: String, bannerData: Banner) {
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
                EVENT_LABEL, "click banner dc mix - $headerName",
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
                FIELD_PRODUCT_LIST, getListProductClickInsideActionField(categoryName, item.recommendationType),
                FIELD_PRODUCTS, DataLayer.listOf(
                convertRecommendationItemToDataImpressionObject(item, isLogin, position)
        )))))
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

    private fun convertRecommendationItemToDataImpressionObject(item: RecommendationItem, isLogin: Boolean, position: String): Any {
        return DataLayer.mapOf(
                FIELD_PRODUCT_NAME, item.name,
                FIELD_PRODUCT_ID, item.productId.toString(),
                FIELD_PRODUCT_PRICE, item.getPriceIntFromString(),
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

    fun eventClickWishlist(categoryName: String, isAddWishlist: Boolean, isLogin: Boolean, recommendationTitle: String) {
        val action = if (isAddWishlist) "remove" else  "add"
        var eventAction = "$CLICK $action - wishlist on product recommendation"
        if (!isLogin)
            eventAction += " - non login"

        tracker.sendGeneralEvent(
                TrackAppUtils
                        .gtmData(CLICK_OS_MICROSITE,
                                "$OS_MICROSITE$categoryName",
                                eventAction,
                                recommendationTitle))
    }

    fun sendAll() {
        trackingQueue.sendAll()
    }
}