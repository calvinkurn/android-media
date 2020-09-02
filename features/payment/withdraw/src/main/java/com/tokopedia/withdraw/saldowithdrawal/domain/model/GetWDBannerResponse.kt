package com.tokopedia.withdraw.saldowithdrawal.domain.model


import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder

data class GetWDBannerResponse(
        @SerializedName("RichieGetWDBanner")
        val richieGetWDBanner: RichieGetWDBanner
)

data class RichieGetWDBanner(
        @SerializedName("data")
        val data: ArrayList<BannerData>,
        @SerializedName("message")
        val message: String,
        @SerializedName("status")
        val status: Int
)

data class BannerData(
        @SerializedName("ID")
        val id: Int,
        @SerializedName("BGURL")
        val bgURL: String,
        @SerializedName("CTA")
        val cta: String,
        @SerializedName("IMGURL")
        val imgURL: String,
        @SerializedName("Status")
        val status: Int,
        @SerializedName("Text1")
        val text1: String,
        @SerializedName("Text2")
        val text2: String,
        @SerializedName("Title")
        val title: String
): ImpressHolder()