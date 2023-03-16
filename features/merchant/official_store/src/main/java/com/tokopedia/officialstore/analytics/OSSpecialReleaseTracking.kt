package com.tokopedia.officialstore.analytics

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.model.TrackingAttributionModel.Companion.CAMPAIGN_TYPE_FLASH_SALE_TOKO
import com.tokopedia.home_component.model.TrackingAttributionModel.Companion.CAMPAIGN_TYPE_SPECIAL_RELEASE
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.ATTRIBUTION
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_CLICK_ON_BANNER
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_CLICK_VIEW_ALL_CARD_ON
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_CLICK_VIEW_ALL_CARD_ON_BANNER
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_CLICK_VIEW_ALL_ON_BANNER
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_DASH_FOUR_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_DASH_THREE_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_IMPRESSION_ON_BANNER
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_ITEM_NAME_FOUR_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_UNDERSCORE_TWO_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.OS_MICROSITE_SINGLE
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.CLICK_HOMEPAGE
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_CLICK
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_VIEW
import com.tokopedia.trackingoptimizer.TrackingQueue
import java.util.HashMap

object OSSpecialReleaseTracking : BaseTrackerConst() {

    private const val VALUE_FEATURE_CAMPAIGN = "dynamic channel feature campaign"
    private const val SPECIAL_RELEASE = "Rilisan Spesial"
    private const val FLASH_SALE = "Flash Sale Toko"
    private const val VALUE_TRACKER_ID_IMPRESSION_FEATURE_CAMPAIGN = "30352"
    private const val VALUE_TRACKER_ID_CLICK_FEATURE_CAMPAIGN = "30353"
    private const val VALUE_TRACKER_ID_CLICK_FEATURE_CAMPAIGN_VIEW_ALL = "30354"
    private const val VALUE_TRACKER_ID_CLICK_FEATURE_CAMPAIGN_VIEW_ALL_CARD = "30355"

    // Row 38
    fun sendSpecialReleaseItemImpression(trackingQueue: TrackingQueue?, channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String, categoryName: String) {
        trackingQueue?.putEETracking(getSpecialReleaseItemImpression(channelModel, channelGrid, position, userId, categoryName))
    }

    private fun getSpecialReleaseItemImpression(channel: ChannelModel, grid: ChannelGrid, position: Int, userId: String, categoryName: String): HashMap<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicPromotionView(
            event = PROMO_VIEW,
            eventCategory = OS_MICROSITE_SINGLE,
            eventAction = FORMAT_IMPRESSION_ON_BANNER.format(VALUE_FEATURE_CAMPAIGN),
            eventLabel = String.format(
                FORMAT_DASH_FOUR_VALUES.format(VALUE_FEATURE_CAMPAIGN, channel.id, channel.channelHeader.name, categoryName)
            ),
            promotions = listOf(
                Promotion(
                    id = FORMAT_UNDERSCORE_TWO_VALUES.format(
                        channel.channelBanner.id,
                        channel.id
                    ),
                    name = FORMAT_ITEM_NAME_FOUR_VALUES.format(
                        categoryName,
                        VALUE_FEATURE_CAMPAIGN,
                        channel.channelHeader.name,
                        grid.applink
                    ),
                    creative = String.format(
                        FORMAT_DASH_THREE_VALUES,
                        channel.name,
                        when(channel.trackingAttributionModel.campaignType) {
                            CAMPAIGN_TYPE_SPECIAL_RELEASE -> SPECIAL_RELEASE
                            CAMPAIGN_TYPE_FLASH_SALE_TOKO -> FLASH_SALE
                            else -> ""
                        },
                        grid.shop.id
                    ),
                    position = (position + 1).toString()
                )
            ))
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendCustomKeyValue(TrackerId.KEY, VALUE_TRACKER_ID_IMPRESSION_FEATURE_CAMPAIGN)
            .build() as HashMap<String, Any>
    }

    // Row 39
    fun sendSpecialReleaseItemClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String, categoryName: String) {
        val bundle = getSpecialReleaseItemClick(
            channelModel, channelGrid, position, userId, categoryName
        )
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    private fun getSpecialReleaseItemClick(channelModel: ChannelModel, channelGrid: ChannelGrid, position: Int, userId: String, categoryName: String) : Bundle {
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Category.KEY, OS_MICROSITE_SINGLE)
            putString(Action.KEY, FORMAT_CLICK_ON_BANNER.format(VALUE_FEATURE_CAMPAIGN))
            putString(Label.KEY, FORMAT_DASH_FOUR_VALUES.format(
                VALUE_FEATURE_CAMPAIGN, channelModel.id, channelModel.channelHeader.name, categoryName))
            putString(UserId.KEY, userId)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            val promotions = arrayListOf(
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, String.format(
                        FORMAT_DASH_THREE_VALUES,
                        channelModel.name,
                        when(channelModel.trackingAttributionModel.campaignType) {
                            CAMPAIGN_TYPE_SPECIAL_RELEASE -> SPECIAL_RELEASE
                            CAMPAIGN_TYPE_FLASH_SALE_TOKO -> FLASH_SALE
                            else -> ""
                        },
                        channelGrid.shop.id
                    ))
                    putString(Promotion.CREATIVE_SLOT, (position+1).toString())
                    putString(Promotion.ITEM_ID, FORMAT_UNDERSCORE_TWO_VALUES.format(channelModel.channelBanner.id, channelModel.id))
                    putString(Promotion.ITEM_NAME,  FORMAT_ITEM_NAME_FOUR_VALUES.format(
                        categoryName,
                        VALUE_FEATURE_CAMPAIGN,
                        channelModel.channelHeader.name,
                        channelGrid.applink
                    ))
                }
            )
            putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_FEATURE_CAMPAIGN)
            putString(ATTRIBUTION, channelGrid.attribution)
            putParcelableArrayList(Promotion.KEY, promotions)
        }
        return bundle
    }

    // Row 40
    fun sendSpecialReleaseSeeAllClick(channelModel: ChannelModel, categoryName: String) {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
            event = CLICK_HOMEPAGE,
            eventCategory = OS_MICROSITE_SINGLE,
            eventAction = FORMAT_CLICK_VIEW_ALL_ON_BANNER.format(VALUE_FEATURE_CAMPAIGN),
            eventLabel = FORMAT_DASH_FOUR_VALUES.format(
                VALUE_FEATURE_CAMPAIGN, channelModel.id, channelModel.channelHeader.name, categoryName
            ))
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendCustomKeyValue(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_FEATURE_CAMPAIGN_VIEW_ALL)
        getTracker().sendGeneralEvent(trackerBuilder.build())
    }

    // Row 41
    fun sendSpecialReleaseSeeAllCardClick(channelModel: ChannelModel, categoryName: String) {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
            event = CLICK_HOMEPAGE,
            eventCategory = OS_MICROSITE_SINGLE,
            eventAction = FORMAT_CLICK_VIEW_ALL_CARD_ON_BANNER.format(VALUE_FEATURE_CAMPAIGN),
            eventLabel = FORMAT_DASH_FOUR_VALUES.format(
                VALUE_FEATURE_CAMPAIGN, channelModel.id, channelModel.channelHeader.name, categoryName
            ))
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendCustomKeyValue(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_FEATURE_CAMPAIGN_VIEW_ALL_CARD)
        getTracker().sendGeneralEvent(trackerBuilder.build())
    }

}
