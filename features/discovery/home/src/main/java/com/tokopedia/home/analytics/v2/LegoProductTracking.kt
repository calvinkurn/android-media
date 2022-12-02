package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.homenav.common.TrackingConst
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.CLICK_HOMEPAGE
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PRODUCT_CLICK
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PRODUCT_VIEW

/**
 * Created by frenzel
 */
object LegoProductTracking : BaseTrackerConst() {
    private const val LEGO_4_PRODUCT_SIZE = 4

    private const val LIST_LEGO_PRODUCT = "/ - p%s - %s - product - %s - %s - %s - %s - %s - %s"
    private const val CLICK_VIEW_ALL_ON_PRODUCT = "click view all on product %s"
    private const val IMPRESSION_ON_PRODUCT = "impression on product %s"
    private const val CLICK_ON_PRODUCT = "click on product %s"

    private const val TRACKER_ID = "trackerId"
    private const val KEY_DIMENSION_84 = "dimension84"
    private const val KEY_DIMENSION_96 = "dimension96"
    private const val TOPADS = "topads"
    private const val NONTOPADS = "non topads"
    private const val NON_CAROUSEL = "non carousel"

    private const val LEGO_4_PRODUCT_NAME = "dynamic channel 4 product"
    private const val TRACKER_ID_LEGO_4_PRODUCT_VIEW_ALL = "37306"
    private const val TRACKER_ID_LEGO_4_PRODUCT_IMPRESSION = "37307"
    private const val TRACKER_ID_LEGO_4_PRODUCT_CLICK = "37308"

    private fun getItemList(
        position: Int,
        trackerName: String,
        channel: ChannelModel,
        isTopAds: Boolean? = null,
        recomType: String = ""
    ): String {
        return LIST_LEGO_PRODUCT.format(
            position + 1,
            trackerName,
            if(isTopAds == null) "" else if (isTopAds) TOPADS else NONTOPADS,
            NON_CAROUSEL,
            recomType,
            channel.pageName,
            channel.trackingAttributionModel.galaxyAttribution,
            channel.channelHeader.name
        )
    }

    fun getLego4ProductImpression(
        channel: ChannelModel,
        position: Int,
        userId: String
    ): HashMap<String, Any> {
        val trackerBuilder = BaseTrackerBuilder()
        return trackerBuilder.constructBasicProductView(
            event = PRODUCT_VIEW,
            eventCategory = Category.HOMEPAGE,
            eventAction = IMPRESSION_ON_PRODUCT.format(LEGO_4_PRODUCT_NAME),
            eventLabel = Label.NONE,
            list = getItemList(trackerName = LEGO_4_PRODUCT_NAME, channel = channel, position = position),
            products = channel.channelGrids.take(LEGO_4_PRODUCT_SIZE).mapIndexed { index, grid ->
                Product(
                    name = grid.name,
                    id = grid.id,
                    productPrice = convertRupiahToInt(
                        grid.price
                    ).toString(),
                    brand = Value.NONE_OTHER,
                    category = Value.NONE_OTHER,
                    variant = Value.NONE_OTHER,
                    productPosition = (index + 1).toString(),
                    channelId = channel.id,
                    isFreeOngkir = grid.isFreeOngkirActive && !grid.labelGroup.hasLabelGroupFulfillment(),
                    isFreeOngkirExtra = grid.isFreeOngkirActive && grid.labelGroup.hasLabelGroupFulfillment(),
                    persoType = channel.trackingAttributionModel.persoType,
                    categoryId = channel.trackingAttributionModel.categoryId,
                    isTopAds = grid.isTopads,
                    recommendationType = grid.recommendationType,
                    headerName = channel.channelHeader.name,
                    pageName = channel.pageName,
                    isCarousel = true
                )
            }, buildCustomList = { getItemList(position, LEGO_4_PRODUCT_NAME, channel, it.isTopAds, it.recommendationType,) })
            .appendBusinessUnit(TrackingConst.DEFAULT_BUSINESS_UNIT)
            .appendCurrentSite(TrackingConst.DEFAULT_CURRENT_SITE)
            .appendUserId(userId)
            .appendCustomKeyValue(TRACKER_ID, TRACKER_ID_LEGO_4_PRODUCT_IMPRESSION)
            .build() as HashMap<String, Any>
    }

    fun sendLego4ProductItemClick(channel: ChannelModel, grid: ChannelGrid, position: Int, userId: String) {
        val bundle = Bundle().apply {
            putString(Event.KEY, Event.SELECT_CONTENT)
            putString(Action.KEY, CLICK_ON_PRODUCT.format(LEGO_4_PRODUCT_NAME))
            putString(Category.KEY, Category.HOMEPAGE)
            putString(
                Label.KEY,
                Label.FORMAT_2_ITEMS.format(
                    channel.id, channel.channelHeader.name
                )
            )
            putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
            putString(CurrentSite.KEY, CurrentSite.DEFAULT)
            putString(UserId.KEY, userId)
            putString(TRACKER_ID, TRACKER_ID_LEGO_4_PRODUCT_CLICK)
            putString(ItemList.KEY, getItemList(channel.verticalPosition, LEGO_4_PRODUCT_NAME, channel, grid.isTopads, grid.recommendationType))
            val items = Bundle().apply {
                putString(Items.DIMENSION_83, Items.DIMENSION_83_DEFAULT)
                putString(KEY_DIMENSION_84, channel.id)
                putString(KEY_DIMENSION_96, Value.FORMAT_2_ITEMS_UNDERSCORE.format(
                    channel.trackingAttributionModel.persoType, channel.trackingAttributionModel.categoryId
                ))
                putString(Items.INDEX, (position + 1).toString())
                putString(Items.ITEM_BRAND, Value.NONE_OTHER)
                putString(Items.ITEM_CATEGORY, Value.NONE_OTHER)
                putString(Items.ITEM_VARIANT, Value.NONE_OTHER)
                putString(Items.ITEM_ID, grid.id)
                putString(Items.ITEM_NAME, grid.name)
                putFloat(Items.PRICE, convertRupiahToInt(
                    grid.price
                ).toFloat())
            }
            putParcelableArrayList(Items.KEY, arrayListOf(items))
        }
        getTracker().sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, bundle)
    }

    fun sendLego4ProductSeeAllClick(channel: ChannelModel) {
        val bundle = Bundle().apply {
            putString(Event.KEY, CLICK_HOMEPAGE)
            putString(Category.KEY, Category.HOMEPAGE)
            putString(Action.KEY, CLICK_VIEW_ALL_ON_PRODUCT.format(LEGO_4_PRODUCT_NAME))
            putString(Label.KEY, Label.FORMAT_2_ITEMS.format(
                channel.id, channel.channelHeader.name
            ))
            putString(CurrentSite.KEY, TrackingConst.DEFAULT_CURRENT_SITE)
            putString(BusinessUnit.KEY, TrackingConst.DEFAULT_BUSINESS_UNIT)
            putString(ChannelId.KEY, channel.id)
            putString(TRACKER_ID, TRACKER_ID_LEGO_4_PRODUCT_VIEW_ALL)
        }
        getTracker().sendEnhanceEcommerceEvent(CLICK_HOMEPAGE, bundle)
    }
}
