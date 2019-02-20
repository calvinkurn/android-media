package com.tokopedia.product.detail.common.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 01/02/19.
 */
data class Variant (

    @SerializedName("pv")
    @Expose
    val pv: Int? = null,
    @SerializedName("v")
    @Expose
    val v: Int? = null,
    @SerializedName("vu")
    @Expose
    val vu: Int? = null,
    @SerializedName("name")
    @Expose
    val name: String? = null,
    @SerializedName("identifier")
    @Expose
    val identifier: String? = null,
    @SerializedName("unit_name")
    @Expose
    val unitName: String? = null,
    @SerializedName("position")
    @Expose
    val position: Int? = null,
    @SerializedName("option")
    @Expose
    val option: List<Option>? = null
)

data class Option (

        @SerializedName("id")
        @Expose
        val id: Int? = null,
        @SerializedName("vuv")
        @Expose
        val vuv: Int? = null,
        @SerializedName("value")
        @Expose
        val value: String? = null,
        @SerializedName("hex")
        @Expose
        val hex: String? = null,
        @SerializedName("picture")
        @Expose
        val picture: Picture? = null
)