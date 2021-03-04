package com.tokopedia.product.detail.data.model.restrictioninfo

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