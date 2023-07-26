package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel
import com.tokopedia.home_component.util.getTopadsString
import com.tokopedia.homenav.common.TrackingConst
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.builder.util.BaseTrackerConst

internal object SpecialReleaseRevampTracking : BaseTrackerConst() {

    private const val LIST_FORMAT = "/ - p%s - dynamic channel rilisan spesial home - product - %s - carousel - %s - %s - %s - %s"
    private const val SHOP_ITEM_ID_FORMAT = "%s_%s_%s_%s_%s"
    private const val SHOP_ITEM_NAME_FORMAT = "/ - p%s - dynamic channel rilisan spesial home - banner - %s"

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4096
    // Tracker ID: 45499
    fun getImpressionProduct(trackingAttributionModel: TrackingAttributionModel, grid: ChannelGrid, userId: String): HashMap<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val gridPosition = (grid.position + 1).toString()
        return trackingBuilder.constructBasicProductView(
            event = Event.PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = "impression on product dynamic channel rilisan spesial home",
            eventLabel = "",
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
            .appendCustomKeyValue(TrackerId.KEY, "45499")
            .build() as HashMap<String, Any>
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4096
    // Tracker ID: 45500
    fun sendClickProduct(trackingAttributionModel: TrackingAttributionModel, grid: ChannelGrid, userId: String) {
        val gridPosition = (grid.position + 1).toString()
        val trackingBuilder = BaseTrackerBuilder().constructBasicProductClick(
            event = Event.PRODUCT_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = "click on product dynamic channel rilisan spesial home",
            eventLabel = "%s - %s".format(trackingAttributionModel.channelId, trackingAttributionModel.headerName),
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
            .appendCustomKeyValue(TrackerId.KEY, "45500")
            .build()
        getTracker().sendEnhanceEcommerceEvent(trackingBuilder)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4096
    // Tracker ID: 45501
    fun sendClickViewAll(trackingAttributionModel: TrackingAttributionModel) {
        Tracker.Builder()
            .setEvent("clickHomepage")
            .setEventAction("click view all on dynamic channel rilisan spesial home")
            .setEventCategory("homepage")
            .setEventLabel("%s - %s".format(trackingAttributionModel.channelId, trackingAttributionModel.headerName))
            .setCustomProperty("trackerId", "45501")
            .setBusinessUnit("home & browse")
            .setCustomProperty("channelId", trackingAttributionModel.channelId)
            .setCurrentSite("tokopediamarketplace")
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4096
    // Tracker ID: 45502
    fun sendClickShop(trackingAttributionModel: TrackingAttributionModel, grid: ChannelGrid, userId: String) {
        val gridPosition = (grid.position + 1).toString()
        val trackerBuilder = BaseTrackerBuilder().constructBasicPromotionClick(
            event = Event.PROMO_CLICK,
            eventCategory = Category.HOMEPAGE,
            eventAction = "click on banner shop dynamic channel rilisan spesial home",
            eventLabel = "%s - %s".format(trackingAttributionModel.channelId, trackingAttributionModel.headerName),
            promotions = arrayListOf(
                Promotion(
                    id = SHOP_ITEM_ID_FORMAT.format(
                        trackingAttributionModel.channelId,
                        trackingAttributionModel.bannerId,
                        grid.shop.id,
                        trackingAttributionModel.persoType,
                        trackingAttributionModel.categoryId
                    ),
                    creative = grid.shop.shopName,
                    name = SHOP_ITEM_NAME_FORMAT.format(trackingAttributionModel.parentPosition, trackingAttributionModel.headerName),
                    position = gridPosition
                )
            )
        )
            .appendBusinessUnit(TrackingConst.DEFAULT_BUSINESS_UNIT)
            .appendCurrentSite(TrackingConst.DEFAULT_CURRENT_SITE)
            .appendUserId(userId)
            .appendChannelId(trackingAttributionModel.channelId)
            .appendCustomKeyValue(TrackerId.KEY, "45502")
            .build() as HashMap<String, Any>
        getTracker().sendEnhanceEcommerceEvent(trackerBuilder)
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4096
    // Tracker ID: 45503
    fun getImpressionShop(trackingAttributionModel: TrackingAttributionModel, grid: ChannelGrid, userId: String): HashMap<String, Any> {
        val gridPosition = (grid.position + 1).toString()
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = "impression on banner shop dynamic channel rilisan spesial home",
            eventLabel = "",
            promotions = arrayListOf(
                Promotion(
                    id = SHOP_ITEM_ID_FORMAT.format(
                        trackingAttributionModel.channelId,
                        trackingAttributionModel.bannerId,
                        grid.shop.id,
                        trackingAttributionModel.persoType,
                        trackingAttributionModel.categoryId
                    ),
                    creative = grid.attribution,
                    name = SHOP_ITEM_NAME_FORMAT.format(trackingAttributionModel.parentPosition, trackingAttributionModel.headerName),
                    position = gridPosition
                )
            )
        )
            .appendBusinessUnit(TrackingConst.DEFAULT_BUSINESS_UNIT)
            .appendCurrentSite(TrackingConst.DEFAULT_CURRENT_SITE)
            .appendUserId(userId)
            .appendChannelId(trackingAttributionModel.channelId)
            .appendCustomKeyValue(TrackerId.KEY, "45503")
            .build() as HashMap<String, Any>
    }
}
