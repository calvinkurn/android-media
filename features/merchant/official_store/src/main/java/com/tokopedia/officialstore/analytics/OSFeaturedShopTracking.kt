package com.tokopedia.officialstore.analytics

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_CLICK
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_VIEW

object OSFeaturedShopTracking: BaseTrackerConst() {
    private const val VALUE_FEATURED_SHOP = "dynamic channel shop"
    private const val VALUE_TRACKER_ID_CLICK_CATEGORY = "19948"

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
            putString(Category.KEY, OfficialStoreTracking.OS_MICROSITE_SINGLE)
            putString(Action.KEY, OfficialStoreTracking.FORMAT_CLICK_BANNER.format(VALUE_FEATURED_SHOP))
            putString(Label.KEY, OfficialStoreTracking.FORMAT_DASH_FIVE_VALUES.format(
                VALUE_FEATURED_SHOP, channel.id, channel.channelHeader.name,
                grid.shopId, categoryName))
            putString(UserId.KEY, userId)
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            val promotions = arrayListOf(
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, grid.attribution)
                    putString(Promotion.CREATIVE_SLOT, (bannerPosition+1).toString())
                    putString(Promotion.ITEM_ID, OfficialStoreTracking.FORMAT_DASH_THREE_VALUES.format(channel.channelBanner.id, channel.id, grid.shopId))
                    putString(Promotion.ITEM_NAME, OfficialStoreTracking.FORMAT_ITEM_NAME.format(categoryName, VALUE_FEATURED_SHOP))
                }
            )
            putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_CATEGORY)
            putParcelableArrayList(Promotion.KEY, promotions)
        }
        getTracker().sendEnhanceEcommerceEvent(PROMO_CLICK, bundle)
    }

    // Row 32
    fun getEventImpressionShopWidget(channel: ChannelModel, grid: ChannelGrid, categoryName: String, bannerPosition: Int, userId: String) : HashMap<String, Any> {
        return BaseTrackerBuilder()
            .constructBasicPromotionView(
                event = PROMO_VIEW,
                eventAction = OfficialStoreTracking.FORMAT_IMPRESSION_BANNER.format(VALUE_FEATURED_SHOP),
                eventCategory = OfficialStoreTracking.OS_MICROSITE_SINGLE,
                eventLabel = OfficialStoreTracking.FORMAT_DASH_FIVE_VALUES.format(
                    VALUE_FEATURED_SHOP, channel.id, channel.channelHeader.id,
                    grid.shop.id, categoryName),
                promotions = listOf(
                    Promotion(
                        id = OfficialStoreTracking.FORMAT_DASH_THREE_VALUES.format(
                            channel.channelBanner.id, channel.id, grid.shopId),
                        name = OfficialStoreTracking.FORMAT_ITEM_NAME_FOUR_VALUES.format(
                            categoryName, VALUE_FEATURED_SHOP,
                            channel.channelHeader.name, grid.applink),
                        position = bannerPosition.toString(),
                        creative = grid.attribution,
                    )
                ))
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(userId)
            .build() as HashMap<String, Any>
    }
}
