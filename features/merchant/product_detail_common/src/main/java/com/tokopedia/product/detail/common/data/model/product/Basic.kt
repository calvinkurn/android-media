package com.tokopedia.product.detail.common.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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

data class VariantBasic(
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

data class Stats(
        @SerializedName("countReview")
        @Expose
        val countReview: String = "",

        @SerializedName("countTalk")
        @Expose
        val countTalk: String = "",

        @SerializedName("rating")
        @Expose
        val rating: Float = 0f
)

data class Category(
        @SerializedName("breadcrumbUrl")
        @Expose
        val breadcrumbUrl: String = "",

        @SerializedName("detail")
        @Expose
        val detail: List<Detail> = listOf(),

        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("isAdult")
        @Expose
        val isAdult: Boolean = false,

        @SerializedName("isKyc")
        @Expose
        val isKyc: Boolean = false,

        @SerializedName("title")
        @Expose
        val title: String = ""
) {

    fun getCategoryNameFormatted(): String {
        return detail.map {
            it.name
        }.joinToString("/", postfix = "/ ${id}")
    }

    fun getCategoryIdFormatted(): String {
        return detail.map {
            it.id
        }.joinToString("/")
    }

    class Detail(
            @SerializedName("breadcrumbUrl")
            @Expose
            val breadcrumbUrl: String = "",

            @SerializedName("id")
            @Expose
            val id: String = "",

            @SerializedName("name")
            @Expose
            val name: String = ""
    )
}

data class TxStatsDynamicPdp(
        @SerializedName("transactionSuccess")
        @Expose
        val txSuccess: String = "",
        @SerializedName("transactionReject")
        @Expose
        val txReject: String = "",
        @SerializedName("countSold")
        @Expose
        val countSold: String = "",
        @SerializedName("paymentVerified")
        @Expose
        val paymentVerified: String = "",
        @SerializedName("itemSoldFmt")
        @Expose
        val itemSoldFmt: String = ""
)

data class PostAtcLayout(
    @SerializedName("layoutID")
    @Expose
    val layoutId: String = ""
)
