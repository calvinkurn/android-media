package com.tokopedia.home.analytics.v2

import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelTracker
import com.tokopedia.home_component.visitable.shorten.ProductWidgetUiModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object Kd2ProductSquareTracker : BaseTrackerConst() {

    fun productView(data: ProductWidgetUiModel, userId: String, position: Int): Map<String, Any> {
        val model = data.channelModel
        val atLeastGetFirstProductData = data.data.firstOrNull() ?: return mapOf()

        val isTopAds = if (atLeastGetFirstProductData.tracker.isTopAds) "topads" else "non topads"
        val isCarousel = if (atLeastGetFirstProductData.tracker.isCarousel) "carousel" else "non carousel"
        val recomType = model.trackingAttributionModel.persoType
        val recomPageName = model.trackingAttributionModel.pageName
        val buType = atLeastGetFirstProductData.tracker.buType
        val headerName = model.channelHeader.name

        return BaseTrackerBuilder()
            .constructBasicProductView(
                event = Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventLabel = "%s - %s".format(
                    model.id,
                    model.channelHeader.name
                ),
                eventAction = "impression on product dynamic channel 2 square",
                list = "/ - p${position + 1} - dynamic channel 2 square - product - $isTopAds - $isCarousel - $recomType - $recomPageName - $buType - $headerName",
                products = model.channelGrids.mapIndexed { index, channelGrid ->
                    val trackerJson = data.data[index].tracker

                    Product(
                        name = trackerJson.productName,
                        id = trackerJson.productId,
                        productPrice = convertRupiahToInt(channelGrid.price).toString(),
                        brand = Value.NONE_OTHER,
                        category = Value.NONE_OTHER,
                        variant = Value.NONE_OTHER,
                        productPosition = (index + 1).toString(),
                        channelId = model.id,
                        isFreeOngkir = channelGrid.isFreeOngkirActive && !channelGrid.labelGroup.hasLabelGroupFulfillment(),
                        isFreeOngkirExtra = channelGrid.isFreeOngkirActive && channelGrid.labelGroup.hasLabelGroupFulfillment(),
                        persoType = model.trackingAttributionModel.persoType,
                        categoryId = model.trackingAttributionModel.categoryId,
                        isTopAds = channelGrid.isTopads,
                        isCarousel = false,
                        headerName = model.channelHeader.name,
                        recommendationType = channelGrid.recommendationType,
                        pageName = model.pageName
                    )
                }
            )
            .appendChannelId(model.id)
            .appendUserId(userId)
            .appendScreen(Screen.DEFAULT)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .build()
    }

    fun productClick(model: ChannelModel, attribute: ChannelTracker, userId: String, position: Int): Map<String, Any> {
        val grid = model.channelGrids[position]

        val isTopAds = if (attribute.isTopAds) "topads" else "non topads"
        val isCarousel = if (attribute.isCarousel) "carousel" else "non carousel"
        val recomType = attribute.persoType
        val recomPageName = attribute.recomPageName
        val buType = attribute.buType
        val headerName = model.channelHeader.name

        return BaseTrackerBuilder()
            .constructBasicProductClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventLabel = "%s - %s".format(
                    model.id,
                    model.channelHeader.name
                ),
                eventAction = "click on product dynamic channel 2 square",
                list = "/ - p${position + 1} - dynamic channel 2 square - product - $isTopAds - $isCarousel - $recomType - $recomPageName - $buType - $headerName",
                products = listOf(
                    Product(
                        id = attribute.productId,
                        name = attribute.productName,
                        productPrice = convertRupiahToInt(grid.price).toString(),
                        brand = Value.NONE_OTHER,
                        category = Value.NONE_OTHER,
                        variant = Value.NONE_OTHER,
                        productPosition = (position + 1).toString(),
                        channelId = model.id,
                        isFreeOngkir = grid.isFreeOngkirActive && !grid.labelGroup.hasLabelGroupFulfillment(),
                        isFreeOngkirExtra = grid.isFreeOngkirActive && grid.labelGroup.hasLabelGroupFulfillment(),
                        persoType = model.trackingAttributionModel.persoType,
                        categoryId = attribute.categoryId,
                        isTopAds = attribute.isTopAds,
                        isCarousel = false,
                        headerName = headerName,
                        recommendationType = attribute.recommendationType,
                        pageName = attribute.recomPageName
                    )
                )
            )
            .appendChannelId(model.id)
            .appendCampaignCode(model.trackingAttributionModel.campaignCode)
            .appendScreen(Screen.DEFAULT)
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .build()
    }
}
