package com.tokopedia.tokofood.feature.home.analytics

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isLessThanZero
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_CLICK_CAROUSEL_BANNER
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_CLICK_CATEGORY_ICONS
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_CLICK_CATEGORY_WIDGET
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_CLICK_LEGO_SIX
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_CLICK_MERCHANT_LIST
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_VIEW_CAROUSEL_BANNER
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_VIEW_CATEGORY_ICONS
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_VIEW_CATEGORY_WIDGET
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_VIEW_LEGO_SIX
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.BEGIN_CHECKOUT
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.BUSSINESS_UNIT
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.CLICK_PG
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.CLICK_SEARCH_BAR_TOKOFOOD
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.CURRENT_SITE
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.DESTINATION_ID
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.EMPTY_DATA
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.GOFOOD_PAGENAME
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.HOME_PAGE
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.IS_LOGGED_IN_STATUS
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.NO_PIN_POIN
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.OPEN_SCREEN
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.OUT_OF_COVERAGE
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.PAGE_SOURCE
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.PHYSICAL_GOODS
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.PRODUCT_ID
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.SCREEN_NAME
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.TOKOFOOD_HOME
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.TRACKER_ID
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.TRACKER_ID_35766
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.USER_ID
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.VIEW_ITEM
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalyticsOld.addGeneralTracker
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalyticsOld.getItemATC
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalyticsOld.getProductIds
import com.tokopedia.tokofood.feature.home.analytics.TokoFoodHomeCategoryCommonAnalyticsOld.getPromotionMerchant
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeLayoutType
import com.tokopedia.tokofood.feature.home.domain.data.DynamicIcon
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.PROMO_CLICK
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.SELECT_CONTENT
import com.tokopedia.track.constant.TrackerConstant

/**
 * Home
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3053 1 - 16
 */
class TokoFoodHomeAnalyticsOld: BaseTrackerConst() {

    companion object {
        private const val ATC_HOME_TRACKER_ID = "31290"
    }

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

