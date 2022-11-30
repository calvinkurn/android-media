package com.tokopedia.product.detail.common.data.model.ar

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductArInfo(
        @SerializedName("productIDs")
        @Expose
        val productIds: List<String> = listOf(),
        @SerializedName("applink")
        @Expose
        val applink: String = "",
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("imageUrl")
        @Expose
        val imageUrl: String = "",
) {
    fun isProductIdContainsAr(selectedProductId: String): Boolean {
        return productIds.contains(selectedProductId)
    }
}
