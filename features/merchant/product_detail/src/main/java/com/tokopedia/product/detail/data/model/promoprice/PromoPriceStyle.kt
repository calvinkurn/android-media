package com.tokopedia.product.detail.data.model.promoprice

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PromoPriceStyle(
    @SerializedName("iconURL")
    @Expose
    val iconUrl: String = "",
    @SerializedName("productID")
    @Expose
    val productId: String = "",
    @SerializedName("color")
    @Expose
    val mainTextColor: String = "",
    @SerializedName("background")
    @Expose
    val backgroundColor: String = "",
    @SerializedName("superGraphicURL")
    @Expose
    val superGraphicUrl: String = "",
    @SerializedName("separatorColor")
    @Expose
    val separatorColor: String = "",
)

fun List<PromoPriceStyle>.getPromoStyleByProductId(productId: String): PromoPriceStyle? {
    return this.firstOrNull {
        it.productId == productId
    }
}
