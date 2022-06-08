package com.tokopedia.tokofood.feature.home.analytics

import android.os.Bundle
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.ONE
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
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics.EVENT_ACTION_VIEW_MERCHANT_LIST
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.ADD_TO_CART
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.EMPTY_DATA
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.GOFOOD_PAGENAME
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.HOME_PAGE
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.IS_LOGGED_IN_STATUS
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.NO_PIN_POIN
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.OPEN_SCREEN
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.OUT_OF_COVERAGE
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.SCREEN_NAME
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.VIEW_ITEM
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodHomeLayoutType
import com.tokopedia.tokofood.feature.home.domain.data.DynamicIcon
import com.tokopedia.tokofood.feature.home.domain.data.Merchant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.builder.util.BaseTrackerConst.Event.SELECT_CONTENT
import com.tokopedia.track.constant.TrackerConstant

/**
 * Home
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3053 1 - 16
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

    fun clickMerchant(userId: String?, destinationId: String?, merchant: Merchant, horizontalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_CLICK_MERCHANT_LIST)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionMerchant(merchant, horizontalPosition))
        eventDataLayer.selectContent(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventDataLayer)
    }

    fun impressMerchant(userId: String?, destinationId: String?, merchant: Merchant, horizontalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,  EVENT_ACTION_VIEW_MERCHANT_LIST)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionMerchant(merchant, horizontalPosition))
        eventDataLayer.viewItem(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventDataLayer)
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
        eventDataLayer.addToCart(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(ADD_TO_CART, eventDataLayer)
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
                    putString(Promotion.ITEM_ID, "${it.id} - ${it.categoryId}")
                    putString(Promotion.ITEM_NAME, "$GOFOOD_PAGENAME - ${TokoFoodHomeLayoutType.CATEGORY_WIDGET} - ${channelModel.verticalPosition + Int.ONE} - ${channelModel.channelHeader.name}")
                }
            }
        )
        return promotionBundle
    }

    private fun getPromotionMerchant(merchant: Merchant, horizontalPosition: Int): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.add(
                Bundle().apply {
                    putString(Promotion.CREATIVE_NAME, "")
                    putString(Promotion.CREATIVE_SLOT, (horizontalPosition + Int.ONE).toString())
                    putString(Promotion.ITEM_ID, "${merchant.id} - ${merchant.name}")
                    putString(Promotion.ITEM_NAME, "//TODO_MERCHANT_LOCATION - ${merchant.etaFmt} - ${merchant.distanceFmt} - ${merchant.ratingFmt}")
            }
        )
        return promotionBundle
    }

    private fun getItemATC(data: CheckoutTokoFoodData): ArrayList<Bundle> {
        val itemBundles = arrayListOf<Bundle>()
        itemBundles.addAll(
            data.availableSection.products.map {
                Bundle().apply {
                    putString(TokoFoodAnalytics.KEY_CATEGORY_ID, "//TODO_CATEGORY_ID")
                    putString(TokoFoodAnalytics.KEY_DIMENSION_45, it.cartId)
                    putString(Items.ITEM_BRAND, "//TODO_BRAND_ID")
                    putString(Items.ITEM_CATEGORY, "//TODO_ITEM_CATEGORY")
                    putString(Items.ITEM_ID, it.productId)
                    putString(Items.ITEM_NAME, it.productName)
                    putString(Items.ITEM_VARIANT, "//TODO_ITEM_VARIANT")
                    putDouble(Items.PRICE, it.price)
                    putInt(Items.QUANTITY, it.quantity)
                    putString(TrackerConstant.SHOP_ID, data.shop.shopId)
                    putString(TokoFoodAnalytics.KEY_SHOP_NAME, data.shop.name)
                    putString(TokoFoodAnalytics.KEY_SHOP_TYPE, "//TODO_SHOP_TYPE")
                }
            }
        )
        return itemBundles
    }

    private fun Bundle.openScreen(userId: String?, destinationId: String?, isLoggenInStatus: Boolean): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, OPEN_SCREEN)
        this.putBoolean(IS_LOGGED_IN_STATUS, isLoggenInStatus)
        return this
    }

    private fun Bundle.addToCart(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, ADD_TO_CART)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_HOME_PAGE)
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

    private fun Bundle.addGeneralTracker(userId: String?, destinationId: String?): Bundle {
        this.putString(TokoFoodAnalyticsConstants.BUSSINESS_UNIT, TokoFoodAnalyticsConstants.PHYSICAL_GOODS)
        this.putString(TokoFoodAnalyticsConstants.CURRENT_SITE, TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE)
        this.putString(TokoFoodAnalyticsConstants.USER_ID, userId ?: EMPTY_DATA)
        this.putString(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId ?: EMPTY_DATA)
        return this
    }
}