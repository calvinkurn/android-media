package com.tokopedia.product.detail.common.data.model.bundleinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BundleInfo(
    @SerializedName("productID")
    @Expose
    val productId: String = "",

    @SerializedName("bundleID")
    @Expose
    val bundleId: String = "",

    @SerializedName("groupID")
    @Expose
    val groupId: String = "",

    @SerializedName("name")
    @Expose
    val name: String = "",

    @SerializedName("type")
    @Expose
    val type: String = "",

    @SerializedName("status")
    @Expose
    val status: String = "",

    @SerializedName("titleComponent")
    @Expose
    val titleComponent: String = "",

    @SerializedName("finalPriceBundling")
    @Expose
    val finalPriceBundling: String = "",

    @SerializedName("originalPriceBundling")
    @Expose
    val originalPriceBundling: String = "",

    @SerializedName("savingPriceBundling")
    @Expose
    val savingPriceBundling: String = "",

    @SerializedName("preorderString")
    @Expose
    val preorderString: String = "",

    @SerializedName("bundleItems")
    @Expose
    val bundleItems: List<BundleItem> = emptyList()
) {
    data class BundleItem(
        @SerializedName("productID")
        @Expose
        val productId: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("picURL")
        @Expose
        val picURL: String = "",

        @SerializedName("status")
        @Expose
        val status: String = "",

        @SerializedName("quantity")
        @Expose
        val quantity: String = "",

        @SerializedName("originalPrice")
        @Expose
        val originalPrice: String = "",

        @SerializedName("bundlePrice")
        @Expose
        val bundlePrice: String = "",

        @SerializedName("discountPercentage")
        @Expose
        val discountPercentage: String = "",

        @SerializedName("stock")
        @Expose
        val stock: String = "",
    )
}
