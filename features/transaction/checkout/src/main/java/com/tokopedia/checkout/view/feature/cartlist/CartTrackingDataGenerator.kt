package com.tokopedia.checkout.view.feature.cartlist

import android.os.Bundle
import android.text.TextUtils
import com.google.firebase.analytics.FirebaseAnalytics
import com.tokopedia.checkout.domain.datamodel.cartlist.CartItemData
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartShopHolderData
import com.tokopedia.transactionanalytics.data.EnhancedECommerceProductCartMapData
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-27.
 */

class CartTrackingDataGenerator @Inject constructor() {

    companion object {
        const val DEFAULT_VALUE_NONE_OTHER = "none / other"
    }

    fun generateBundleEnhancedEcommerceStep1(cartItemDataList: List<CartItemData>): Bundle {

        val productBundles = ArrayList<Bundle>()

        for (cartItemData in cartItemDataList) {
            val productBundle = getProductBundle(cartItemData)
            productBundles.add(productBundle)
        }

        val eCommerceBundle = Bundle()
        eCommerceBundle.putParcelableArrayList("items", productBundles)

        eCommerceBundle.putLong("step", 1)
        eCommerceBundle.putString("option", "cart page loaded")

        eCommerceBundle.putString("eventCategory", "cart");
        eCommerceBundle.putString("eventAction", "click check out");
//        eCommerceBundle.putString("eventLabel", "success - default");

//        eCommerceBundle.putString("shopId", { { shop_id } });
//        eCommerceBundle.putString("shopType", { { shop_type } });
//        eCommerceBundle.putString("coupon", { { coupon_name } });
//        eCommerceBundle.putString("categoryId", { { category_id } });

        return eCommerceBundle
    }

    private fun getProductBundle(cartItemData: CartItemData): Bundle {
        val productBundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, cartItemData.originData.productId)
            putString(FirebaseAnalytics.Param.ITEM_NAME, cartItemData.originData.productName)
            putString(FirebaseAnalytics.Param.ITEM_BRAND, DEFAULT_VALUE_NONE_OTHER)
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY,
                    if (TextUtils.isEmpty(cartItemData.originData.category)) DEFAULT_VALUE_NONE_OTHER
                    else cartItemData.originData.category
            )
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, DEFAULT_VALUE_NONE_OTHER)
            putDouble(FirebaseAnalytics.Param.PRICE, cartItemData.originData.pricePlanInt.toDouble())
            putLong(FirebaseAnalytics.Param.QUANTITY, cartItemData.updatedData.quantity.toLong())
            putString("dimension45", cartItemData.originData.cartId.toString())
            putBoolean("dimension54", cartItemData.isFulfillment)
            putBoolean("dimension53", cartItemData.originData.priceOriginal > 0)
            putString("dimension80",
                    if (TextUtils.isEmpty(cartItemData.originData.trackerAttribution)) EnhancedECommerceProductCartMapData.DEFAULT_VALUE_NONE_OTHER
                    else cartItemData.originData.trackerAttribution)
            putString("shop_id", cartItemData.originData.shopId)
            putString("shop_type", cartItemData.originData.shopType)
            putString("shop_name", cartItemData.originData.shopName)
            putString("category_id", cartItemData.originData.categoryId)
            putString("dimension56", cartItemData.originData.warehouseId.toString())
            putString("dimension48", cartItemData.originData.weightPlan.toString())
            putString("dimension45", cartItemData.originData.cartId.toString())
            putString("dimension49", cartItemData.originData.promoCodes)
            putString("dimension59", cartItemData.originData.promoDetails)
        }
        return productBundle
    }

}