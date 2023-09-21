package com.tokopedia.thankyou_native.presentation.helper

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.thankyou_native.domain.model.ShopOrder
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseProductModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseShopModel
import javax.inject.Inject

class PostPurchaseShareHelper @Inject constructor() {

    fun goToSharePostPurchase(
        context: Context,
        shopOrderList: List<ShopOrder>
    ) {
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalCommunication.POST_PURCHASE_SHARING
        )
        intent.putExtra(
            ApplinkConstInternalCommunication.PRODUCT_LIST_DATA,
            getPostPurchaseData(shopOrderList)
        )
        context.startActivity(intent)
    }

    private fun getPostPurchaseData(shopOrderList: List<ShopOrder>): UniversalSharingPostPurchaseModel {
        val shopList = ArrayList<UniversalSharingPostPurchaseShopModel>()
        // Loop each shop
        shopOrderList.forEach {
            val productList = ArrayList<UniversalSharingPostPurchaseProductModel>()
            // Loop each product in each shop
            it.purchaseItemList.forEach { item ->
                val productData = UniversalSharingPostPurchaseProductModel(
                    productId = item.productId,
                    productName = item.productName,
                    productPrice = item.priceStr,
                    imageUrl = item.thumbnailProduct
                )
                productList.add(productData)
            }
            val shopData = UniversalSharingPostPurchaseShopModel(
                shopName = it.storeName ?: "",
                shopType = it.storeType,
                productList = productList
            )
            shopList.add(shopData)
        }
        return UniversalSharingPostPurchaseModel(
            shopList = shopList
        )
    }
}
