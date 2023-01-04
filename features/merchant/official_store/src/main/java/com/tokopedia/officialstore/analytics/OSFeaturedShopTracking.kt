package com.tokopedia.officialstore.analytics

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_CLICK_BANNER
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_DASH_FIVE_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_IMPRESSION_BANNER
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_ITEM_NAME_FOUR_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_UNDERSCORE_THREE_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.OS_MICROSITE_SINGLE
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_CLICK
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_VIEW

object OSFeaturedShopTracking: BaseTrackerConst() {
    private const val VALUE_FEATURED_SHOP = "dynamic channel shop"
    private const val VALUE_TRACKER_ID_CLICK_CATEGORY = "19948"
    private const val VALUE_TRACKER_ID_IMPRESSION_CATEGORY = "19949"

    // Row 31
    fun getEventClickShopWidget(
        channel: ChannelModel,
        grid: ChannelGrid,
        categoryName: String,
        bannerPosition: Int,
        userId: String
    ) {
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Category.KEY, OS_MICROSITE_SINGLE)
            putString(Action.KEY, FORMAT_CLICK_BANNER.format(VALUE_FEATURED_SHOP))
            putString(Label.KEY, FORMAT_DASH_FIVE_VALUES.format(
                VALUE_FEATURED_SHOP, channel.id, channel.channelHeader.name,
                channel.trackingAttributionModel.brandId, categoryName))
            putString(UserId.KEY, userId)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            val promotions = arrayListOf(
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, grid.attribution)
                    putString(Promotion.CREATIVE_SLOT, (bannerPosition+1).toString())
                    putString(Promotion.ITEM_ID, FORMAT_UNDERSCORE_THREE_VALUES.format(channel.channelBanner.id, channel.id, grid.shop.id))
                    putString(Promotion.ITEM_NAME, FORMAT_ITEM_NAME_FOUR_VALUES.format(
                        categoryName, VALUE_FEATURED_SHOP,
                        channel.channelHeader.name, grid.applink))
                }
            )
            putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_CATEGORY)
            putParcelableArrayList(Promotion.KEY, promotions)
        }
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    // Row 32
    fun getEventImpressionShopWidget(channel: ChannelModel, grid: ChannelGrid, categoryName: String, bannerPosition: Int, userId: String) : HashMap<String, Any> {
        return BaseTrackerBuilder()
            .constructBasicPromotionView(
                event = PROMO_VIEW,
                eventAction = FORMAT_IMPRESSION_BANNER.format(VALUE_FEATURED_SHOP),
                eventCategory = OS_MICROSITE_SINGLE,
                eventLabel = FORMAT_DASH_FIVE_VALUES.format(
                    VALUE_FEATURED_SHOP, channel.id, channel.channelHeader.name,
                    channel.trackingAttributionModel.brandId, categoryName),
                promotions = listOf(
                    Promotion(
                        id = FORMAT_UNDERSCORE_THREE_VALUES.format(
                            channel.channelBanner.id, channel.id, grid.shop.id),
                        name = FORMAT_ITEM_NAME_FOUR_VALUES.format(
                            categoryName, VALUE_FEATURED_SHOP,
                            channel.channelHeader.name, grid.applink),
                        position = bannerPosition.toString(),
                        creative = grid.attribution,
                    )
                ))
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .appendCustomKeyValue(TrackerId.KEY, VALUE_TRACKER_ID_IMPRESSION_CATEGORY)
            .build() as HashMap<String, Any>
    }
}
