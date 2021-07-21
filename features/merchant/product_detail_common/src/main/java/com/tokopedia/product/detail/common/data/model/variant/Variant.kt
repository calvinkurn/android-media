package com.tokopedia.product.detail.common.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 01/07/20
 */
data class Variant(
        @SerializedName("productVariantID")
        @Expose
        val pv: String? = null, //ex: 6528

        @SerializedName("variantID")
        @Expose
        val v: String? = null, //ex:1

        @SerializedName("name")
        @Expose
        val name: String? = null, //ex:Warna

        @SerializedName("identifier")
        @Expose
        val identifier: String? = null, // ex: colour

        @SerializedName("unitName")
        @Expose
        val unitName: String? = null, //example: International

        @SerializedName("option")
        @Expose
        val options: List<VariantOption> = listOf()
){
        companion object {
                const val SIZE = "size" // this is from api
                const val COLOR = "colour" // this is from api
        }

        val isSizeIdentifier: Boolean
                get() = SIZE.equals(identifier, false)
}

data class VariantOption(
        @SerializedName("productVariantOptionID") // ex:23454
        @Expose
        val id: String? = null,

        @SerializedName("variantUnitValueID") //ex: 1
        @Expose
        val vuv: String? = null,

        @SerializedName("value")
        @Expose
        val value: String? = null, // example: "White"

        @SerializedName("hex")
        @Expose
        val hex: String? = null,

        @SerializedName("picture")
        @Expose
        val picture: Picture? = null
)

