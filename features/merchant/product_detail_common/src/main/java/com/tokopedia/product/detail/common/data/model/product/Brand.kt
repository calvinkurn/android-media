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

data class Etalase(
        @SerializedName("id")
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

data class Media(
        @SerializedName("type")
        @Expose
        val type: String = "",

        @SerializedName("URL300")
        @Expose
        val url300: String = "",

        @SerializedName("URLOriginal")
        @Expose
        val urlOriginal: String = "",

        @SerializedName("URLThumbnail")
        @Expose
        val urlThumbnail: String = "",

        @SerializedName("Description")
        @Expose
        val mediaDescription: String = "",

        @SerializedName("VideoURLAndroid")
        @Expose
        val videoUrl: String = "",

        @SerializedName("IsAutoplay")
        @Expose
        val isAutoPlay: Boolean = false
)

data class FreeOngkir(
        @SerializedName("isActive")
        @Expose
        val isFreeOngkirActive: Boolean = false,

        @SerializedName("imgURL")
        @Expose
        val freeOngkirImgUrl: String = ""
)
