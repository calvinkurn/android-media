package com.tokopedia.home.analytics.v2

import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelTracker
import com.tokopedia.home_component.visitable.shorten.ProductWidgetUiModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object Kd2ProductSquareTracker : BaseTrackerConst() {

    private const val TWO_SQUARE_TYPE = "dynamic channel 2 square"
    private const val ITEM_TYPE = "product"

    fun productView(data: ProductWidgetUiModel, userId: String, position: Int): Map<String, Any> {
        val model = data.channelModel
        val atLeastGetFirstProductData = data.data.firstOrNull() ?: return mapOf()

        return BaseTrackerBuilder()
            .constructBasicProductView(
                event = Event.PRODUCT_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventLabel = generateEventLabel(model.id, model.channelHeader.name),
                eventAction = "impression on $ITEM_TYPE $TWO_SQUARE_TYPE",
                list = generateParentItemList(
                    position = position,
                    topAds = IsTopAds(atLeastGetFirstProductData.tracker.isTopAds),
                    carousel = IsCarousel(atLeastGetFirstProductData.tracker.isCarousel),
                    tracker = atLeastGetFirstProductData.tracker,
                    headerName = model.channelHeader.name
                ),
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

    fun productClick(
        model: ChannelModel,
        attribute: ChannelTracker,
        userId: String,
        position: Int
    ): Map<String, Any> {
        val grid = model.channelGrids[position]

        return BaseTrackerBuilder()
            .constructBasicProductClick(
                event = Event.PRODUCT_CLICK,
                eventCategory = Category.HOMEPAGE,
                eventLabel = generateEventLabel(model.id, model.channelHeader.name),
                eventAction = "click on $ITEM_TYPE $TWO_SQUARE_TYPE",
                list = generateParentItemList(
                    position = position,
                    topAds = IsTopAds(attribute.isTopAds),
                    carousel = IsCarousel(attribute.isCarousel),
                    tracker = attribute,
                    headerName = model.channelHeader.name
                ),
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
                        headerName = model.channelHeader.name,
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
            .appendUserId(userId)
            .build()
    }

    private fun generateEventLabel(channelId: String, headerName: String): String {
        return "%s - %s".format(channelId, headerName)
    }

    private fun generateParentItemList(
        position: Int,
        topAds: IsTopAds,
        carousel: IsCarousel,
        tracker: ChannelTracker,
        headerName: String
    ) = "/ - p${position + 1} - $TWO_SQUARE_TYPE - $ITEM_TYPE - %s - %s - %s - %s - %s - %s"
        .format(
            topAds.toString(),
            carousel.toString(),
            tracker.recommendationType,
            tracker.recomPageName,
            tracker.buType,
            headerName
        )

    @JvmInline
    value class IsTopAds(val value: Boolean) {

        override fun toString(): String {
            return if (value) "topads" else "non topads"
        }
    }

    @JvmInline
    value class IsCarousel(val value: Boolean) {

        override fun toString(): String {
            return if (value) "carousel" else "non carousel"
        }
    }
}
