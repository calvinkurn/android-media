package com.tokopedia.product.detail.common.data.model.bebasongkir

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 25/02/21
 */
data class BebasOngkir(
        @SerializedName("products")
        @Expose
        val boProduct: List<BebasOngkirProduct> = listOf(),

        @SerializedName("images")
        @Expose
        val boImages: List<BebasOngkirImage> = listOf()
)

data class BebasOngkirProduct(
        @SerializedName("boType")
        @Expose
        val boType: Int = 0, // 1 No Bo, 2 BO, 3 BOE

        @SerializedName("productID")
        @Expose
        val productId: String = ""
)

data class BebasOngkirImage(
        @SerializedName("boType")
        @Expose
        val boType: Int = 0, // 1 No Bo, 2 BO, 3 BOE

        @SerializedName("imageURL")
        @Expose
        val imageURL: String = "",

        @SerializedName("tokoCabangImageURL")
        @Expose
        val tokoCabangImageURL: String = "",
)

enum class BebasOngkirType(val value: Int) {
    NON_BO(0),
    BO_REGULER(1),
    BO_EXTRA(2),
    BO_TOKONOW(3),
    BO_TOKONOW_20M(4),
    BO_PLUS(5),
    BO_PLUS_DT(6)
}
