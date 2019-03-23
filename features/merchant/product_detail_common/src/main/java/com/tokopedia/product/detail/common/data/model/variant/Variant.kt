package com.tokopedia.product.detail.common.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 01/02/19.
 */
data class Variant(

        @SerializedName("productVariantID")
        @Expose
        val pv: Int? = null, //ex: 6528

        @SerializedName("variantID")
        @Expose
        val v: Int? = null, //ex:1

        @SerializedName("variantUnitID")
        @Expose
        val vu: Int? = null, // ex: 0

        @SerializedName("name")
        @Expose
        val name: String? = null, //ex:Warna

        @SerializedName("identifier")
        @Expose
        val identifier: String? = null, // ex: colour

        @SerializedName("unitName")
        @Expose
        val unitName: String? = null, //example: International

        @SerializedName("position")
        @Expose
        val position: Int? = null, //start from 1

        @SerializedName("option")
        @Expose
        val options: List<Option> = listOf()
) {
        companion object {
                const val SIZE = "size" // this is from api
                const val COLOR = "colour" // this is from api
        }
        val isSizeIdentifier: Boolean
                get() = SIZE.equals(identifier, false)
}

data class Option(

        @SerializedName("productVariantOptionID") // ex:23454
        @Expose
        val id: Int? = null,

        @SerializedName("variantUnitValueID") //ex: 1
        @Expose
        val vuv: Int? = null,

        @SerializedName("value")
        @Expose
        val value: String? = null, // example: "White"

        @SerializedName("hex")
        @Expose
        val hex: String? = null, // ex:#ff3303

        @SerializedName("picture")
        @Expose
        val picture: Picture? = null
)