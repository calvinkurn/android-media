package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

class Upsell(
    @SerializedName("is_show")
    val isShow: Boolean = false,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("app_link")
    val appLink: String = "",
    @SerializedName("image")
    val image: String = ""
)

class NewUpsell(
    @SerializedName("is_show")
    val isShow: Boolean = false,
    @SerializedName("is_selected")
    val isSelected: Boolean = false,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("app_link")
    val appLink: String = "",
    @SerializedName("image")
    val image: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    val price: Long = 0,
    @SerializedName("price_fmt")
    val priceWording: String = "",
    @SerializedName("duration")
    val duration: String = "",
    @SerializedName("summary_info")
    val summaryInfo: String = "",
    @SerializedName("button")
    val button: UpsellButton = UpsellButton(),
    @SerializedName("id")
    val id: String = "",
    @SerializedName("additional_vertical_id")
    val additionalVerticalId: String = "",
    @SerializedName("transaction_type")
    val transactionType: String = ""
)

class UpsellButton(
    @SerializedName("text")
    val text: String = ""
)
