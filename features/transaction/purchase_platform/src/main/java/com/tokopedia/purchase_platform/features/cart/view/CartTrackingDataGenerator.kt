package com.tokopedia.purchase_platform.features.cart.view

import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.analytics.FirebaseAnalytics
import com.tokopedia.purchase_platform.common.analytics.enhanced_ecommerce_data.EnhancedECommerceProductCartMapData
import com.tokopedia.purchase_platform.features.cart.domain.model.cartlist.CartItemData
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-27.
 */

class CartTrackingDataGenerator @Inject constructor() {

    companion object {
        const val DEFAULT_VALUE_NONE_OTHER = "none / other"
    }

    fun generateBundleEnhancedEcommerce(cartItemDataList: List<CartItemData>?): Bundle {
        val eCommerceBundle = Bundle()

        val productBundles = ArrayList<Bundle>()

        cartItemDataList?.apply {
            forEach { cartItemData ->
                val productBundle = getProductBundle(cartItemData)
                productBundles.add(productBundle)
            }
        }

        eCommerceBundle.putParcelableArrayList("items", productBundles)

        eCommerceBundle.putLong(FirebaseAnalytics.Param.CHECKOUT_STEP, 1)
        eCommerceBundle.putString(FirebaseAnalytics.Param.CHECKOUT_OPTION, "cart page loaded")

        eCommerceBundle.putString("eventCategory", "cart");
        eCommerceBundle.putString("eventAction", "click check out");

        return eCommerceBundle
    }

    private fun getProductBundle(cartItemData: CartItemData?): Bundle {
        val productBundle = Bundle().apply {
            cartItemData?.apply {
                putString(FirebaseAnalytics.Param.ITEM_ID, originData.productId ?: "")
                putString(FirebaseAnalytics.Param.ITEM_NAME, originData.productName ?: "")
                putString(FirebaseAnalytics.Param.ITEM_BRAND, DEFAULT_VALUE_NONE_OTHER)
                putString(FirebaseAnalytics.Param.ITEM_CATEGORY,
                        if (TextUtils.isEmpty(originData.category)) DEFAULT_VALUE_NONE_OTHER
                        else originData.category
                )
                putString(FirebaseAnalytics.Param.ITEM_VARIANT, DEFAULT_VALUE_NONE_OTHER)
                putDouble(FirebaseAnalytics.Param.PRICE, originData.pricePlanInt.toDouble())
                putLong(FirebaseAnalytics.Param.QUANTITY, updatedData.quantity.toLong())
                putString("dimension45", originData.cartId.toString())
                putString("dimension54", isFulfillment.toString())
                putString("dimension53", (originData.priceOriginal > 0).toString())
                putString("dimension80",
                        if (TextUtils.isEmpty(originData.trackerAttribution)) EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                        else originData.trackerAttribution)
                putString("dimension56", originData.warehouseId.toString())
                putString("dimension48", originData.weightPlan.toString())
                putString("dimension45", originData.cartId.toString())
                putString("dimension49", originData.promoCodes ?: "")
                putString("dimension59", originData.promoDetails ?: "")
            }
        }

        return productBundle
    }

}