package com.tokopedia.product.detail.common.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 01/02/19.
 */
data class Variant(

        @SerializedName("ProductVariantID")
        @Expose
        val pv: Int? = null, //ex: 6528

        @SerializedName("VariantID")
        @Expose
        val v: Int? = null, //ex:1

        @SerializedName("VariantUnitID")
        @Expose
        val vu: Int? = null, // ex: 0

        @SerializedName("Name")
        @Expose
        val name: String? = null, //ex:Warna

        @SerializedName("Identifier")
        @Expose
        val identifier: String? = null, // ex: colour

        @SerializedName("UnitName")
        @Expose
        val unitName: String? = null, //example: International

        @SerializedName("Position")
        @Expose
        val position: Int? = null, //start from 1

        @SerializedName("Option")
        @Expose
        val options: List<Option> = listOf()
) {
    val isSizeIdentifier: Boolean
        get() = "size".equals(identifier, false)
}

data class Option(

        @SerializedName("ProductVariantOptionID") // ex:23454
        @Expose
        val id: Int? = null,

        @SerializedName("VariantUnitValueID") //ex: 1
        @Expose
        val vuv: Int? = null,

        @SerializedName("Value")
        @Expose
        val value: String? = null, // example: "White"

        @SerializedName("Hex")
        @Expose
        val hex: String? = null, // ex:#ff3303

        @SerializedName("picture")
        @Expose
        val picture: Picture? = null
)