package com.tokopedia.home.analytics.v2

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel.Companion.CAMPAIGN_TYPE_FLASH_SALE_TOKO
import com.tokopedia.home_component.model.TrackingAttributionModel.Companion.CAMPAIGN_TYPE_SPECIAL_RELEASE
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.CLICK_HOMEPAGE
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_VIEW
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

object SpecialReleaseTracking : BaseTrackerConst() {

    private const val IMPRESSION_ON_BANNER_SPECIAL_RELEASE = "impression on banner dynamic channel feature campaign"
    private const val CLICK_ON_BANNER_SPECIAL_RELEASE = "click on banner dynamic channel feature campaign"
    private const val EVENT_ACTION_CLICK_VIEW_ALL_SPECIAL_RELEASE = "click view all on dynamic channel feature campaign"

    //channelID - bannerID - channel.persoType - channel.categoryID
    private const val SPECIAL_RELEASE_PROMO_ID = "%s_%s_%s_%s"

    //creative_name - campaign type - shop_id
    private const val SPECIAL_RELEASE_PROMO_CREATIVE_NAME = "%s - %s - %s"

    private const val SPECIAL_RELEASE_EVENT_LABEL = "%s - %s"

    private const val SPECIAL_RELEASE = "Rilisan Spesial"
    private const val FLASH_SALE = "Flash Sale Toko"

    fun sendSpecialReleaseItemClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) {
        getTracker().sendEnhanceEcommerceEvent(getSpecialReleaseItemClick(channelModel, channelGrid, position, userId))
    }

    fun sendSpecialReleaseItemImpression(trackingQueue: TrackingQueue?, channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) {
        trackingQueue?.putEETracking(getSpecialReleaseItemImpression(channelModel, channelGrid, position, false, userId))
    }

    fun sendSpecialReleaseSeeAllClick(channelModel: ChannelModel) {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
            event = CLICK_HOMEPAGE,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_CLICK_VIEW_ALL_SPECIAL_RELEASE,
            eventLabel = String.format(
                SPECIAL_RELEASE_EVENT_LABEL, channelModel.id, channelModel.channelHeader.name
            )
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(channelModel.id)
        getTracker().sendGeneralEvent(trackerBuilder.build())
    }

    private fun getSpecialReleaseItemClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String) : Map<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionClick(
                    event = Event.PROMO_CLICK,
                    eventCategory = Category.HOMEPAGE,
                    eventAction = CLICK_ON_BANNER_SPECIAL_RELEASE,
                    eventLabel = String.format(
                        SPECIAL_RELEASE_EVENT_LABEL, channelModel.id, channelModel.channelHeader.name
                    ),
                    promotions = listOf(channelGrid.convertToSpecialReleasePromotionModel(channelModel, position)))
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .appendChannelId(channelModel.id)
                .appendCampaignCode(channelModel.trackingAttributionModel.campaignCode)
                .build()
    }

    private fun getSpecialReleaseItemImpression(channel: ChannelModel, grid: ChannelGrid, position: Int, isToIris: Boolean, userId: String): HashMap<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
                event = if (isToIris) Event.PROMO_VIEW_IRIS else PROMO_VIEW,
                eventCategory = Category.HOMEPAGE,
                eventAction = IMPRESSION_ON_BANNER_SPECIAL_RELEASE,
                eventLabel = String.format(
                    SPECIAL_RELEASE_EVENT_LABEL, channel.id, channel.channelHeader.name
                ),
                promotions = listOf(grid.convertToSpecialReleasePromotionModel(channel, position)))
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(userId)
                .build() as HashMap<String, Any>
    }

    private fun ChannelGrid.convertToSpecialReleasePromotionModel(channelModel: ChannelModel, position: Int) = Promotion(
            id = String.format(
                SPECIAL_RELEASE_PROMO_ID,
                channelModel.id,
                id,
                channelModel.trackingAttributionModel.persoType,
                channelModel.trackingAttributionModel.categoryId),
            name = channelModel.trackingAttributionModel.promoName,
            creative = String.format(
                SPECIAL_RELEASE_PROMO_CREATIVE_NAME,
                channelModel.name,
                when(channelModel.trackingAttributionModel.campaignType) {
                    CAMPAIGN_TYPE_SPECIAL_RELEASE -> SPECIAL_RELEASE
                    CAMPAIGN_TYPE_FLASH_SALE_TOKO -> FLASH_SALE
                    else -> ""
                },
                shop.id
            ),
            position = (position + 1).toString()
    )

    private fun ChannelModel.convertToHomePromotionModelList(position: Int) =
            this.channelGrids.map { it.convertToSpecialReleasePromotionModel(this, position) }
}