package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object FlashSaleTracking: BaseTrackerConst() {
    private const val FLASH_SALE = "product dynamic channel flash sale"
    private const val EVENT_ACTION_CLICK_CHEVRON_CTA = "click chevron button on dynamic channel flash sale"
    private const val EVENT_ACTION_CLICK_VIEW_ALL_CARD = "click view all card on dynamic channel flash sale"
    private const val TRACKER_ID_IMPRESSION = "43653"
    private const val TRACKER_ID_CLICK = "43654"
    private const val TRACKER_ID_CLICK_CHEVRON_CTA = "43655"
    private const val TRACKER_ID_CLICK_VIEW_ALL_CARD = "43656"
    private const val EVENT_LABEL_FORMAT = "%s - %s"
    private const val ITEM_LIST_FORMAT = "/ - p%s - dynamic channel flash sale - product - %s - carousel - %s - %s - campaign - %s - %s"
    private const val TOPADS = "topads"
    private const val NON_TOPADS = "non topads"
    private const val CURRENCY_CODE = "currencyCode"
    private const val IDR = "IDR"
    private const val IMPRESSIONS = "impressions"
    private const val DIMENSION_40 = "dimension40"
    private const val DIMENSION_84 = "dimension84"
    private const val DIMENSION_96 = "dimension96"
    private const val DIMENSION_96_FORMAT = "%s_%s"

    fun getImpressionEvent(grid: ChannelGrid, channel: ChannelModel, userId: String): Map<String, Any>? {
        return DataLayer.mapOf(
            Event.KEY, Event.PRODUCT_VIEW,
            Action.KEY, Action.IMPRESSION_ON.format(FLASH_SALE),
            Category.KEY, Category.HOMEPAGE,
            Label.KEY, EVENT_LABEL_FORMAT.format(channel.id, channel.channelHeader.name),
            BusinessUnit.KEY, BusinessUnit.DEFAULT,
            CurrentSite.KEY, CurrentSite.DEFAULT,
            Ecommerce.KEY, DataLayer.mapOf(
                CURRENCY_CODE, IDR,
                IMPRESSIONS, arrayListOf(getProductImpression(grid, channel))
            ),
            UserId.KEY, userId,
            ItemList.KEY, ITEM_LIST_FORMAT.format(
                channel.verticalPosition,
                Value.EMPTY,
                Value.EMPTY,
                Value.EMPTY,
                Value.EMPTY,
                channel.channelHeader.name
            ),
            TrackerId.KEY, TRACKER_ID_IMPRESSION
        ) as? HashMap<String, Any>
    }

    private fun getProductImpression(grid: ChannelGrid, channel: ChannelModel): Map<String, Any> {
        return mapOf(
            DIMENSION_40 to ITEM_LIST_FORMAT.format(
                channel.verticalPosition,
                if(grid.isTopads) TOPADS else NON_TOPADS,
                grid.recommendationType,
                Value.EMPTY,
                grid.shopId,
                channel.channelHeader.name
            ),
            DIMENSION_84 to channel.id,
            DIMENSION_96 to DIMENSION_96_FORMAT.format(
                channel.trackingAttributionModel.persoType,
                channel.trackingAttributionModel.categoryId
            ),
            Items.INDEX to (grid.position + 1).toString(),
            Items.ITEM_BRAND to Value.NONE_OTHER,
            Items.ITEM_CATEGORY to grid.categoryBreadcrumbs,
            Items.ITEM_ID to grid.id,
            Items.ITEM_NAME to grid.name,
            Items.ITEM_VARIANT to Value.NONE_OTHER,
            Items.PRICE to convertRupiahToInt(grid.price).toFloat().toString()
        )
    }

    fun sendClickEvent(grid: ChannelGrid, channel: ChannelModel, userId: String) {
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Action.KEY, Action.CLICK_ON.format(FLASH_SALE))
            putString(Category.KEY, Category.HOMEPAGE)
            putString(Label.KEY, EVENT_LABEL_FORMAT.format(channel.id, channel.channelHeader.name))
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            putString(UserId.KEY, userId)
            putString(ItemList.KEY, ITEM_LIST_FORMAT.format(
                channel.verticalPosition,
                if(grid.isTopads) TOPADS else NON_TOPADS,
                grid.recommendationType,
                Value.EMPTY,
                grid.shopId,
                channel.channelHeader.name
            ))
            putParcelableArrayList(Items.KEY, arrayListOf(Bundle().apply {
                putString(DIMENSION_40, ITEM_LIST_FORMAT.format(
                    channel.verticalPosition,
                    if(grid.isTopads) TOPADS else NON_TOPADS,
                    grid.recommendationType,
                    Value.EMPTY,
                    grid.shopId,
                    channel.channelHeader.name
                ))
                putString(DIMENSION_84, channel.id)
                putString(DIMENSION_96, DIMENSION_96_FORMAT.format(
                    channel.trackingAttributionModel.persoType,
                    channel.trackingAttributionModel.categoryId
                ))
                putString(Items.INDEX, (grid.position + 1).toString())
                putString(Items.ITEM_BRAND, Value.NONE_OTHER)
                putString(Items.ITEM_CATEGORY, grid.categoryBreadcrumbs)
                putString(Items.ITEM_ID, grid.id)
                putString(Items.ITEM_NAME, grid.name)
                putString(Items.ITEM_VARIANT, Value.NONE_OTHER)
                putString(Items.PRICE, convertRupiahToInt(grid.price).toFloat().toString())
            }))
            putString(TrackerId.KEY, TRACKER_ID_CLICK)
        }
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    fun sendHeaderCtaClickEvent(channel: ChannelModel) {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_CLICK_CHEVRON_CTA,
            eventLabel = EVENT_LABEL_FORMAT.format(channel.id, channel.channelHeader.name)
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(channel.id)
            .appendCustomKeyValue(TrackerId.KEY, TRACKER_ID_CLICK_CHEVRON_CTA)

        getTracker().sendGeneralEvent(trackerBuilder.build())
    }

    fun sendViewAllCardClickEvent(channel: ChannelModel) {
        val trackerBuilder = BaseTrackerBuilder()
        trackerBuilder.constructBasicGeneralClick(
            event = Event.CLICK_HOMEPAGE,
            eventCategory = Category.HOMEPAGE,
            eventAction = EVENT_ACTION_CLICK_VIEW_ALL_CARD,
            eventLabel = EVENT_LABEL_FORMAT.format(channel.id, channel.channelHeader.name)
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendChannelId(channel.id)
            .appendCustomKeyValue(TrackerId.KEY, TRACKER_ID_CLICK_VIEW_ALL_CARD)

        getTracker().sendGeneralEvent(trackerBuilder.build())
    }
}