    fun clickMerchant(userId: String?, destinationId: String?, merchant: Merchant, horizontalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_CLICK_MERCHANT_LIST)
            putString(TrackAppUtils.EVENT_LABEL, "${merchant.additionalData.topTextBanner} - ${merchant.promo}")
            putString(TRACKER_ID, TokoFoodAnalyticsConstants.TRACKER_ID_31288)
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionMerchant(merchant, horizontalPosition))
        eventDataLayer.selectContent(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(PROMO_CLICK, eventDataLayer)
    }

    fun openScreenHomePage(userId: String?, destinationId: String?, isLoggenInStatus: Boolean) {
        val eventDataLayer = Bundle().apply {
            putString(SCREEN_NAME, HOME_PAGE)
        }
        eventDataLayer.openScreen(userId, destinationId, isLoggenInStatus)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(OPEN_SCREEN, eventDataLayer)
    }

    fun openScreenOutOfCoverage(userId: String?, destinationId: String?, isLoggenInStatus: Boolean) {
        val eventDataLayer = Bundle().apply {
            putString(SCREEN_NAME, OUT_OF_COVERAGE)
        }
        eventDataLayer.openScreen(userId, destinationId, isLoggenInStatus)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(OPEN_SCREEN, eventDataLayer)
    }

    fun openScreenNoPinPoin(userId: String?, destinationId: String?, isLoggenInStatus: Boolean) {
        val eventDataLayer = Bundle().apply {
            putString(SCREEN_NAME, NO_PIN_POIN)
        }
        eventDataLayer.openScreen(userId, destinationId, isLoggenInStatus)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(OPEN_SCREEN, eventDataLayer)
    }

    fun clickEmptyState(userId: String?, destinationId: String?, errorState: String, title: String, desc: String) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalytics.EVENT_ACTION_CLICK_OUT_COVERAGE) //TODO BLOCKER FROM DA, Error status
            putString(TrackAppUtils.EVENT_LABEL, "error_state:$errorState;\ntitle:$title;\ndescription:$desc;")
        }
        eventDataLayer.clickPG(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.CLICK_PG, eventDataLayer)
    }

    fun clickAtc(userId: String?, destinationId: String?, data: CheckoutTokoFoodData){
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalytics.EVENT_ACTION_CLICK_ORDER_MINICART)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        val items = getItemATC(data)
        eventDataLayer.putParcelableArrayList(TokoFoodAnalytics.KEY_ITEMS, items)
        eventDataLayer.addToCart(userId, destinationId, data.shop.shopId, data)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(BEGIN_CHECKOUT, eventDataLayer)
    }

    fun clickSearchBar(userId: String?, destinationId: String?) {
        val eventData = mapOf(
            TrackAppUtils.EVENT to CLICK_PG,
            TrackAppUtils.EVENT_ACTION to CLICK_SEARCH_BAR_TOKOFOOD,
            TrackAppUtils.EVENT_CATEGORY to TOKOFOOD_HOME,
            TrackAppUtils.EVENT_LABEL to "",
            TRACKER_ID to TRACKER_ID_35766,
            BUSSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            DESTINATION_ID to destinationId.orEmpty(),
            PAGE_SOURCE to TOKOFOOD_HOME,
            USER_ID to userId.orEmpty()
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventData)
    }

    fun viewSearchBarCoachmark(userId: String?, destinationId: String?, title: String, subtitle: String) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalyticsConstants.VIEW_COACHMARK)
            putString(TrackAppUtils.EVENT_LABEL, String.EMPTY)
            putString(TRACKER_ID, TokoFoodAnalyticsConstants.TRACKER_ID_39609)
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionSearchBarCoachmark(title, subtitle))
        eventDataLayer.viewItem(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventDataLayer)
    }

    private fun getPromotionItemIcon(data: List<DynamicIcon>, horizontalPosition: Int = -Int.ONE, verticalPosition: Int): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.addAll(
            data.mapIndexed { index, it ->
                val position = if (horizontalPosition.isLessThanZero()) index else horizontalPosition
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, "${it.imageUrl} - ${it.applinks}")
                    putString(Promotion.CREATIVE_SLOT, (position + Int.ONE).toString())
                    putString(Promotion.ITEM_ID, it.name)
                    putString(Promotion.ITEM_NAME, "$GOFOOD_PAGENAME - ${TokoFoodHomeLayoutType.ICON_TOKOFOOD} - ${verticalPosition + Int.ONE} - $EMPTY_DATA")
                }
            }
        )
        return promotionBundle
    }

    private fun getPromotionItemBanner(channelModel: ChannelModel, channelGrids: List<ChannelGrid>, horizontalPosition: Int = -Int.ONE): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.addAll(
            channelGrids.mapIndexed { index, it ->
                val position = if (horizontalPosition.isLessThanZero()) index else horizontalPosition
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, "${it.imageUrl} - ${it.applink}")
                    putString(Promotion.CREATIVE_SLOT, (position + Int.ONE).toString())
                    putString(Promotion.ITEM_ID, it.id)
                    putString(Promotion.ITEM_NAME, "$GOFOOD_PAGENAME - ${TokoFoodHomeLayoutType.BANNER_CAROUSEL} - ${channelModel.verticalPosition + Int.ONE} - ${channelModel.channelHeader.name}")
                }
            }
        )
        return promotionBundle
    }

    private fun getPromotionLego(channelModel: ChannelModel, channelGrids: List<ChannelGrid>, horizontalPosition: Int = -Int.ONE): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.addAll(
            channelGrids.mapIndexed { index, it ->
                val position = if (horizontalPosition.isLessThanZero()) index else horizontalPosition
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, "${it.imageUrl} - ${it.applink}")
                    putString(Promotion.CREATIVE_SLOT, (position + Int.ONE).toString())
                    putString(Promotion.ITEM_ID, "${it.id} - ${it.imageUrl}")
                    putString(Promotion.ITEM_NAME, "$GOFOOD_PAGENAME - ${TokoFoodHomeLayoutType.LEGO_6_IMAGE} - ${channelModel.verticalPosition + Int.ONE} - ${channelModel.channelHeader.name}")
                }
            }
        )
        return promotionBundle
    }

    private fun getPromotionCategory(channelModel: ChannelModel, channelGrids: List<ChannelGrid>, horizontalPosition: Int = -Int.ONE): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.addAll(
            channelGrids.mapIndexed { index, it ->
                val position = if (horizontalPosition.isLessThanZero()) index else horizontalPosition
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, "${it.imageUrl} - ${it.applink}")
                    putString(Promotion.CREATIVE_SLOT, (position + Int.ONE).toString())
                    putString(Promotion.ITEM_ID, "${it.name} - ${it.id}")
                    putString(Promotion.ITEM_NAME, "$GOFOOD_PAGENAME - ${TokoFoodHomeLayoutType.CATEGORY_WIDGET} - ${channelModel.verticalPosition + Int.ONE} - ${channelModel.channelHeader.name}")
                }
            }
        )
        return promotionBundle
    }

    private fun getPromotionSearchBarCoachmark(title: String, subtitle: String): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.add(
            Bundle().apply {
                putString(Promotion.ITEM_ID, TokoFoodAnalyticsConstants.COMPONENT_SEARCH_BAR)
                putString(Promotion.ITEM_NAME, TokoFoodAnalyticsConstants.TITLE_PREFIX + title)
                putString(Promotion.CREATIVE_SLOT, Int.ZERO.toString())
                putString(Promotion.CREATIVE_NAME, TokoFoodAnalyticsConstants.SUBTITLE_PREFIX + subtitle)
            }
        )
        return promotionBundle
    }

    private fun Bundle.openScreen(userId: String?, destinationId: String?, isLoggenInStatus: Boolean): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, OPEN_SCREEN)
        this.putBoolean(IS_LOGGED_IN_STATUS, isLoggenInStatus)
        return this
    }

    private fun Bundle.addToCart(userId: String?, destinationId: String?, shopId: String, data: CheckoutTokoFoodData): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, BEGIN_CHECKOUT)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_HOME_PAGE)
        this.putString(TrackerConstant.SHOP_ID, shopId)
        this.putString(PRODUCT_ID, getProductIds(data))
        this.putString(TokoFoodAnalytics.KEY_TRACKER_ID, ATC_HOME_TRACKER_ID)
        this.putString(TokoFoodAnalytics.KEY_CHECKOUT_STEP, TokoFoodAnalytics.CHECKOUT_STEP_1)
        this.putString(TokoFoodAnalytics.KEY_CHECKOUT_OPTION, TokoFoodAnalytics.EVENT_CHECKOUT_OPTION_MINI_CART)
        return this
    }

    private fun Bundle.viewItem(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, VIEW_ITEM)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_HOME_PAGE)
        return this
    }

    private fun Bundle.clickPG(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.CLICK_PG)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_HOME_PAGE)
        return this
    }

    private fun Bundle.selectContent(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, SELECT_CONTENT)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_HOME_PAGE)
        return this
    }
}
