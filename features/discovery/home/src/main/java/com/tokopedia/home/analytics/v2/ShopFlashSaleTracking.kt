package com.tokopedia.home.analytics.v2

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.util.getTopadsString
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.builder.util.BaseTrackerConst

object ShopFlashSaleTracking: BaseTrackerConst() {
    private const val LIST_FORMAT = "/ - p%s - dynamic channel flash sale toko home - product - %s - carousel - %s - %s - %s - %s"
    private const val EVENT_ACTION_PRODUCT_IMPRESSION = "impression on product dynamic channel flash sale toko home"
    private const val EVENT_ACTION_PRODUCT_CLICK = "click on product dynamic channel flash sale toko home"
    private const val EVENT_ACTION_CLICK_SHOP_TAB = "click on shop chips dynamic channel flash sale toko home"
    private const val EVENT_ACTION_CLICK_VIEW_ALL = "click view all on dynamic channel flash sale toko home"
    private const val EVENT_ACTION_CLICK_SHOP_NAME = "click on shop name dynamic channel flash sale toko home"
    private const val EVENT_ACTION_CLICK_VIEW_ALL_CARD = "click view all card on dynamic channel flash sale toko home"

    private const val EVENT_LABEL_FORMAT = "%s - %s"

    private const val TRACKER_ID_PRODUCT_IMPRESSION = "45515"
    private const val TRACKER_ID_PRODUCT_CLICK = "45516"
    private const val TRACKER_ID_CLICK_SHOP_TAB = "45517"
    private const val TRACKER_ID_CLICK_VIEW_ALL = "45518"
    private const val TRACKER_ID_CLICK_SHOP_NAME = "45598"
    private const val TRACKER_ID_CLICK_VIEW_ALL_CARD = "45598"

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4099
    // Tracker ID: 45515
    fun getImpressionProduct(trackingAttributionModel: TrackingAttributionModel, grid: ChannelGrid, userId: String): HashMap<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val gridPosition = (grid.position + 1).toString()
        return trackingBuilder.constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_PRODUCT_IMPRESSION,
            eventLabel = Value.EMPTY,
            products = listOf(
                Product(
                    name = grid.name,
                    id = grid.id,
                    productPrice = convertRupiahToInt(
                        grid.price
                    ).toString(),
                    brand = Value.NONE_OTHER,
                    category = grid.categoryBreadcrumbs,
                    variant = Value.NONE_OTHER,
                    productPosition = gridPosition,
                    channelId = trackingAttributionModel.channelId,
                    isFreeOngkir = grid.isFreeOngkirActive && !grid.labelGroup.hasLabelGroupFulfillment(),
                    isFreeOngkirExtra = grid.isFreeOngkirActive && grid.labelGroup.hasLabelGroupFulfillment(),
                    persoType = trackingAttributionModel.persoType,
                    categoryId = trackingAttributionModel.categoryId,
                    isTopAds = grid.isTopads,
                    recommendationType = grid.recommendationType,
                    headerName = trackingAttributionModel.headerName,
                    isCarousel = true
                )
            ),
            list = LIST_FORMAT.format(
                trackingAttributionModel.parentPosition,
                grid.getTopadsString(),
                grid.recommendationType,
                trackingAttributionModel.pageName,
                trackingAttributionModel.galaxyAttribution,
                trackingAttributionModel.headerName
            )
        )
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendUserId(userId)
            .appendCustomKeyValue(TrackerId.KEY,
                TRACKER_ID_PRODUCT_IMPRESSION
            )
            .build() as HashMap<String, Any>
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4099
    // Tracker ID: 45516
    fun sendClickProduct(trackingAttributionModel: TrackingAttributionModel, grid: ChannelGrid, userId: String) {
        val gridPosition = (grid.position + 1).toString()
        val trackingBuilder = BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_PRODUCT_CLICK,
            eventLabel = EVENT_LABEL_FORMAT.format(trackingAttributionModel.channelId, trackingAttributionModel.headerName),
            products = listOf(
                Product(
                    name = grid.name,
                    id = grid.id,
                    productPrice = convertRupiahToInt(
                        grid.price
                    ).toString(),
                    brand = Value.NONE_OTHER,
                    category = grid.categoryBreadcrumbs,
                    variant = Value.NONE_OTHER,
                    productPosition = gridPosition,
                    channelId = trackingAttributionModel.channelId,
                    isFreeOngkir = grid.isFreeOngkirActive && !grid.labelGroup.hasLabelGroupFulfillment(),
                    isFreeOngkirExtra = grid.isFreeOngkirActive && grid.labelGroup.hasLabelGroupFulfillment(),
                    persoType = trackingAttributionModel.persoType,
                    categoryId = trackingAttributionModel.categoryId,
                    isTopAds = grid.isTopads,
                    recommendationType = grid.recommendationType,
                    headerName = trackingAttributionModel.headerName,
                    isCarousel = true
                )
            ),
            list = LIST_FORMAT.format(
                trackingAttributionModel.parentPosition,
                grid.getTopadsString(),
                grid.recommendationType,
                trackingAttributionModel.pageName,
                trackingAttributionModel.galaxyAttribution,
                trackingAttributionModel.headerName
            )
        )
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendUserId(userId)
            .appendCustomKeyValue(TrackerId.KEY,
                TRACKER_ID_PRODUCT_CLICK
            )
            .build()
        getTracker().sendEnhanceEcommerceEvent(trackingBuilder)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4099
    // Tracker ID: 45517
    fun sendClickShopTab(trackingAttributionModel: TrackingAttributionModel) {
        Tracker.Builder()
            .setEvent(Event.CLICK_HOMEPAGE)
            .setEventAction(EVENT_ACTION_CLICK_SHOP_TAB)
            .setEventCategory(Category.HOMEPAGE)
            .setEventLabel(EVENT_LABEL_FORMAT.format(trackingAttributionModel.channelId, trackingAttributionModel.headerName))
            .setCustomProperty(TrackerId.KEY, TRACKER_ID_CLICK_SHOP_TAB)
            .setBusinessUnit(BusinessUnit.DEFAULT)
            .setCustomProperty(ChannelId.KEY, trackingAttributionModel.channelId)
            .setCurrentSite(CurrentSite.DEFAULT)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4099
    // Tracker ID: 45518
    fun sendClickViewAll(trackingAttributionModel: TrackingAttributionModel) {
        Tracker.Builder()
            .setEvent(Event.CLICK_HOMEPAGE)
            .setEventAction(EVENT_ACTION_CLICK_VIEW_ALL)
            .setEventCategory(Category.HOMEPAGE)
            .setEventLabel(EVENT_LABEL_FORMAT.format(trackingAttributionModel.channelId, trackingAttributionModel.headerName))
            .setCustomProperty(TrackerId.KEY, TRACKER_ID_CLICK_VIEW_ALL)
            .setBusinessUnit(BusinessUnit.DEFAULT)
            .setCustomProperty(ChannelId.KEY, trackingAttributionModel.channelId)
            .setCurrentSite(CurrentSite.DEFAULT)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4099
    // Tracker ID: 46117
    fun sendClickViewAllCard(trackingAttributionModel: TrackingAttributionModel) {
        Tracker.Builder()
            .setEvent(Event.CLICK_HOMEPAGE)
            .setEventAction(EVENT_ACTION_CLICK_VIEW_ALL_CARD)
            .setEventCategory(Category.HOMEPAGE)
            .setEventLabel(EVENT_LABEL_FORMAT.format(trackingAttributionModel.channelId, trackingAttributionModel.headerName))
            .setCustomProperty(TrackerId.KEY, TRACKER_ID_CLICK_VIEW_ALL_CARD)
            .setBusinessUnit(BusinessUnit.DEFAULT)
            .setCustomProperty(ChannelId.KEY, trackingAttributionModel.channelId)
            .setCurrentSite(CurrentSite.DEFAULT)
            .build()
            .send()
    }
}
