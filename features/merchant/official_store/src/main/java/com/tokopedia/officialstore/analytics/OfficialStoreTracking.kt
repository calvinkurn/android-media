package com.tokopedia.officialstore.analytics

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.DynamicChannelLayout
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Banner
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.CLICK_HOMEPAGE
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PRODUCT_VIEW
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_CLICK
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_VIEW
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.SELECT_CONTENT
import com.tokopedia.trackingoptimizer.TrackingQueue

/*
OS HOME REVAMP
Data layer docs
https://docs.google.com/spreadsheets/d/19l7m_uveuFB6YLVLqSTaclLFf13qmtCB9jQKVwzE38o/edit?usp=sharing
*/

class OfficialStoreTracking(context: Context) : BaseTrackerConst() {

    val trackerObj
        get() = getTracker()

    val trackingQueueObj
        get() = trackingQueue
    private val trackingQueue = TrackingQueue(context)
    
    companion object{
        private const val CLICK = "click"
        private const val IMPRESSION = "impression"
        private const val CLICK_OS_MICROSITE = "clickOSMicrosite"
        private const val OS_MICROSITE = "os microsite - "
        private const val FIELD_PRODUCTS = "products"
        private const val FIELD_PRODUCT_LIST = "list"
        private const val FIELD_ACTION_FIELD = "actionField"
        const val FIELD_DIMENSION_38 = "dimension38"
        const val ATTRIBUTION = "attribution"
        private const val VALUE_NONE_OTHER = "none / other"
        private const val VALUE_IDR = "IDR"

        const val EVENT_PRODUCT_VIEW = "productView"
        const val EVENT_PRODUCT_CLICK = "productClick"
        private const val PRODUCT_EVENT_ACTION = "product recommendation"
        private const val SLASH_OFFICIAL_STORE = "/official-store"
        private const val RADIX_10 = 10
        const val OS_MICROSITE_SINGLE = "os microsite"
        private const val CAMPAIGN_CODE = "campaignCode"
        private const val ECOMMERCE_IMPRESSIONS = "impressions"
        private const val ECOMMERCE_CURRENCY_CODE = "currencyCode"

        const val FORMAT_DASH_TWO_VALUES = "%s - %s"
        const val FORMAT_DASH_THREE_VALUES = "%s - %s - %s"
        const val FORMAT_DASH_FOUR_VALUES = "%s - %s - %s - %s"
        const val FORMAT_DASH_FIVE_VALUES = "%s - %s - %s - %s - %s"
        const val FORMAT_UNDERSCORE_TWO_VALUES = "%s_%s"
        const val FORMAT_UNDERSCORE_THREE_VALUES = "%s_%s_%s"
        const val FORMAT_ITEM_NAME = "${SLASH_OFFICIAL_STORE}/%s - %s"
        const val FORMAT_ITEM_NAME_THREE_VALUES = "${SLASH_OFFICIAL_STORE}/%s - %s - %s"
        const val FORMAT_ITEM_NAME_FOUR_VALUES = "${SLASH_OFFICIAL_STORE}/%s - %s - %s - %s"
        const val FORMAT_IMPRESSION_BANNER = "impression banner - %s"
        const val FORMAT_IMPRESSION_ON_BANNER = "impression on banner %s"
        const val FORMAT_CLICK_BANNER = "click banner - %s"
        const val FORMAT_CLICK_ON_BANNER = "click on banner %s"
        const val FORMAT_IMPRESSION_PRODUCT = "impression product - %s"
        const val FORMAT_CLICK_PRODUCT = "click product - %s"
        const val FORMAT_CLICK_VIEW_ALL = "click view all - %s"
        const val FORMAT_CLICK_VIEW_ALL_ON = "click view all on %s"
        const val FORMAT_CLICK_VIEW_ALL_ON_BANNER = "click view all on banner %s"
        const val FORMAT_CLICK_VIEW_ALL_CARD_ON = "click view all card on %s"
        const val FORMAT_CLICK_VIEW_ALL_CARD_ON_BANNER = "click view all card on banner %s"
        const val FORMAT_CLICK_BUTTON_ON = "click button on %s"

        private const val VALUE_TRACKER_ID_CLICK_CATEGORY = "4660"
        private const val VALUE_TRACKER_ID_IMPRESSION_CATEGORY = "4694"
        private const val VALUE_TRACKER_ID_CLICK_BANNER = "4695"
        private const val VALUE_TRACKER_ID_IMPRESSION_BANNER = "4696"
        private const val VALUE_TRACKER_ID_VIEW_ALL_BANNER = "4697"
        private const val VALUE_TRACKER_ID_VIEW_ALL_FEATURED_BRAND = "4698"
        private const val VALUE_TRACKER_ID_CLICK_FEATURED_BRAND = "4699"
        private const val VALUE_TRACKER_ID_IMPRESSION_FEATURED_BRAND = "4700"
        private const val VALUE_TRACKER_ID_VIEW_ALL_FLASH_SALE = "4701"
        private const val VALUE_TRACKER_ID_CLICK_PDP_FLASH_SALE = "4702"
        private const val VALUE_TRACKER_ID_IMPRESSION_FLASH_SALE = "4703"
        private const val VALUE_TRACKER_ID_CLICK_LEGO = "4704"
        private const val VALUE_TRACKER_ID_IMPRESSION_LEGO = "4705"
        private const val VALUE_TRACKER_ID_IMPRESSION_MIX_TOP_PRODUCT = "4713"
        private const val VALUE_TRACKER_ID_CLICK_MIX_TOP_PRODUCT = "4714"
        private const val VALUE_TRACKER_ID_CLICK_MIX_TOP_VIEW_ALL_HEADER = "4715"
        private const val VALUE_TRACKER_ID_CLICK_MIX_TOP_BUTTON_CTA = "4716"
        private const val VALUE_TRACKER_ID_IMPRESSION_MIX_LEFT_PRODUCT = "4717"
        private const val VALUE_TRACKER_ID_CLICK_MIX_LEFT_PRODUCT = "4718"
        private const val VALUE_TRACKER_ID_CLICK_MIX_LEFT_BANNER = "472O"
        private const val VALUE_TRACKER_ID_IMPRESSION_MIX_LEFT_BANNER = "4721"
        private const val VALUE_TRACKER_ID_CLICK_MIX_LEFT_VIEW_ALL_HEADER = "4715"
        private const val VALUE_TRACKER_ID_CLICK_MIX_LEFT_VIEW_ALL_CARD = "4715"

        private const val VALUE_TRACKER_ID_CLICK_THEMATIC_PRODUCT = "4706"
        private const val VALUE_TRACKER_ID_IMPRESSION_THEMATIC_PRODUCT = "4707"
        private const val VALUE_TRACKER_ID_CLICK_THEMATIC_BANNER = "4708"
        private const val VALUE_TRACKER_ID_IMPRESSION_THEMATIC_BANNER = "4709"

        private const val VALUE_CATEGORY_ICON = "category icon"
        private const val VALUE_CATEGORY_NAVIGATION = "category navigation"
        private const val VALUE_SLIDER_BANNER = "slider banner"
        private const val VALUE_POPULAR_BRANDS = "popular brands"
        private const val VALUE_FLASH_SALE = "flash sale"
        private const val VALUE_DYNAMIC_CHANNEL = "dynamic channel"
        private const val VALUE_DYNAMIC_MIX_TOP_CAROUSEL = "dynamic channel top carousel"
        private const val VALUE_DYNAMIC_MIX_LEFT_CAROUSEL = "dynamic channel left carousel"
        private const val VALUE_DYNAMIC_CHANNEL_MIX = "dynamic channel mix"
    }

