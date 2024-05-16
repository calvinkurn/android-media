package com.tokopedia.home.analytics.v2

import com.tokopedia.home.analytics.HomePageTracking
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.ChannelTracker
import com.tokopedia.home_component.visitable.shorten.ProductWidgetUiModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object Kd2SquareTracker : BaseTrackerConst() {

    // TrackerID: 50030
    private const val IMPRESSION_TRACKER_ID = "50030"
    fun widgetImpressed(data: ProductWidgetUiModel, userId: String, position: Int): Map<String, Any> {
        val shouldAtLeastContainSingleDataAsProduct = data.data.firstOrNull()?.tracker?.isProduct() ?: false

        val type = if (shouldAtLeastContainSingleDataAsProduct) Const.PRODUCT else Const.BANNER
        val name = if (shouldAtLeastContainSingleDataAsProduct) Const.PRODUCT_NAME else Const.CARD_NAME

        val action = "impression on $name"
        val attribute = data.channelModel.trackingAttributionModel

        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionView(
            event = HomePageTracking.PROMO_VIEW,
            eventAction = action,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "${attribute.channelId} - ${attribute.headerName}",
            promotions = data.channelModel.channelGrids.mapIndexed { index, channelGrid ->
                Promotion(
                    id = "${attribute.channelId}_${attribute.bannerId}_${attribute.categoryId}_${attribute.persoType}",
                    name = "/ - p${position + 1} - ${Const.NAME} - $type - ${attribute.headerName}",
                    position = (index + 1).toString(),
                    creative = channelGrid.attribution
                )
            }
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(attribute.channelId)
            .appendCampaignCode(attribute.campaignCode)
            .appendCustomKeyValue(TrackerId.KEY, IMPRESSION_TRACKER_ID)
            .appendUserId(userId)

        return trackingBuilder.build()
    }

    // TrackerID: 50031
    private const val CARD_CLICK_TRACKER_ID = "50031"
    fun cardClicked(attribute: ChannelTracker, userId: String, position: Int): Map<String, Any> {
        val type = if (attribute.isProduct()) Const.PRODUCT else Const.BANNER
        val name = if (attribute.isProduct()) Const.PRODUCT_NAME else Const.CARD_NAME

        val action = "click on $name"

        val trackingBuilder = BaseTrackerBuilder().constructBasicPromotionClick(
            event = HomePageTracking.PROMO_CLICK,
            eventAction = action,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "${attribute.channelId} - ${attribute.headerName}",
            promotions = listOf(
                Promotion(
                    id = "${attribute.channelId}_${attribute.bannerId}_${attribute.categoryId}_${attribute.persoType}",
                    name = "/ - p${position + 1} - ${Const.NAME} - $type - ${attribute.headerName}",
                    position = (position + 1).toString(),
                    creative = attribute.attribution
                )
            )
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(attribute.channelId)
            .appendCustomKeyValue(TrackerId.KEY, CARD_CLICK_TRACKER_ID)
            .appendCampaignCode(attribute.campaignCode)
            .appendUserId(userId)

        return trackingBuilder.build()
    }

    // TrackerID: 50032
    private const val CHEVRON_CLICK_TRACKER_ID = "50032"
    fun channelHeaderClicked(model: ChannelModel): Map<String, Any> {
        val action = "click view all chevron on ${Const.NAME}"
        val attribute = model.trackingAttributionModel

        val trackingBuilder = BaseTrackerBuilder().constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventAction = action,
            eventCategory = Category.HOMEPAGE,
            eventLabel = "${attribute.channelId} - ${attribute.headerName}"
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(attribute.channelId)
            .appendCustomKeyValue(TrackerId.KEY, CHEVRON_CLICK_TRACKER_ID)
            .appendCampaignCode(attribute.campaignCode)

        return trackingBuilder.build()
    }

    object Const {
        const val NAME = "dynamic channel 2 square"

        const val BANNER = "banner"
        const val PRODUCT = "product"

        const val CARD_NAME = "$BANNER $NAME"
        const val PRODUCT_NAME = "$PRODUCT $NAME"
    }
}
