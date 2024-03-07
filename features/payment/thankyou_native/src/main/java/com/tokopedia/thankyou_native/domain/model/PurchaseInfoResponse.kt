package com.tokopedia.thankyou_native.domain.model

import com.google.gson.annotations.SerializedName

data class PurchaseInfoResponse(
    @SerializedName("getPurchaseInfo")
    val purchaseInfo: PurchaseInfo
)

data class PurchaseInfo(
    @SerializedName("summarySection")
    val summarySection: List<Section> = arrayListOf(),
    @SerializedName("orderSection")
    val orderSection: List<Section> = arrayListOf()
)

data class Section(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("priceStr")
    val priceStr: String = "",
    @SerializedName("totalPriceStr")
    val totalPriceStr: String = "",
    @SerializedName("quantity")
    val quantity: Long = 0L,
    @SerializedName("details")
    val details: Details,
    @SerializedName("textType")
    val textType: String = "",
    @SerializedName("colorName")
    val colorName: String = "",
    @SerializedName("colorPrice")
    val colorPrice: String = "",
    @SerializedName("iconURL")
    val iconURL: String = "",
)

data class Details(
    @SerializedName("tooltip_title")
    val tooltipTitle: String = "",
    @SerializedName("tooltip_description")
    val tooltipDescription: String = "",
    @SerializedName("slashed_price")
    val slashedPrice: String = "",
    @SerializedName("total_price_subtitle")
    val totalPriceSubtitle: String = "",
    @SerializedName("custom_notes")
    val customNotes: String = "",
    @SerializedName("items")
    val items: List<Item> = arrayListOf(),
    @SerializedName("shipping_name")
    val shippingName: String = "",
    @SerializedName("shipping_eta")
    val shippingEta: String = "",
    @SerializedName("name_subtitle")
    val nameSubtitle: String = ""
)

data class Item(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("price_str")
    val priceStr: String = "",
    @SerializedName("total_price_str")
    val totalPriceStr: String = "",
    @SerializedName("quantity")
    val quantity: Long = 0L,
)
