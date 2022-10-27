package com.tokopedia.tokofood.feature.home.analytics

import android.os.Bundle
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalytics
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants
import com.tokopedia.tokofood.common.analytics.TokoFoodAnalyticsConstants.EMPTY_DATA
import com.tokopedia.tokofood.common.domain.response.CheckoutTokoFoodData
import com.tokopedia.tokofood.common.domain.response.Merchant
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

object TokoFoodHomeCategoryCommonAnalytics: BaseTrackerConst() {

    fun impressMerchant(userId: String?, destinationId: String?, merchant: Merchant, horizontalPosition: Int, isHome: Boolean): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val merchantAddress = if (merchant.addressLocality.isNotEmpty()) merchant.addressLocality else EMPTY_DATA
        val listPromotions = arrayListOf(
            Promotion(
                creative = "${merchant.additionalData.topTextBanner} - ${merchant.promo}",
                position = (horizontalPosition + Int.ONE).toString(),
                id = "${merchant.id} - ${merchant.name}",
                name = "$merchantAddress - ${merchant.etaFmt} - ${merchant.distanceFmt} - ${merchant.ratingFmt}"
            )
        )
        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventAction = TokoFoodAnalytics.EVENT_ACTION_VIEW_MERCHANT_LIST,
            eventLabel = "",
            eventCategory = if (isHome) TokoFoodAnalytics.EVENT_CATEGORY_HOME_PAGE else TokoFoodAnalytics.EVENT_CATEGORY_CATEGORY_PAGE,
            promotions = listPromotions)
            .appendBusinessUnit(TokoFoodAnalytics.PHYSICAL_GOODS)
            .appendCurrentSite(TokoFoodAnalytics.TOKOPEDIA_MARKETPLACE)
            .appendUserId(userId ?: EMPTY_DATA)
            .appendCustomKeyValue(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId ?: EMPTY_DATA)
            .appendCustomKeyValue(
                TokoFoodAnalyticsConstants.TRACKER_ID,
                if (isHome) TokoFoodAnalyticsConstants.TRACKER_ID_31289 else TokoFoodAnalyticsConstants.TRACKER_ID_32007
            )
            .build()
    }

    fun getItemATC(data: CheckoutTokoFoodData): ArrayList<Bundle> {
        val itemBundles = arrayListOf<Bundle>()
        itemBundles.addAll(
            data.availableSection.products.map {
                Bundle().apply {
                    putString(TokoFoodAnalytics.KEY_DIMENSION_45, it.cartId)
                    putString(Items.ITEM_BRAND, EMPTY_DATA)
                    putString(Items.ITEM_CATEGORY, EMPTY_DATA)
                    putString(Items.ITEM_ID, it.productId)
                    putString(Items.ITEM_NAME, it.productName)
                    putString(Items.ITEM_VARIANT, it.productId)
                    putDouble(Items.PRICE, it.price)
                    putInt(TokoFoodAnalytics.KEY_QUANTITY, it.quantity)
                    putString(TokoFoodAnalytics.KEY_SHOP_ID, data.shop.shopId)
                    putString(TokoFoodAnalytics.KEY_SHOP_NAME, data.shop.name)
                    putString(TokoFoodAnalytics.KEY_SHOP_TYPE, EMPTY_DATA)
                }
            }
        )
        return itemBundles
    }

    fun getProductIds(data: CheckoutTokoFoodData): String {
        return data.availableSection.products.joinToString(",") {
            it.productId
        }
    }

    fun getPromotionMerchant(merchant: Merchant, horizontalPosition: Int): ArrayList<Bundle> {
        val promotionBundle = arrayListOf<Bundle>()
        promotionBundle.add(
            Bundle().apply {
                val merchantAddress = if (merchant.addressLocality.isNotEmpty()) merchant.addressLocality else EMPTY_DATA
                putString(Promotion.CREATIVE_NAME, "")
                putString(Promotion.CREATIVE_SLOT, (horizontalPosition + Int.ONE).toString())
                putString(Promotion.ITEM_ID, "${merchant.id} - ${merchant.name}")
                putString(Promotion.ITEM_NAME, "$merchantAddress - ${merchant.etaFmt} - ${merchant.distanceFmt} - ${merchant.ratingFmt}")
            }
        )
        return promotionBundle
    }

    fun Bundle.addGeneralTracker(userId: String?, destinationId: String?): Bundle {
        this.putString(TokoFoodAnalyticsConstants.BUSSINESS_UNIT, TokoFoodAnalyticsConstants.PHYSICAL_GOODS)
        this.putString(TokoFoodAnalyticsConstants.CURRENT_SITE, TokoFoodAnalyticsConstants.TOKOPEDIA_MARKETPLACE)
        this.putString(TokoFoodAnalyticsConstants.USER_ID, userId ?: EMPTY_DATA)
        this.putString(TokoFoodAnalyticsConstants.DESTINATION_ID, destinationId ?: EMPTY_DATA)
        return this
    }
}
