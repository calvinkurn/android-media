package com.tokopedia.purchase_platform.features.checkout.view

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel
import com.tokopedia.purchase_platform.common.data.model.request.checkout.CheckoutRequest
import com.tokopedia.purchase_platform.common.data.model.request.checkout.ProductDataCheckoutRequest
import com.tokopedia.purchase_platform.common.data.model.request.checkout.ShopProductCheckoutRequest
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 2019-08-27.
 */

class ShipmentTrackingDataGenerator @Inject constructor() {

    fun generateBundleEnhancedEcommerce(checkoutRequest: CheckoutRequest?,
                                        shipmentCartItemModelList: List<ShipmentCartItemModel>?): Bundle {
        val eCommerceBundle = Bundle()

        val productBundles = ArrayList<Bundle>()

        checkoutRequest?.apply {
            data.forEach { dataCheckoutRequest ->
                dataCheckoutRequest?.apply {
                    shopProducts.forEach { shopProductCheckoutRequest ->
                        shopProductCheckoutRequest?.apply {
                            productData.forEach { productDataCheckoutRequest ->
                                productDataCheckoutRequest?.apply {
                                    productBundles.add(getProductBundle(shopProductCheckoutRequest, productDataCheckoutRequest, shipmentCartItemModelList))
                                }
                            }
                        }
                    }
                }
            }
        }

        eCommerceBundle.putParcelableArrayList("items", productBundles)

        return eCommerceBundle
    }

    private fun getProductBundle(shopProductCheckoutRequest: ShopProductCheckoutRequest,
                                 productDataCheckoutRequest: ProductDataCheckoutRequest,
                                 shipmentCartItemModelList: List<ShipmentCartItemModel>?): Bundle {
        val productBundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, productDataCheckoutRequest.getProductId().toString())
            putString(FirebaseAnalytics.Param.ITEM_NAME, productDataCheckoutRequest.productName ?: "")
            putString(FirebaseAnalytics.Param.ITEM_BRAND, "")
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, productDataCheckoutRequest.productCategory ?: "")
            putString(FirebaseAnalytics.Param.ITEM_VARIANT, "")
            putDouble(FirebaseAnalytics.Param.PRICE, productDataCheckoutRequest.productPrice?.toDouble() ?: 0.0)
            putLong(FirebaseAnalytics.Param.QUANTITY, productDataCheckoutRequest.getProductQuantity().toLong())
            putString("dimension38", productDataCheckoutRequest.productAttribution ?: "")
            putString("dimension40", productDataCheckoutRequest.productListName ?: "")
            putString("dimension45", productDataCheckoutRequest.cartId.toString())
            putString("dimension53", productDataCheckoutRequest.isDiscountedPrice.toString())
            putString("dimension12", shopProductCheckoutRequest.shippingInfo?.analyticsDataShippingCourierPrice ?: "")
            putString("dimension56", productDataCheckoutRequest.warehouseId ?: "")
            putString("dimension48", productDataCheckoutRequest.productWeight ?: "")
            putString("dimension49", productDataCheckoutRequest.promoCode ?: "")
            putString("dimension59", productDataCheckoutRequest.promoDetails ?: "")
            putString("dimension45", productDataCheckoutRequest.cartId.toString())
            putString("dimension11", productDataCheckoutRequest.buyerAddressId ?: "")
            putString("dimension16", productDataCheckoutRequest.shippingDuration ?: "")
            putString("dimension14", productDataCheckoutRequest.courier ?: "")
            putString("dimension12", productDataCheckoutRequest.shippingPrice ?: "")
            putString("dimension10", productDataCheckoutRequest.codFlag ?: "")
            putString("dimension57", productDataCheckoutRequest.tokopediaCornerFlag ?: "")
            putString("dimension58", productDataCheckoutRequest.isFulfillment ?: "")
            putString("dimension54", if (getFulfillmentStatus(shopProductCheckoutRequest.getShopId(), shipmentCartItemModelList)) "tokopedia" else "regular")
        }
        return productBundle
    }

    private fun getFulfillmentStatus(shopId: Int, shipmentCartItemModelList: List<ShipmentCartItemModel>?): Boolean {
        shipmentCartItemModelList?.apply {
            for (cartItemModel in shipmentCartItemModelList) {
                if (cartItemModel.shopId == shopId) {
                    return cartItemModel.isFulfillment
                }
            }
        }
        return false
    }

}