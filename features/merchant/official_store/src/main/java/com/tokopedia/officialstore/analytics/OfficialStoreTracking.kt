package com.tokopedia.officialstore.analytics

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.analytic_constant.Event
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.iris.IrisAnalytics
import com.tokopedia.officialstore.category.data.model.Category
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Banner
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Promotion.Companion.ITEM_ID
import com.tokopedia.track.builder.util.BaseTrackerConst.Promotion.Companion.ITEM_NAME
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

    companion object{
        private const val EVENT = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"
        private const val EVENT_ATTRIBUTION = "attribution"
        private const val EVENT_LABEL_POPULAR_BRANDS = "%s - %s - %s"
        private const val IMPRESSION_BANNER = "impression banner"
        private const val EVENT_POPULAR_BRANDS = "%s - %s"
        private const val PROMOTIONS_ID_POPULAR_BRANDS = "%s_%s"
        private const val PROMOTIONS = "promotions"
        private const val PROMOTIONS_NAME_POPULAR_BRANDS = "%s%s - %s - %s"
        private const val CLICK_BANNER = "click banner"
        private const val ATTRIBUTION = "attribution"
        private const val AFFINITY_LABEL = "affinityLabel"
        private const val CATEGORY_ID = "categoryId"
        private const val SHOP_ID = "shopId"
        private const val ECOMMERCE = "ecommerce"
        private const val CLICK = "click"
        private const val IMPRESSION = "impression"
        private const val ECOMMERCE_IMPRESSIONS = "impressions"
        private const val ECOMMERCE_CURRENCY_CODE = "currencyCode"
        private const val FIELD_BUSINESS_UNIT = "businessUnit"
        private const val FIELD_CURRENT_SITE = "currentSite"
        private const val USER_ID = "userId"
        private const val REFERENCE_POPULAR_BRANDS = "reference: row 18"
        private const val CLICK_HOMEPAGE = "clickHomepage"
        private const val CLICK_OS_MICROSITE = "clickOSMicrosite"
        private const val PROMO_CLICK = "promoClick"
        private const val PROMO_VIEW = "promoView"
        private const val CATEGORY = "category"
        private const val CAMPAIGN_CODE = "campaignCode"
        private const val OS_MICROSITE = "os microsite - "
        private const val ALL_BRANDS = "all brands"
        private const val VIEW_ALL = "view all"
        private const val FIELD_PRODUCTS = "products"
        private const val FIELD_PRODUCT_NAME = "name"
        private const val FIELD_PRODUCT_ID = "id"
        private const val FIELD_PRODUCT_PRICE = "price"
        private const val FIELD_PRODUCT_BRAND = "brand"
        private const val FIELD_PRODUCT_VARIANT = "variant"
        private const val FIELD_PRODUCT_CATEGORY = "category"
        private const val FIELD_PRODUCT_LIST = "list"
        private const val FIELD_PRODUCT_POSITION = "position"
        private const val FIELD_ACTION_FIELD = "actionField"
        private const val FIELD_DIMENSION_38 = "dimension38"
        private const val FIELD_PRODUCT_CREATIVE = "creative"
        private const val VALUE_NONE_OTHER = "none / other"
        private const val VALUE_NONE = "none"
        private const val VALUE_IDR = "IDR"
        private const val VALUE_NON_LOGIN = ""
        private const val VALUE_NON_LOGIN_NEW = "non login"
        private const val VALUE_LOGIN_NEW = "login"
        private const val VALUE_DYNAMIC_MIX_TOP_CAROUSEL = "dynamic channel top carousel"
        private const val VALUE_DYNAMIC_MIX_LEFT_CAROUSEL = "dynamic channel left carousel"
        private const val VALUE_BUSINESS_UNIT_DEFAULT = "home & browse"
        private const val VALUE_CURRENT_SITE_DEFAULT = "tokopediamarketplace"
        private const val EVENT_PRODUCT_VIEW = "productView"
        private const val EVENT_PRODUCT_CLICK = "productClick"
        private const val PRODUCT_EVENT_ACTION = "product recommendation"
        private const val SLASH_OFFICIAL_STORE = "/official-store"
        private const val SLASH_OFFICIAL_STORE_WITHOUT_CATEGORY = "/official-store/"
        private const val SKEL_APPLINK = "{&data}"
        private const val SKEL_APPLINK_DATA = "&data"
        private const val POPULAR_BRANDS = "popular brands"
        private const val RADIX_10 = 10
        const val OS_MICROSITE_SINGLE = "os microsite"

        const val FORMAT_DASH_TWO_VALUES = "%s - %s"
        const val FORMAT_DASH_THREE_VALUES = "%s - %s - %s"
        const val FORMAT_DASH_FOUR_VALUES = "%s - %s - %s - %s"
        const val FORMAT_UNDERSCORE_TWO_VALUES = "%s_%s"
        const val FORMAT_ITEM_NAME = "${SLASH_OFFICIAL_STORE}/%s - %s"
        const val FORMAT_ITEM_NAME_FOUR_VALUES = "${SLASH_OFFICIAL_STORE}/%s - %s - %s - %s"
        const val FORMAT_ITEM_LIST = "${SLASH_OFFICIAL_STORE}/%s - %s - %s"
        const val FORMAT_CLICK_VIEW_ALL = "click view all on %s"
        const val FORMAT_CLICK_VIEW_ALL_CARD = "click view all card on %s"
        private const val VALUE_SLIDER_BANNER = "slider banner"
        private const val VALUE_PRODUCT_IMPRESSION = "$IMPRESSION product"
        private const val VALUE_PRODUCT_CLICK = "$CLICK product"
    }

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
                         bannerItem: com.tokopedia.officialstore.official.data.model.Banner, userId: String) {
        val bundle = Bundle().apply {
            putString(EVENT, Event.SELECT_CONTENT)
            putString(EVENT_CATEGORY, OS_MICROSITE_SINGLE)
            putString(EVENT_ACTION, FORMAT_DASH_TWO_VALUES.format(CLICK_BANNER, VALUE_SLIDER_BANNER))
            putString(EVENT_LABEL, FORMAT_DASH_TWO_VALUES.format(VALUE_SLIDER_BANNER, categoryName))
            putString(USER_ID, userId)
            putString(FIELD_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_DEFAULT)
            putString(FIELD_CURRENT_SITE, VALUE_CURRENT_SITE_DEFAULT)
            val promotions = arrayListOf(
                Bundle().apply {
                    putString(BaseTrackerConst.Promotion.CREATIVE_NAME, bannerItem.title)
                    putString(BaseTrackerConst.Promotion.CREATIVE_SLOT, (bannerPosition+1).toString())
                    putString(ITEM_ID, bannerItem.bannerId)
                    putString(ITEM_NAME, FORMAT_ITEM_NAME.format(categoryName, VALUE_SLIDER_BANNER))
                }
            )
            putParcelableArrayList(BaseTrackerConst.Promotion.KEY, promotions)
        }
        tracker.sendEnhanceEcommerceEvent(PROMO_CLICK, bundle)
    }

    fun eventImpressionBanner(categoryName: String, bannerPosition: Int,
                              bannerItem: com.tokopedia.officialstore.official.data.model.Banner, userId: String) {
        val trackerBuilder = BaseTrackerBuilder().apply {
            constructBasicPromotionView(
                event = PROMO_VIEW,
                eventCategory = OS_MICROSITE_SINGLE,
                eventAction = FORMAT_DASH_TWO_VALUES.format(IMPRESSION_BANNER, VALUE_SLIDER_BANNER),
                eventLabel = FORMAT_DASH_TWO_VALUES.format(VALUE_SLIDER_BANNER, categoryName),
                promotions = listOf(BaseTrackerConst.Promotion(
                    creative = bannerItem.title,
                    position = bannerPosition.toString(),
                    name = FORMAT_ITEM_NAME.format(categoryName, VALUE_SLIDER_BANNER),
                    id = bannerItem.bannerId,
                    creativeUrl = bannerItem.applink
                ))
            )
                .appendUserId(userId)
                .appendBusinessUnit(VALUE_BUSINESS_UNIT_DEFAULT)
                .appendCurrentSite(VALUE_CURRENT_SITE_DEFAULT)
        }.build() as HashMap<String, Any>
        trackingQueue.putEETracking(trackerBuilder)
    }

    fun eventClickAllBanner(categoryName: String) {
        tracker.sendGeneralEvent(
                TrackAppUtils
                        .gtmData(CLICK_OS_MICROSITE,
                                "$OS_MICROSITE$categoryName",
                                "banner - $CLICK",
                                "$CLICK view all"))
    }

    fun eventClickAllShop(categoryName: String) {
        tracker.sendGeneralEvent(
                TrackAppUtils
                        .gtmData(CLICK_OS_MICROSITE,
                                "$OS_MICROSITE$categoryName",
                                "all brands - $CLICK",
                                "$CLICK view all"))
    }

    fun eventClickShop(
            categoryName: String,
            shopPosition: Int,
            shopName: String,
            url: String,
            additionalInformation: String,
            featuredBrandId: String,
            isLogin: Boolean,
            shopId: String,
            campaignCode: String,
            isFromDC: Boolean = false,
            attribute: String = ""
    ) {
        val creativeName = if (isFromDC) attribute else "$shopName - $additionalInformation"
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
                        "creative", creativeName,
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

    fun eventImpressionShop(
            categoryName: String,
            shopPosition: Int,
            shopName: String,
            url: String,
            additionalInformation: String,
            featuredBrandId: String,
            isLogin: Boolean,
            shopId: String,
            isFromDC: Boolean = false,
            attribute: String = "",
    ) {
        val creativeName = if (isFromDC) attribute else "$shopName - $additionalInformation"
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
                        "creative", creativeName,
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

    fun eventClickAllFeaturedBrandOS(categoryName: String) {
        val eventAction = EVENT_POPULAR_BRANDS.format(ALL_BRANDS, CLICK)
        val eventLabelFirstFormat = "$CLICK $VIEW_ALL"
        val trackerClickAllFeaturedBrand = TrackAppUtils
            .gtmData(
                CLICK_HOMEPAGE,
                OS_MICROSITE_SINGLE,
                eventAction,
                EVENT_POPULAR_BRANDS.format(eventLabelFirstFormat, categoryName)
            )
        trackerClickAllFeaturedBrand[FIELD_BUSINESS_UNIT] = VALUE_BUSINESS_UNIT_DEFAULT
        trackerClickAllFeaturedBrand[FIELD_CURRENT_SITE] = VALUE_CURRENT_SITE_DEFAULT
        tracker.sendGeneralEvent(trackerClickAllFeaturedBrand)
    }

    fun eventClickFeaturedBrandOS(
        categoryName: String,
        shopPosition: Int,
        shopId: String,
        creativeName: String,
        headerName: String,
        bannerId: String,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            EVENT, PROMO_CLICK,
            EVENT_CATEGORY, OS_MICROSITE_SINGLE,
            EVENT_ACTION, EVENT_POPULAR_BRANDS.format(CLICK_BANNER, POPULAR_BRANDS),
            EVENT_LABEL, EVENT_LABEL_POPULAR_BRANDS.format(POPULAR_BRANDS, shopId, categoryName),
            ECOMMERCE, DataLayer.mapOf(
                PROMO_CLICK, DataLayer.mapOf(
                    PROMOTIONS, DataLayer.listOf(
                        DataLayer.mapOf(
                            FIELD_PRODUCT_ID, PROMOTIONS_ID_POPULAR_BRANDS.format(bannerId, shopId),
                            FIELD_PRODUCT_NAME, PROMOTIONS_NAME_POPULAR_BRANDS.format(
                                SLASH_OFFICIAL_STORE_WITHOUT_CATEGORY,
                                categoryName,
                                POPULAR_BRANDS,
                                headerName
                            ),
                            FIELD_PRODUCT_POSITION, "$shopPosition",
                            FIELD_PRODUCT_CREATIVE, "$creativeName $REFERENCE_POPULAR_BRANDS"
                        )
                    )
                )
            ),
            FIELD_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_DEFAULT,
            FIELD_CURRENT_SITE, VALUE_CURRENT_SITE_DEFAULT,
            USER_ID, userId
        )
        tracker.sendEnhanceEcommerceEvent(data as HashMap<String, Any>)
    }

    fun eventImpressionFeatureBrandOS(
        categoryName: String,
        shopPosition: Int,
        shopId: String,
        creativeName: String,
        userId: String,
        headerName: String,
        bannerId: String
    ) {
        val data = DataLayer.mapOf(
            EVENT, PROMO_VIEW,
            EVENT_CATEGORY, OS_MICROSITE_SINGLE,
            EVENT_ACTION, EVENT_POPULAR_BRANDS.format(IMPRESSION_BANNER, POPULAR_BRANDS),
            EVENT_LABEL, EVENT_LABEL_POPULAR_BRANDS.format(POPULAR_BRANDS, shopId, categoryName),
            USER_ID, userId,
            ECOMMERCE, DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                    PROMOTIONS, DataLayer.listOf(
                        DataLayer.mapOf(
                            FIELD_PRODUCT_ID, PROMOTIONS_ID_POPULAR_BRANDS.format(bannerId, shopId),
                            FIELD_PRODUCT_NAME, PROMOTIONS_NAME_POPULAR_BRANDS.format(
                                SLASH_OFFICIAL_STORE_WITHOUT_CATEGORY,
                                categoryName,
                                POPULAR_BRANDS,
                                headerName
                            ),
                            FIELD_PRODUCT_POSITION, "$shopPosition",
                            FIELD_PRODUCT_CREATIVE, creativeName
                        )
                    )
                )
            ),
            FIELD_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_DEFAULT,
            FIELD_CURRENT_SITE, VALUE_CURRENT_SITE_DEFAULT
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
                "id", gridData.id.toString(RADIX_10),
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
                        "id", id.toString(RADIX_10),
                        "price", price,
                        "brand", "none",
                        "category", "",
                        "variant", "none",
                        "list", "/official-store/$categoryName - flash sale - $campaignId - $headerName",
                        "position", (index + 1).toString(RADIX_10),
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
                "id", gridData.id.toString(RADIX_10),
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
                "id", gridData.id.toString(RADIX_10),
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
                        "id", id.toString(RADIX_10),
                        "price", price,
                        "brand", "none",
                        "category", "",
                        "variant", "none",
                        "list", "/official-store/$categoryName - dynamic channel mix - $headerName",
                        "position", (index + 1).toString(RADIX_10),
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
                "id", bannerData.id.toString(RADIX_10),
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
                        "id", id.toString(RADIX_10),
                        "name", "/official-store/$categoryName - $channelType - $headerName",
                        "position", (index + 1).toString(RADIX_10),
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
                        "position", (index + 1).toString(RADIX_10),
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

    fun eventClickWishlist(categoryName: String, isAddWishlist: Boolean, isLogin: Boolean, productId: Long, isTopAds: Boolean) {
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
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
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
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val eventAction = FORMAT_DASH_TWO_VALUES.format(VALUE_PRODUCT_IMPRESSION, valueDynamicMix)
        val eventLabel = FORMAT_DASH_FOUR_VALUES.format(valueDynamicMix, channel.id, channel.channelHeader.name, categoryName)
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, OS_MICROSITE_SINGLE,
                EVENT_ACTION, eventAction,
                EVENT_LABEL, eventLabel,
                FIELD_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_DEFAULT,
                FIELD_CURRENT_SITE, VALUE_CURRENT_SITE_DEFAULT,
                USER_ID, userId,
                ECOMMERCE, DataLayer.mapOf(
                    ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                    ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                            createFlashSaleCardProductItemMapComponent(
                                    channel,
                                    productItem,
                                    productPosition,
                                    isLogin,
                                    valueDynamicMix,
                                    FORMAT_ITEM_LIST.format(categoryName, valueDynamicMix, channel.channelHeader.name)
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
            channelModel: ChannelModel,
            gridData: ChannelGrid,
            position: String,
            isLogin: Boolean,
            valueDynamicMix: String,
            itemList: String = "",
    ): MutableMap<String, Any> {
        val listKeyValue = itemList.ifEmpty {
            val list = mutableListOf(SLASH_OFFICIAL_STORE)
            if (!isLogin)
                list.add(VALUE_NON_LOGIN)
            if (valueDynamicMix.isNotEmpty())
                list.add(valueDynamicMix)
            TextUtils.join(" - ", list)
        }
        return DataLayer.mapOf(
                FIELD_PRODUCT_NAME, gridData.name,
                FIELD_PRODUCT_ID, gridData.id.toString(),
                FIELD_PRODUCT_PRICE, formatPrice(gridData.price),
                FIELD_PRODUCT_BRAND, VALUE_NONE,
                FIELD_PRODUCT_CATEGORY, VALUE_NONE,
                FIELD_PRODUCT_VARIANT, VALUE_NONE,
                FIELD_PRODUCT_LIST, listKeyValue,
                FIELD_PRODUCT_POSITION, position,
                FIELD_DIMENSION_38, channelModel.channelBanner.attribution
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
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
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
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val list = mutableListOf(SLASH_OFFICIAL_STORE_WITHOUT_CATEGORY)
        if (valueDynamicMix.isNotEmpty())
            list.add(valueDynamicMix)
        if (!isLogin)
            list.add(VALUE_NON_LOGIN_NEW)
        else list.add(VALUE_LOGIN_NEW)
        val listKeyValue = TextUtils.join(" - ", list)
        val eventAction = FORMAT_DASH_TWO_VALUES.format(VALUE_PRODUCT_CLICK, valueDynamicMix)
        val eventLabel = FORMAT_DASH_THREE_VALUES.format(channel.id, channel.channelHeader.name, categoryName)
        val data = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, OS_MICROSITE_SINGLE,
                EVENT_ACTION, eventAction,
                EVENT_LABEL, eventLabel,
                FIELD_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_DEFAULT,
                FIELD_CURRENT_SITE, VALUE_CURRENT_SITE_DEFAULT,
                USER_ID, userId,
                CAMPAIGN_CODE, channel.trackingAttributionModel.campaignCode,
                ECOMMERCE, DataLayer.mapOf(
                    CLICK , DataLayer.mapOf(
                        FIELD_ACTION_FIELD , DataLayer.mapOf( FIELD_PRODUCT_LIST , listKeyValue),
                        FIELD_PRODUCTS, DataLayer.listOf(
                            createFlashSaleCardProductItemMapComponent(
                                    channel,
                                    productItem,
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
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
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
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val eventLabel = when(channel.layout){
            DynamicChannelLayout.LAYOUT_MIX_TOP -> FORMAT_DASH_TWO_VALUES.format(channel.id, categoryName)
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> FORMAT_DASH_THREE_VALUES.format(channel.id, channel.channelHeader.name, categoryName)
            else -> ""
        }
        val eventActionValue = FORMAT_CLICK_VIEW_ALL_CARD.format(valueDynamicMix)
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_HOMEPAGE,
                EVENT_CATEGORY, OS_MICROSITE_SINGLE,
                EVENT_ACTION, eventActionValue,
                EVENT_LABEL, eventLabel,
                EVENT_ATTRIBUTION, channel.channelBanner.attribution,
                FIELD_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_DEFAULT,
                FIELD_CURRENT_SITE, VALUE_CURRENT_SITE_DEFAULT
        ))
    }

    fun seeAllBannerFlashSaleClickedComponent(categoryName: String, channel: ChannelModel) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val eventLabel = when(channel.layout){
            DynamicChannelLayout.LAYOUT_MIX_TOP -> FORMAT_DASH_TWO_VALUES.format(channel.id, categoryName)
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> FORMAT_DASH_THREE_VALUES.format(channel.id, channel.channelHeader.name, categoryName)
            else -> ""
        }
        val eventActionValue = FORMAT_CLICK_VIEW_ALL.format(valueDynamicMix)
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_HOMEPAGE,
                EVENT_CATEGORY, OS_MICROSITE_SINGLE,
                EVENT_ACTION, eventActionValue,
                EVENT_LABEL, eventLabel,
                EVENT_ATTRIBUTION, channel.channelBanner.attribution,
                FIELD_BUSINESS_UNIT, VALUE_BUSINESS_UNIT_DEFAULT,
                FIELD_CURRENT_SITE, VALUE_CURRENT_SITE_DEFAULT
        ))
    }

    fun mixTopBannerCtaButtonClicked(categoryName: String, buttonName: String, channelId: String, channelBannerAttribution: String = "") {
        val eventActionValue = "$CLICK $buttonName on $VALUE_DYNAMIC_MIX_TOP_CAROUSEL"
        val mapTracking = DataLayer.mapOf(
            EVENT, CLICK_OS_MICROSITE,
            EVENT_CATEGORY, "$OS_MICROSITE$categoryName",
            EVENT_ACTION, eventActionValue,
            EVENT_LABEL, channelId,
            EVENT_ATTRIBUTION, channelBannerAttribution
        )
        tracker.sendGeneralEvent(mapTracking)
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
                categoryName = categoryName.lowercase(),
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
        eventDataLayer.putString(CAMPAIGN_CODE, "channel.campaignCode")

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
            categoryName = categoryName.lowercase(),
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