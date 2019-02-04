package com.tokopedia.product.detail.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 01/02/19.
 */
data class Variant (

    @SerializedName("pv")
    @Expose
    private val pv: Int? = null,
    @SerializedName("v")
    @Expose
    private val v: Int? = null,
    @SerializedName("vu")
    @Expose
    private val vu: Int? = null,
    @SerializedName("name")
    @Expose
    private val name: String? = null,
    @SerializedName("identifier")
    @Expose
    private val identifier: String? = null,
    @SerializedName("unit_name")
    @Expose
    private val unitName: String? = null,
    @SerializedName("position")
    @Expose
    private val position: Int? = null,
    @SerializedName("option")
    @Expose
    private val option: List<Option>? = null
)

data class Option (

        @SerializedName("id")
        @Expose
        private val id: Int? = null,
        @SerializedName("vuv")
        @Expose
        private val vuv: Int? = null,
        @SerializedName("value")
        @Expose
        private val value: String? = null,
        @SerializedName("hex")
        @Expose
        private val hex: String? = null,
        @SerializedName("picture")
        @Expose
        private val picture: Picture? = null
)