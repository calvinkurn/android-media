package com.tokopedia.pdp.fintech.domain.datamodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class WidgetDetailV3(
    @SerializedName("paylater_getPDPWidgetV3") var baseWidgetResponse: WidgetDetailV3Data = WidgetDetailV3Data()
)

@Parcelize
data class WidgetDetailV3Data(
    @SerializedName("data") var baseData: List<WidgetDetailV3Item> = arrayListOf()
) : Parcelable


@Parcelize
data class WidgetDetailV3Item(
    @SerializedName("icon_url_light")
    var iconUrlLight: String = "",
    @SerializedName("icon_url_dark")
    var iconUrlDark: String = "",
    @SerializedName("android_url")
    var androidUrl: String = "",
    @SerializedName("messages")
    var messages: List<String> = arrayListOf(),
    @SerializedName("price")
    var price: Double = 0.0,
    @SerializedName("product_id")
    var productId: String = "",
    @SerializedName("usecase_rank")
    var usecaseRank: String = "",
    @SerializedName("installment_amt")
    var installmentAmt: String = "",
    @SerializedName("linking_status")
    var linkingStatus: String = "",
    @SerializedName("user_state")
    var userState: String = "",
    @SerializedName("widget_type")
    var widgetType: String = "",
    @SerializedName("product_code")
    var productCode: String = ""
) : Parcelable

