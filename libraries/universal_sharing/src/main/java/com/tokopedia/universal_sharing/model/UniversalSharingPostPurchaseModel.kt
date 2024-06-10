package com.tokopedia.universal_sharing.model

import android.os.Parcelable
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
) : Parcelable

@Parcelize
data class UniversalSharingPostPurchaseProductModel(
    val orderId: String = "",
    val productId: String = "",
    val productName: String = "",
    val productPrice: String = "",
    val imageUrl: String = ""
) : Parcelable
