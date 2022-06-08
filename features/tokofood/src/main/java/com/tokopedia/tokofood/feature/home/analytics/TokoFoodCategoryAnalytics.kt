package com.tokopedia.tokofood.feature.home.analytics

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.feature.home.domain.data.Merchant
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.constant.TrackerConstant

/**
 * Category
 * https://mynakama.tokopedia.com/datatracker/requestdetail/view/3053 17 - 19
 */

class TokoFoodCategoryAnalytics: BaseTrackerConst() {

    fun clickMerchant(userId: String?, destinationId: String?, merchant: Merchant, horizontalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION,
                TokoFoodAnalytics.EVENT_ACTION_CLICK_MERCHANT_LIST
            )
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionMerchant(merchant, horizontalPosition))
        eventDataLayer.selectContent(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(Event.SELECT_CONTENT, eventDataLayer)
    }

    fun impressMerchant(userId: String?, destinationId: String?, merchant: Merchant, horizontalPosition: Int) {
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalytics.EVENT_ACTION_VIEW_MERCHANT_LIST)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        eventDataLayer.putParcelableArrayList(Promotion.KEY, getPromotionMerchant(merchant, horizontalPosition))
        eventDataLayer.viewItem(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.VIEW_ITEM, eventDataLayer)
    }

    fun clickAtc(userId: String?, destinationId: String?, data: CheckoutTokoFoodData){
        val eventDataLayer = Bundle().apply {
            putString(TrackAppUtils.EVENT_ACTION, TokoFoodAnalytics.EVENT_ACTION_CLICK_ORDER_MINICART)
            putString(TrackAppUtils.EVENT_LABEL, "")
        }
        val items = getItemATC(data)
        eventDataLayer.putParcelableArrayList(TokoFoodAnalytics.KEY_ITEMS, items)
        eventDataLayer.addToCart(userId, destinationId)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(TokoFoodAnalyticsConstants.ADD_TO_CART, eventDataLayer)
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

    private fun Bundle.addToCart(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.ADD_TO_CART)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_CATEGORY_PAGE)
        return this
    }


    private fun Bundle.viewItem(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, TokoFoodAnalyticsConstants.VIEW_ITEM)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_CATEGORY_PAGE)
        return this
    }

    private fun Bundle.selectContent(userId: String?, destinationId: String?): Bundle {
        addGeneralTracker(userId, destinationId)
        this.putString(TrackAppUtils.EVENT, Event.SELECT_CONTENT)
        this.putString(TrackAppUtils.EVENT_CATEGORY, TokoFoodAnalytics.EVENT_CATEGORY_CATEGORY_PAGE)
        return this
    }

    private fun Bundle.addGeneralTracker(userId: String?, destinationId: String?): Bundle {
        this.putString(TokoFoodAnalyticsConstants.BUSSINESS_UNIT, TokoFoodAnalyticsConstants.PHYSICAL_GOODS)
        this.putString(TokoFoodAnalyticsConstants.CURRENT_SITE, TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE)
        this.putString(TokoFoodAnalyticsConstants.USER_ID, userId ?: TokoFoodAnalyticsConstants.EMPTY_DATA)
        this.putString(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId ?: TokoFoodAnalyticsConstants.EMPTY_DATA)
        return this
    }
}