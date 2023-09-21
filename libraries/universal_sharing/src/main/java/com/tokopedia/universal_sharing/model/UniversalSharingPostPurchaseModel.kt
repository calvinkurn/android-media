package com.tokopedia.universal_sharing.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Parcelize
data class UniversalSharingPostPurchaseModel(
    val shopList: List<UniversalSharingPostPurchaseShopModel> = listOf()
) : Parcelable

@Parcelize
data class UniversalSharingPostPurchaseShopModel(
    val shopName: String = "",
    val shopType: String = "",
    val productList: List<UniversalSharingPostPurchaseProductModel> = listOf()
) : Parcelable {

    @Keep
    enum class ShopType(val key: String) {
        OFFICIAL_STORE("official_store"),
        PM("Gold Merchant"),
        PM_PRO("gold_merchant"),
        REGULAR("marketplace")
    }

    fun getShopType(): ShopType {
        return when (shopType) {
            ShopType.OFFICIAL_STORE.key -> ShopType.OFFICIAL_STORE
            ShopType.PM.key -> ShopType.PM
            ShopType.PM_PRO.key -> ShopType.PM_PRO
            ShopType.REGULAR.key -> ShopType.REGULAR
            else -> ShopType.REGULAR
        }
    }
}

@Parcelize
data class UniversalSharingPostPurchaseProductModel(
    val productId: String = "",
    val productName: String = "",
    val productPrice: String = "",
    val imageUrl: String = ""
) : Parcelable