    fun sendScreen(categoryName: String) {
        val screenName = "/official-store/$categoryName"
        val customDimension = HashMap<String, String>()
        // ask requested Dini Praptiwi at 6/11/2019 3:25 PM
        // @mzennis jadi seharusnya pake event
        customDimension["event"] = "openScreen"
        customDimension["cd35"] = "/official-store"
        getTracker().sendScreenAuthenticated(screenName, customDimension)
    }

    // Row 1
    fun eventClickCategory(
        categoryPosition: Int,
        categorySelected: com.tokopedia.officialstore.category.data.model.Category,
        userId: String
    ) {
        val bundle = Bundle().apply {
            putString(Event.KEY, SELECT_CONTENT)
            putString(Category.KEY, OS_MICROSITE_SINGLE)
            putString(Action.KEY, FORMAT_CLICK_BANNER.format(VALUE_CATEGORY_ICON))
            putString(Label.KEY, FORMAT_DASH_TWO_VALUES.format(VALUE_CATEGORY_ICON, categorySelected.title))
            putString(UserId.KEY, userId)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            val promotions = arrayListOf(
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, categorySelected.title)
                    putString(Promotion.CREATIVE_SLOT, (categoryPosition+1).toString())
                    putString(Promotion.ITEM_ID, categorySelected.categoryId)
                    putString(Promotion.ITEM_NAME, FORMAT_ITEM_NAME.format(categorySelected.title, VALUE_CATEGORY_NAVIGATION))
                }
            )
            putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_CATEGORY)
            putParcelableArrayList(Promotion.KEY, promotions)
        }
        getTracker().sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    // Row 2
    fun eventImpressionCategory(
        categoryName: String,
        categoryId: String,
        categoryPosition: Int,
        userId: String
    ) {
        val data = DataLayer.mapOf(
            Event.KEY, PROMO_VIEW,
            Category.KEY, OS_MICROSITE_SINGLE,
            Action.KEY, FORMAT_IMPRESSION_BANNER.format(VALUE_CATEGORY_ICON),
            Label.KEY, FORMAT_DASH_TWO_VALUES.format(VALUE_CATEGORY_ICON, categoryName),
            Ecommerce.KEY, DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                    Promotion.KEY,DataLayer.listOf(
                        DataLayer.mapOf(
                            Promotion.ITEM_ID, categoryId,
                            Promotion.ITEM_NAME, FORMAT_ITEM_NAME.format(categoryName, VALUE_CATEGORY_NAVIGATION),
                            Promotion.CREATIVE_SLOT, (categoryPosition+1).toString(),
                            Promotion.CREATIVE_NAME, categoryName,
                        )
                    )
                )
            ),
            TrackerId.KEY, VALUE_TRACKER_ID_IMPRESSION_CATEGORY,
            UserId.KEY, userId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    // Row 3
    fun eventClickBanner(categoryName: String, bannerPosition: Int,
                         bannerItem: com.tokopedia.officialstore.official.data.model.Banner, userId: String) {
        val bundle = Bundle().apply {
            putString(Event.KEY, SELECT_CONTENT)
            putString(Category.KEY, OS_MICROSITE_SINGLE)
            putString(Action.KEY, FORMAT_CLICK_BANNER.format(VALUE_SLIDER_BANNER))
            putString(Label.KEY, FORMAT_DASH_THREE_VALUES.format(VALUE_SLIDER_BANNER, bannerItem.bannerId, categoryName))
            putString(UserId.KEY, userId)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            val promotions = arrayListOf(
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, bannerItem.title)
                    putString(Promotion.CREATIVE_SLOT, (bannerPosition+1).toString())
                    putString(Promotion.ITEM_ID, bannerItem.bannerId)
                    putString(Promotion.ITEM_NAME, FORMAT_ITEM_NAME.format(categoryName, VALUE_SLIDER_BANNER))
                }
            )
            putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_BANNER)
            putParcelableArrayList(Promotion.KEY, promotions)
        }
        getTracker().sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    // Row 4
    fun eventImpressionBanner(categoryName: String, bannerPosition: Int,
                              bannerItem: com.tokopedia.officialstore.official.data.model.Banner, userId: String) {
        val trackerBuilder = BaseTrackerBuilder().apply {
            constructBasicPromotionView(
                event = PROMO_VIEW,
                eventCategory = OS_MICROSITE_SINGLE,
                eventAction = FORMAT_IMPRESSION_BANNER.format(VALUE_SLIDER_BANNER),
                eventLabel = FORMAT_DASH_THREE_VALUES.format(VALUE_SLIDER_BANNER, bannerItem.bannerId, categoryName),
                promotions = listOf(Promotion(
                    creative = bannerItem.title,
                    position = (bannerPosition+1).toString(),
                    name = FORMAT_ITEM_NAME.format(categoryName, VALUE_SLIDER_BANNER),
                    id = bannerItem.bannerId,
                    creativeUrl = bannerItem.applink
                ))
            )
                .appendUserId(userId)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendCustomKeyValue(TrackerId.KEY, VALUE_TRACKER_ID_IMPRESSION_BANNER)
        }.build() as HashMap<String, Any>
        trackingQueue.putEETracking(trackerBuilder)
    }

    // Row 5
    fun eventClickAllBanner(categoryName: String) {
        val trackingBuilder = BaseTrackerBuilder().constructBasicGeneralClick(
            CLICK_HOMEPAGE,
            OS_MICROSITE_SINGLE,
            FORMAT_CLICK_VIEW_ALL.format(VALUE_SLIDER_BANNER),
            FORMAT_DASH_THREE_VALUES.format(VALUE_SLIDER_BANNER, "", categoryName)
        ).apply {
            appendBusinessUnit(BusinessUnit.DEFAULT)
            appendCurrentSite(CurrentSite.DEFAULT)
            appendCustomKeyValue(TrackerId.KEY, VALUE_TRACKER_ID_VIEW_ALL_BANNER)
        }.build()
        getTracker().sendGeneralEvent(trackingBuilder)
    }

    // Row 6
    fun eventClickAllFeaturedBrandOS(
        categoryName: String,
        channelId: String,
        headerName: String,
        brandId: String
    ) {
        val trackingBuilder = BaseTrackerBuilder().constructBasicGeneralClick(
            CLICK_HOMEPAGE,
            OS_MICROSITE_SINGLE,
            FORMAT_CLICK_VIEW_ALL.format(VALUE_POPULAR_BRANDS),
            FORMAT_DASH_FIVE_VALUES.format(
                VALUE_POPULAR_BRANDS,
                channelId,
                headerName,
                brandId,
                categoryName
            )
        ).apply {
            appendBusinessUnit(BusinessUnit.DEFAULT)
            appendCurrentSite(CurrentSite.DEFAULT)
            appendCustomKeyValue(TrackerId.KEY, VALUE_TRACKER_ID_VIEW_ALL_FEATURED_BRAND)
        }.build()
        getTracker().sendGeneralEvent(trackingBuilder)
    }

    // Row 7
    fun eventClickFeaturedBrandOS(
        categoryName: String,
        shopPosition: Int,
        shopId: String,
        creativeName: String,
        headerName: String,
        bannerId: String,
        userId: String,
        channelId: String,
        brandId: String
    ) {
        val bundle = Bundle().apply {
            putString(Event.KEY, SELECT_CONTENT)
            putString(Category.KEY, OS_MICROSITE_SINGLE)
            putString(Action.KEY, FORMAT_CLICK_BANNER.format(VALUE_POPULAR_BRANDS))
            putString(Label.KEY, FORMAT_DASH_FIVE_VALUES.format(
                VALUE_POPULAR_BRANDS,
                channelId,
                headerName,
                brandId,
                categoryName
            ))
            putString(UserId.KEY, userId)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            val promotions = arrayListOf(
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, creativeName)
                    putString(Promotion.CREATIVE_SLOT, shopPosition.toString())
                    putString(Promotion.ITEM_ID, FORMAT_UNDERSCORE_THREE_VALUES.format(bannerId, channelId, shopId))
                    putString(Promotion.ITEM_NAME, FORMAT_ITEM_NAME_THREE_VALUES.format(
                        categoryName,
                        VALUE_POPULAR_BRANDS,
                        headerName
                    ))
                }
            )
            putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_FEATURED_BRAND)
            putParcelableArrayList(Promotion.KEY, promotions)
        }
        getTracker().sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    // Row 8
    fun eventImpressionFeatureBrandOS(
        categoryName: String,
        shopPosition: Int,
        shopId: String,
        creativeName: String,
        userId: String,
        headerName: String,
        bannerId: String,
        channelId: String,
        brandId: String
    ) {
        val data = DataLayer.mapOf(
            Event.KEY, PROMO_VIEW,
            Category.KEY, OS_MICROSITE_SINGLE,
            Action.KEY, FORMAT_IMPRESSION_BANNER.format(VALUE_POPULAR_BRANDS),
            Label.KEY, FORMAT_DASH_FIVE_VALUES.format(
                VALUE_POPULAR_BRANDS,
                channelId,
                headerName,
                brandId,
                categoryName
            ),
            UserId.KEY, userId,
            Ecommerce.KEY, DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                    Promotion.KEY, DataLayer.listOf(
                        DataLayer.mapOf(
                            Promotion.ITEM_ID, FORMAT_UNDERSCORE_THREE_VALUES.format(bannerId, channelId, shopId),
                            Promotion.ITEM_NAME, FORMAT_ITEM_NAME_THREE_VALUES.format(
                                categoryName,
                                VALUE_POPULAR_BRANDS,
                                headerName
                            ),
                            Promotion.CREATIVE_SLOT, "$shopPosition",
                            Promotion.CREATIVE_NAME, creativeName
                        )
                    )
                )
            ),
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            TrackerId.KEY, VALUE_TRACKER_ID_IMPRESSION_FEATURED_BRAND
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    // Row 9
    fun flashSaleClickViewAll(
        categoryName: String,
        channelId: String,
        headerName: String
    ) {
        val trackingBuilder = BaseTrackerBuilder().constructBasicGeneralClick(
            CLICK_HOMEPAGE,
            OS_MICROSITE_SINGLE,
            FORMAT_CLICK_VIEW_ALL.format(VALUE_FLASH_SALE),
            FORMAT_DASH_FOUR_VALUES.format(VALUE_FLASH_SALE, channelId, headerName, categoryName)
        ).apply {
            appendBusinessUnit(BusinessUnit.DEFAULT)
            appendCurrentSite(CurrentSite.DEFAULT)
            appendCustomKeyValue(TrackerId.KEY, VALUE_TRACKER_ID_VIEW_ALL_FLASH_SALE)
        }.build()
        getTracker().sendGeneralEvent(trackingBuilder)
    }

    // Row 10
    fun flashSalePDPClick(
        categoryName: String,
        headerName: String,
        position: String,
        gridData: Grid,
        channelId: String,
        userId: String
    ) {
        val bundle = Bundle().apply {
            putString(Event.KEY, SELECT_CONTENT)
            putString(Category.KEY, OS_MICROSITE_SINGLE)
            putString(Action.KEY, FORMAT_CLICK_PRODUCT.format(VALUE_FLASH_SALE))
            putString(Label.KEY, FORMAT_DASH_FOUR_VALUES.format(VALUE_FLASH_SALE, channelId, headerName, categoryName))
            putString(ItemList.KEY, FORMAT_ITEM_NAME_THREE_VALUES.format(categoryName, VALUE_FLASH_SALE, headerName))
            putString(UserId.KEY, userId)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            val items = arrayListOf(
                Bundle().apply {
                    putString(FIELD_DIMENSION_38, gridData.attribution)
                    putString(Items.ITEM_NAME, gridData.name)
                    putString(Items.ITEM_ID, gridData.id.toString())
                    putString(Items.PRICE, formatPrice(gridData.price))
                    putString(Items.ITEM_BRAND, Value.NONE_OTHER)
                    putString(Items.ITEM_CATEGORY, Value.EMPTY)
                    putString(Items.ITEM_VARIANT, Value.NONE_OTHER)
                    putString(Items.INDEX, position)
                }
            )
            putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_PDP_FLASH_SALE)
            putParcelableArrayList(Items.KEY, items)
        }
        getTracker().sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    // Row 11
    fun flashSaleImpression(
        categoryName: String,
        channelData: Channel,
        userId: String
    ) {
        val headerName = channelData.header?.name ?: ""
        val trackerBuilder = BaseTrackerBuilder().constructBasicProductView(
            event = PRODUCT_VIEW,
            eventLabel = FORMAT_DASH_FOUR_VALUES.format(VALUE_FLASH_SALE, channelData.id, headerName, categoryName),
            eventCategory = OS_MICROSITE_SINGLE,
            eventAction = FORMAT_IMPRESSION_PRODUCT.format(VALUE_FLASH_SALE),
            list = FORMAT_ITEM_NAME_THREE_VALUES.format(categoryName, VALUE_FLASH_SALE, headerName),
            products = channelData.grids.mapIndexed { index, grid ->
                Product(
                    name = grid.name,
                    id = grid.id.toString(),
                    productPrice = grid.price,
                    brand = Value.NONE_OTHER,
                    category = Value.EMPTY,
                    variant = Value.NONE_OTHER,
                    productPosition = (index + 1).toString(),
                    isFreeOngkir = grid.freeOngkir?.isActive ?: false
                )
            }
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendCustomKeyValue(TrackerId.KEY, VALUE_TRACKER_ID_IMPRESSION_FLASH_SALE)
        trackingQueue.putEETracking(trackerBuilder.build() as HashMap<String, Any>)
    }

    // Row 12
    fun clickLego36Image(categoryName: String, headerName: String, position: String, channelGrid: ChannelGrid, channelModel: ChannelModel, userId: String) {
        val bundle = Bundle().apply {
            putString(Event.KEY, SELECT_CONTENT)
            putString(Category.KEY, OS_MICROSITE_SINGLE)
            putString(Action.KEY, FORMAT_CLICK_BANNER.format(VALUE_DYNAMIC_CHANNEL))
            putString(Label.KEY, FORMAT_DASH_FOUR_VALUES.format(VALUE_DYNAMIC_CHANNEL, channelModel.id, headerName, categoryName))
            putString(UserId.KEY, userId)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            val promotions = arrayListOf(
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, channelGrid.attribution)
                    putString(Promotion.CREATIVE_SLOT, position)
                    putString(Promotion.ITEM_ID, FORMAT_UNDERSCORE_TWO_VALUES.format(channelModel.channelBanner.id, channelModel.id))
                    putString(Promotion.ITEM_NAME, FORMAT_ITEM_NAME_FOUR_VALUES.format(
                        categoryName, VALUE_DYNAMIC_CHANNEL,
                        headerName, channelGrid.applink))
                }
            )
            putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_LEGO)
            putParcelableArrayList(Promotion.KEY, promotions)
        }
        getTracker().sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    // Row 13
    fun impressionLego36Image(categoryName: String, channelModel: ChannelModel, userId: String) {
        val headerName = channelModel.channelHeader.name
        val promotionBody = channelModel.channelGrids.mapIndexed { index, grid ->
            DataLayer.mapOf(
                Promotion.ITEM_ID, FORMAT_UNDERSCORE_TWO_VALUES.format(channelModel.channelBanner.id, channelModel.id),
                Promotion.ITEM_NAME, FORMAT_ITEM_NAME_FOUR_VALUES.format(
                    categoryName, VALUE_DYNAMIC_CHANNEL,
                    headerName, grid.applink),
                Promotion.CREATIVE_SLOT, (index+1).toString(),
                Promotion.CREATIVE_NAME, grid.attribution,
            )
        }

        trackingQueue.putEETracking(DataLayer.mapOf(
            Event.KEY, PROMO_VIEW,
            Category.KEY, OS_MICROSITE_SINGLE,
            Action.KEY, FORMAT_IMPRESSION_BANNER.format(VALUE_DYNAMIC_CHANNEL),
            Label.KEY, FORMAT_DASH_FOUR_VALUES.format(VALUE_DYNAMIC_CHANNEL, channelModel.id, headerName, categoryName),
            Ecommerce.KEY, DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                    Promotion.KEY, promotionBody
                )
            ),
            UserId.KEY, userId,
            TrackerId.KEY, VALUE_TRACKER_ID_IMPRESSION_LEGO,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT
        ) as HashMap<String, Any>)
    }

    // Row 21 & Row 25
    fun carouselProductCardImpression(
        categoryName: String,
        channel: ChannelModel,
        productItem: ChannelGrid,
        productPosition: String,
        userId: String
    ) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val trackerId = when (channel.layout) {
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_TRACKER_ID_IMPRESSION_MIX_TOP_PRODUCT
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_TRACKER_ID_IMPRESSION_MIX_LEFT_PRODUCT
            else -> ""
        }
        val eventAction = FORMAT_IMPRESSION_PRODUCT.format(valueDynamicMix)
        val eventLabel = FORMAT_DASH_FOUR_VALUES.format(valueDynamicMix, channel.id, channel.channelHeader.name, categoryName)
        val list = FORMAT_ITEM_NAME_THREE_VALUES.format(categoryName, valueDynamicMix, channel.channelHeader.name)
        val data = DataLayer.mapOf(
            Event.KEY, PRODUCT_VIEW,
            Category.KEY, OS_MICROSITE_SINGLE,
            Action.KEY, eventAction,
            Label.KEY, eventLabel,
            ItemList.KEY, list,
            Ecommerce.KEY, DataLayer.mapOf(
                ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                    DataLayer.mapOf(
                        Items.ITEM_NAME, productItem.name,
                        Items.ITEM_ID, productItem.id,
                        Items.PRICE, formatPrice(productItem.price),
                        Items.ITEM_BRAND, Value.NONE_OTHER,
                        Items.ITEM_CATEGORY, Value.EMPTY,
                        Items.ITEM_VARIANT, Value.NONE_OTHER,
                        Items.INDEX, productPosition,
                        FIELD_DIMENSION_38, productItem.attribution
                    )
                )
            ),
            UserId.KEY, userId,
            TrackerId.KEY, trackerId,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT
        )
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    // Row 22 & Row 26
    fun carouselProductCardClicked(
        categoryName: String,
        channel: ChannelModel,
        productItem: ChannelGrid,
        productPosition: String,
        userId: String
    ) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val trackerId = when (channel.layout) {
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_TRACKER_ID_CLICK_MIX_TOP_PRODUCT
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_TRACKER_ID_CLICK_MIX_LEFT_PRODUCT
            else -> ""
        }
        val eventAction = FORMAT_CLICK_PRODUCT.format(valueDynamicMix)
        val eventLabel = FORMAT_DASH_FOUR_VALUES.format(valueDynamicMix, channel.id, channel.channelHeader.name, categoryName)
        val list = FORMAT_ITEM_NAME_THREE_VALUES.format(categoryName, valueDynamicMix, channel.channelHeader.name)

        val bundle = Bundle().apply {
            putString(Event.KEY, SELECT_CONTENT)
            putString(Category.KEY, OS_MICROSITE_SINGLE)
            putString(Action.KEY, eventAction)
            putString(Label.KEY, eventLabel)
            putString(UserId.KEY, userId)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            putString(ItemList.KEY, list)
            val items = arrayListOf(
                Bundle().apply {
                    putString(Items.ITEM_NAME, productItem.name)
                    putString(Items.ITEM_ID, productItem.id)
                    putString(Items.PRICE, formatPrice(productItem.price))
                    putString(Items.ITEM_BRAND, Value.NONE_OTHER)
                    putString(Items.ITEM_CATEGORY, Value.EMPTY)
                    putString(Items.ITEM_VARIANT, Value.NONE_OTHER)
                    putString(Items.INDEX, productPosition)
                    putString(FIELD_DIMENSION_38, productItem.attribution)
                }
            )
            putString(TrackerId.KEY, trackerId)
            putParcelableArrayList(Items.KEY, items)
        }
        getTracker().sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    private fun formatPrice(price: String): String? {
        return if (!TextUtils.isEmpty(price)) {
            price.replace("[^\\d]".toRegex(), "")
        } else {
            ""
        }
    }

    // Row 23 & Row 29
    fun carouselHeaderSeeAllClick(categoryName: String, channel: ChannelModel) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val eventLabel = FORMAT_DASH_FOUR_VALUES.format(valueDynamicMix, channel.id, channel.channelHeader.name, categoryName)
        val eventActionValue = FORMAT_CLICK_VIEW_ALL_ON.format(valueDynamicMix)
        val trackerId = when (channel.layout) {
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_TRACKER_ID_CLICK_MIX_TOP_VIEW_ALL_HEADER
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_TRACKER_ID_CLICK_MIX_LEFT_VIEW_ALL_HEADER
            else -> ""
        }
        getTracker().sendGeneralEvent(DataLayer.mapOf(
            Event.KEY, CLICK_HOMEPAGE,
            Category.KEY, OS_MICROSITE_SINGLE,
            Action.KEY, eventActionValue,
            Label.KEY, eventLabel,
            ATTRIBUTION, channel.channelBanner.attribution,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            TrackerId.KEY, trackerId
        ))
    }

    // Row 24
    fun mixTopBannerCtaButtonClicked(
        categoryName: String,
        buttonName: String,
        channelId: String,
        headerName: String,
        channelBannerAttribution: String
    ) {
        val eventActionValue = FORMAT_CLICK_BUTTON_ON.format(VALUE_DYNAMIC_MIX_TOP_CAROUSEL)
        val eventLabel = FORMAT_DASH_FIVE_VALUES.format(VALUE_DYNAMIC_MIX_TOP_CAROUSEL, channelId, headerName, buttonName, categoryName)
        val mapTracking = DataLayer.mapOf(
            Event.KEY, CLICK_HOMEPAGE,
            Category.KEY, OS_MICROSITE_SINGLE,
            Action.KEY, eventActionValue,
            Label.KEY, eventLabel,
            ATTRIBUTION, channelBannerAttribution,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            TrackerId.KEY, VALUE_TRACKER_ID_CLICK_MIX_TOP_BUTTON_CTA
        )
        getTracker().sendGeneralEvent(mapTracking)
    }

    // Row 27
    fun eventClickMixLeftImageBanner(channel: ChannelModel, categoryName: String, bannerPosition: Int, userId: String) {
        val promotions = arrayListOf(
            Bundle().apply {
                putString(Promotion.CREATIVE_NAME, channel.name)
                putString(Promotion.CREATIVE_SLOT, bannerPosition.toString())
                putString(Promotion.ITEM_ID, FORMAT_UNDERSCORE_TWO_VALUES.format(channel.channelBanner.id, channel.id))
                putString(Promotion.ITEM_NAME, FORMAT_ITEM_NAME_FOUR_VALUES.format(
                    categoryName, VALUE_DYNAMIC_MIX_LEFT_CAROUSEL,
                    channel.channelHeader.name, channel.channelBanner.applink))
            }
        )
        val bundle = Bundle().apply {
            putString(Event.KEY, SELECT_CONTENT)
            putString(Action.KEY, FORMAT_CLICK_BANNER.format(VALUE_DYNAMIC_MIX_LEFT_CAROUSEL))
            putString(Label.KEY, FORMAT_DASH_FOUR_VALUES.format(VALUE_DYNAMIC_MIX_LEFT_CAROUSEL, channel.id, channel.channelHeader.name, categoryName))
            putString(Category.KEY, OS_MICROSITE_SINGLE)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            putString(UserId.KEY, userId)
            putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_MIX_LEFT_BANNER)
            putParcelableArrayList(Promotion.KEY, promotions)
        }
        getTracker().sendEnhanceEcommerceEvent(SELECT_CONTENT, bundle)
    }

    // Row 28
    fun eventImpressionMixLeftImageBanner(channel: ChannelModel, categoryName: String, bannerPosition: Int, userId: String) {
        val trackerBuilder = BaseTrackerBuilder()
            .constructBasicPromotionView(
                event = PROMO_VIEW,
                eventAction = FORMAT_IMPRESSION_BANNER.format(VALUE_DYNAMIC_MIX_LEFT_CAROUSEL),
                eventCategory = OS_MICROSITE_SINGLE,
                eventLabel = FORMAT_DASH_FOUR_VALUES.format(
                    VALUE_DYNAMIC_MIX_LEFT_CAROUSEL,
                    channel.id,
                    channel.channelHeader.name,
                    categoryName
                ),
                promotions = listOf(
                    Promotion(
                        id = FORMAT_UNDERSCORE_TWO_VALUES.format(
                            channel.channelBanner.id,
                            channel.id
                        ),
                        name = FORMAT_ITEM_NAME_FOUR_VALUES.format(
                            categoryName,
                            VALUE_DYNAMIC_MIX_LEFT_CAROUSEL,
                            channel.channelHeader.name,
                            channel.channelBanner.applink
                        ),
                        position = bannerPosition.toString(),
                        creative = channel.name
                    )
                )
            )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendCustomKeyValue(TrackerId.KEY, VALUE_TRACKER_ID_IMPRESSION_MIX_LEFT_BANNER)
            .build()
        trackingQueue.putEETracking(trackerBuilder as HashMap<String, Any>)
    }

    // Row 30
    fun carouselViewAllCardClicked(categoryName: String, channel: ChannelModel) {
        val valueDynamicMix = when (channel.layout) {
            DynamicChannelLayout.LAYOUT_MIX_TOP -> VALUE_DYNAMIC_MIX_TOP_CAROUSEL
            DynamicChannelLayout.LAYOUT_MIX_LEFT -> VALUE_DYNAMIC_MIX_LEFT_CAROUSEL
            else -> ""
        }
        val eventLabel = FORMAT_DASH_FOUR_VALUES.format(valueDynamicMix, channel.id, channel.channelHeader.name, categoryName)
        val eventActionValue = FORMAT_CLICK_VIEW_ALL_CARD_ON.format(valueDynamicMix)
        getTracker().sendGeneralEvent(DataLayer.mapOf(
            Event.KEY, CLICK_HOMEPAGE,
            Category.KEY, OS_MICROSITE_SINGLE,
            Action.KEY, eventActionValue,
            Label.KEY, eventLabel,
            ATTRIBUTION, channel.channelBanner.attribution,
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            TrackerId.KEY, VALUE_TRACKER_ID_CLICK_MIX_LEFT_VIEW_ALL_CARD
        ))
    }

    fun eventClickProductRecommendation(
        item: RecommendationItem,
        position: String,
        recommendationTitle: String,
        isLogin: Boolean,
        categoryName: String
    ) {
        val data = DataLayer.mapOf(
            Event.KEY, EVENT_PRODUCT_CLICK,
            Category.KEY, "$OS_MICROSITE$categoryName", // Here
            Action.KEY, "click - $PRODUCT_EVENT_ACTION",
            Label.KEY, recommendationTitle,
            Ecommerce.KEY, DataLayer.mapOf(
                CLICK, DataLayer.mapOf(
                    FIELD_ACTION_FIELD, DataLayer.mapOf(
                        FIELD_PRODUCT_LIST, getListProductClickInsideActionField(categoryName, item.recommendationType)
                    ),
                    FIELD_PRODUCTS, DataLayer.listOf(
                        convertRecommendationItemToDataImpressionObject(item, isLogin, position)
                    ))))
        getTracker().sendEnhanceEcommerceEvent(data)
    }

    fun eventImpressionProductRecommendation(
        item: RecommendationItem,
        isLogin: Boolean,
        categoryName: String,
        recommendationTitle: String,
        position: String) {
        val data = DataLayer.mapOf(
            Event.KEY, EVENT_PRODUCT_VIEW,
            Category.KEY, String.format(OS_MICROSITE, categoryName),
            Action.KEY, "impression - $PRODUCT_EVENT_ACTION",
            Label.KEY, recommendationTitle,
            Ecommerce.KEY, DataLayer.mapOf(
                ECOMMERCE_CURRENCY_CODE, VALUE_IDR,
                ECOMMERCE_IMPRESSIONS, DataLayer.listOf(
                    convertRecommendationItemToDataImpressionObject(item, isLogin, position)
                )))
        trackingQueue.putEETracking(data as HashMap<String, Any>)
    }

    private fun convertRecommendationItemToDataImpressionObject(item: RecommendationItem, isLogin: Boolean, position: String): Any {
        return DataLayer.mapOf(
                Items.ITEM_NAME, item.name,
                Items.ITEM_ID, item.productId.toString(),
                Items.PRICE, item.priceInt.toString(),
                Items.ITEM_BRAND, item.shopName,
                Items.ITEM_CATEGORY, item.categoryBreadcrumbs,
                Items.ITEM_VARIANT, VALUE_NONE_OTHER,
                FIELD_PRODUCT_LIST, getListProductInsideProductField(item.recommendationType, item.isTopAds, isLogin),
                Items.INDEX, position,
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

        getTracker().sendGeneralEvent(
                TrackAppUtils
                        .gtmData(CLICK_OS_MICROSITE,
                                "$OS_MICROSITE$categoryName",
                                eventAction,
                                eventLabel))
    }

    @Deprecated("old shop tracker")
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
            Event.KEY, PROMO_CLICK,
            Category.KEY, "$OS_MICROSITE$categoryName",
            Action.KEY, "$CLICK - shop - all brands - $statusLogin",
            Label.KEY, shopId,
            CAMPAIGN_CODE, campaignCode,
            Ecommerce.KEY, DataLayer.mapOf(
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
        getTracker().sendEnhanceEcommerceEvent(data as HashMap<String, Any>)
    }

    @Deprecated("old shop tracker")
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
            Event.KEY, PROMO_VIEW,
            Category.KEY, "$OS_MICROSITE$categoryName",
            Action.KEY, "all brands - $IMPRESSION - $statusLogin",
            Label.KEY, shopId,
            Ecommerce.KEY, DataLayer.mapOf(
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

    @Deprecated("old shop tracker")
    fun eventClickAllShop(categoryName: String) {
        getTracker().sendGeneralEvent(
            TrackAppUtils
                .gtmData(CLICK_OS_MICROSITE,
                    "$OS_MICROSITE$categoryName",
                    "all brands - $CLICK",
                    "$CLICK view all"))
    }

    fun sendAll() {
        trackingQueue.sendAll()
    }
}
