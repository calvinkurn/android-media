package com.tokopedia.tokofood.feature.home.analytics

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.feature.home.domain.data.Merchant
import com.tokopedia.track.builder.util.BaseTrackerConst
import com.tokopedia.track.constant.TrackerConstant

object TokoFoodHomeCategoryCommonAnalytics: BaseTrackerConst() {

    fun getItemATC(data: CheckoutTokoFoodData): ArrayList<Bundle> {
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

    fun getPromotionMerchant(merchant: Merchant, horizontalPosition: Int): ArrayList<Bundle> {
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

    fun Bundle.addGeneralTracker(userId: String?, destinationId: String?): Bundle {
        this.putString(TokoFoodAnalyticsConstants.BUSSINESS_UNIT, TokoFoodAnalyticsConstants.PHYSICAL_GOODS)
        this.putString(TokoFoodAnalyticsConstants.CURRENT_SITE, TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE)
        this.putString(TokoFoodAnalyticsConstants.USER_ID, userId ?: TokoFoodAnalyticsConstants.EMPTY_DATA)
        this.putString(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId ?: TokoFoodAnalyticsConstants.EMPTY_DATA)
        return this
    }
}