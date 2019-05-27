package com.tokopedia.home_recom.model.entity


import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("foreign_title")
        val foreignTitle: String,
        @SerializedName("productDetailData")
        val productDetailData: List<ProductDetailData>,
        @SerializedName("source")
        val source: String,
        @SerializedName("tid")
        val tid: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("widget_url")
        val widgetUrl: String
)