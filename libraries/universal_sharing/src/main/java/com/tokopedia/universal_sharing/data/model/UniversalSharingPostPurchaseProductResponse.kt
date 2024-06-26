package com.tokopedia.universal_sharing.data.model

import com.google.gson.annotations.SerializedName

data class UniversalSharingPostPurchaseProductWrapperResponse(
    @SerializedName("getProductV3")
    val product: UniversalSharingPostPurchaseProductResponse =
        UniversalSharingPostPurchaseProductResponse()
)

data class UniversalSharingPostPurchaseProductResponse(
    @SerializedName("productID")
    val productId: String = "",

    @SerializedName("productName")
    val productName: String = "",

    @SerializedName("price")
    val productPrice: Double = 0.0,

    @SerializedName("stock")
    var stock: Int = 0,

    @SerializedName("description")
    val desc: String = "",

    @SerializedName("status")
    var status: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("pictures")
    val images: List<UniversalSharingPostPurchaseProductImageResponse> = listOf()
) {
    fun getImageList(): ArrayList<String> {
        val list = images.map {
            it.imageUrl
        }
        return ArrayList(list)
    }
}

data class UniversalSharingPostPurchaseProductImageResponse(
    @SerializedName("urlOriginal")
    val imageUrl: String = ""
)
