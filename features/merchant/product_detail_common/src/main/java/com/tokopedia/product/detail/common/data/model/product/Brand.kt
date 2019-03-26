package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Brand(
        @SerializedName("brandID")
        @Expose
        val id: String = "",

        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("brandStatus")
        @Expose
        val status: Int = 0
)

data class Menu(
        @SerializedName("menuID")
        @Expose
        val id: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("url")
        @Expose
        val url: String = ""
)

data class Variant(
        @SerializedName("parentID")
        @Expose
        val parentID: String = "",

        @SerializedName("isVariant")
        @Expose
        val isVariant: Boolean = false
)

data class Stock(
        @SerializedName("useStock")
        @Expose
        val useStock: Boolean = false,

        @SerializedName("value")
        @Expose
        val value: Int = 0,

        @SerializedName("stockWording")
        @Expose
        val stockWording: String = ""
)