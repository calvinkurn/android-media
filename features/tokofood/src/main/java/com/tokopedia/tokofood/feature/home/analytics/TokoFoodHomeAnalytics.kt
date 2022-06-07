package com.tokopedia.tokofood.feature.home.analytics

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_CLICK_CAROUSEL_BANNER
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_CLICK_CATEGORY_ICONS
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_CLICK_CATEGORY_WIDGET
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_CLICK_LEGO_SIX
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_VIEW_CAROUSEL_BANNER
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_VIEW_CATEGORY_ICONS
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_VIEW_CATEGORY_WIDGET
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_VIEW_LEGO_SIX
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.EMPTY_DATA
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.GOFOOD_PAGENAME
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.VIEW_ITEM
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeLayoutType
import com.tokopedia.tokofood.feature.home.domain.data.DynamicIcon
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.SELECT_CONTENT

/**
 * Home
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3053
 */
class TokoFoodHomeAnalytics: BaseTrackerConst() {

    fun clickLCAWidget(userId: String?, destinationId: String?) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalytics.EVENT_ACTION_CLICK_LCA)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.clickPG(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.CLICK_PG, eventDataLayer)
    }

    fun clickIconWidget(userId: String?, destinationId: String?, data: List<DynamicIcon>, horizontalPosition: Int, verticalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_CLICK_CATEGORY_ICONS)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionItemIcon(data, horizontalPosition, verticalPosition))
        eventDataLayer.selectContent(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun impressionIconWidget(userId: String?, destinationId: String?, data: List<DynamicIcon>, verticalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_VIEW_CATEGORY_ICONS)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionItemIcon(data, verticalPosition = verticalPosition))
        eventDataLayer.viewItem(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventDataLayer)
    }

    fun clickBannerWidget(userId: String?, destinationId: String?, channelModel: ChannelModel, channelGrid: ChannelGrid, horizontalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_CLICK_CAROUSEL_BANNER)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionItemBanner(channelModel, listOf(channelGrid), horizontalPosition))
        eventDataLayer.selectContent(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun impressBannerWidget(userId: String?, destinationId: String?, channelModel: ChannelModel) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_VIEW_CAROUSEL_BANNER)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionItemBanner(channelModel, channelModel.channelGrids))
        eventDataLayer.viewItem(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventDataLayer)
    }

    fun clickLego(userId: String?, destinationId: String?, channelModel: ChannelModel, channelGrid: ChannelGrid, horizontalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_CLICK_LEGO_SIX)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionLego(channelModel, listOf(channelGrid), horizontalPosition))
        eventDataLayer.selectContent(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun impressLego(userId: String?, destinationId: String?, channelModel: ChannelModel) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_VIEW_LEGO_SIX)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionLego(channelModel, channelModel.channelGrids))
        eventDataLayer.viewItem(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventDataLayer)
    }

    fun clickCategory(userId: String?, destinationId: String?, channelModel: ChannelModel, channelGrid: ChannelGrid, horizontalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_CLICK_CATEGORY_WIDGET)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionCategory(channelModel, listOf(channelGrid), horizontalPosition))
        eventDataLayer.selectContent(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun impressCategory(userId: String?, destinationId: String?, channelModel: ChannelModel) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_VIEW_CATEGORY_WIDGET)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionCategory(channelModel, channelModel.channelGrids))
        eventDataLayer.viewItem(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventDataLayer)
    }

    private fun getPromotionItemIcon(data: List<DynamicIcon>, horizontalPosition: Int = -1, verticalPosition: Int): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.addAll(
            data.mapIndexed { index, it ->
                val position = if (horizontalPosition.isLessThanZero()) index else horizontalPosition
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, "${it.imageUrl} - ${it.applinks}")
                    putString(Promotion.CREATIVE_SLOT, (position + 1).toString())
                    putString(Promotion.ITEM_ID, it.name)
                    putString(Promotion.ITEM_NAME, "$GOFOOD_PAGENAME - ${TokoFoodHomeLayoutType.ICON_TOKOFOOD} - ${verticalPosition + 1} - $EMPTY_DATA")
                }
            }
        )
        return promotionBundle
    }

    private fun getPromotionItemBanner(channelModel: ChannelModel, channelGrids: List<ChannelGrid>, horizontalPosition: Int = -1): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.addAll(
            channelGrids.mapIndexed { index, it ->
                val position = if (horizontalPosition.isLessThanZero()) index else horizontalPosition
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, "${it.imageUrl} - ${it.applink}")
                    putString(Promotion.CREATIVE_SLOT, (position + 1).toString())
                    putString(Promotion.ITEM_ID, it.id)
                    putString(Promotion.ITEM_NAME, "$GOFOOD_PAGENAME - ${TokoFoodHomeLayoutType.BANNER_CAROUSEL} - ${channelModel.verticalPosition + 1} - ${channelModel.channelHeader.name}")
                }
            }
        )
        return promotionBundle
    }

    private fun getPromotionLego(channelModel: ChannelModel, channelGrids: List<ChannelGrid>, horizontalPosition: Int = -1): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.addAll(
            channelGrids.mapIndexed { index, it ->
                val position = if (horizontalPosition.isLessThanZero()) index else horizontalPosition
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, "${it.imageUrl} - ${it.applink}")
                    putString(Promotion.CREATIVE_SLOT, (position + 1).toString())
                    putString(Promotion.ITEM_ID, "${it.id} - ${it.shopId}")
                    putString(Promotion.ITEM_NAME, "$GOFOOD_PAGENAME - ${TokoFoodHomeLayoutType.LEGO_6_IMAGE} - ${channelModel.verticalPosition + 1} - ${channelModel.channelHeader.name}")
                }
            }
        )
        return promotionBundle
    }

    private fun getPromotionCategory(channelModel: ChannelModel, channelGrids: List<ChannelGrid>, horizontalPosition: Int = -1): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.addAll(
            channelGrids.mapIndexed { index, it ->
                val position = if (horizontalPosition.isLessThanZero()) index else horizontalPosition
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, "${it.imageUrl} - ${it.applink}")
                    putString(Promotion.CREATIVE_SLOT, (position + 1).toString())
                    putString(Promotion.ITEM_ID, "${it.id} - ${it.categoryId}")
                    putString(Promotion.ITEM_NAME, "$GOFOOD_PAGENAME - ${TokoFoodHomeLayoutType.CATEGORY_WIDGET} - ${channelModel.verticalPosition + 1} - ${channelModel.channelHeader.name}")
                }
            }
        )
        return promotionBundle
    }

    private fun Bundle.viewItem(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, VIEW_ITEM)
        return this
    }

    private fun Bundle.clickPG(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.CLICK_PG)
        return this
    }

    private fun Bundle.selectContent(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, SELECT_CONTENT)
        return this
    }

    private fun Bundle.addGeneralTracker(userId: String?, destinationId: String?): Bundle {
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_HOME_PAGE)
        this.putString(TokoFoodAnalyticsConstants.BUSSINESS_UNIT, TokoFoodAnalyticsConstants.PHYSICAL_GOODS)
        this.putString(TokoFoodAnalyticsConstants.CURRENT_SITE, TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE)
        this.putString(TokoFoodAnalyticsConstants.USER_ID, userId ?: EMPTY_DATA)
        this.putString(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId ?: EMPTY_DATA)
        return this
    }
}