package com.tokopedia.thankyou_native.presentation.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.thankyou_native.domain.model.ShopOrder
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseProductModel
import com.tokopedia.universal_sharing.model.UniversalSharingPostPurchaseShopModel
import javax.inject.Inject
import com.tokopedia.thankyou_native.R as thankyou_nativeR

class PostPurchaseShareHelper @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val POST_PURCHASE_SHARE_ONBOARDING_KEY = "show_post_purchase_onboarding"
    }

    fun showCoachMarkShare(
        context: Context,
        anchorView: View
    ) {
        if (sharedPreferences.getBoolean(POST_PURCHASE_SHARE_ONBOARDING_KEY, true)) {
            val coachMark2 = CoachMark2(context)
            val coachMark2Item = arrayListOf(
                CoachMark2Item(
                    anchorView = anchorView,
                    title = "",
                    description = context.getString(
                        thankyou_nativeR.string.thankyou_postpurchase_share_onboarding
                    )
                )
            )
            coachMark2.showCoachMark(coachMark2Item)
            saveOnBoardingCache()
        }
    }

    private fun saveOnBoardingCache() {
        sharedPreferences
            .edit()
            .putBoolean(POST_PURCHASE_SHARE_ONBOARDING_KEY, false)
            .apply()
    }

    fun goToSharePostPurchase(
        activity: Activity,
        requestCode: Int,
        shopOrderList: List<ShopOrder>,
        pageType: String
    ) {
        val intent = RouteManager.getIntent(
            activity,
            ApplinkConstInternalCommunication.POST_PURCHASE_SHARING
        )
        intent.putExtra(
            ApplinkConstInternalCommunication.PRODUCT_LIST_DATA,
            getPostPurchaseData(shopOrderList)
        )
        intent.putExtra(
            ApplinkConstInternalCommunication.SOURCE,
            "Thankyou"
        )
        intent.putExtra(
            ApplinkConstInternalCommunication.PAGE_TYPE,
            pageType
        )
        activity.startActivityForResult(intent, requestCode)
    }

    private fun getPostPurchaseData(shopOrderList: List<ShopOrder>): UniversalSharingPostPurchaseModel {
        val shopList = ArrayList<UniversalSharingPostPurchaseShopModel>()
        // Loop each shop
        shopOrderList.forEach {
            val productList = ArrayList<UniversalSharingPostPurchaseProductModel>()
            // Loop each product in each shop
            it.purchaseItemList.forEach { item ->
                val productData = UniversalSharingPostPurchaseProductModel(
                    orderId = it.orderId,
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

    fun getOrderIdListString(shopOrderList: List<ShopOrder>): String {
        return shopOrderList.joinToString(separator = ",") {
            it.orderId.toIntOrZero().toString() // If the order ID contains any characters/strings, they should be replaced with 0
        }
    }
}
