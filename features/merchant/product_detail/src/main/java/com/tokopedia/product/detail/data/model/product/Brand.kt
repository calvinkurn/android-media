package com.tokopedia.product.detail.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Brand(
        @SerializedName("brandID")
        @Expose
        val id: Int = 0,

        @SerializedName("isActive")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("name")
        @Expose
        val name: String = ""
)

data class Catalog(
        @SerializedName("id")
        @Expose
        val id: Int = 0,

        @SerializedName("is_active")
        @Expose
        val isActive: Boolean = false,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("url")
        @Expose
        val url: String = ""
)

data class Menu(
        @SerializedName("menuID")
        @Expose
        val id: Int = 0,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("url")
        @Expose
        val url: String = ""
)