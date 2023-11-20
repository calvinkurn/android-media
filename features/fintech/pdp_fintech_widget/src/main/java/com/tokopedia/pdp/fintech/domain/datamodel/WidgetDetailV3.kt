package com.tokopedia.pdp.fintech.domain.datamodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class WidgetDetailV3(
    @SerializedName("paylater_getPDPWidgetV3") var baseWidgetResponse: WidgetDetailV3Data = WidgetDetailV3Data()
)

@Parcelize
data class WidgetDetailV3Data(
    @SerializedName("data") var baseData: WidgetDetailV3List = WidgetDetailV3List()
) : Parcelable

@Parcelize
data class WidgetDetailV3List(
    @SerializedName("list") var list: List<WidgetDetailV3Item> = arrayListOf()
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
    @SerializedName("parent_product_id")
    var parentProductId: String = "",
    @SerializedName("usecase_rank")
    var usecaseRank: Int = 0,
    @SerializedName("installment_amt")
    var installmentAmt: Float = 0f,
    @SerializedName("linking_status")
    var linkingStatus: String = "",
    @SerializedName("user_state")
    var userState: String = "",
    @SerializedName("widget_type")
    var widgetType: String = "",
    @SerializedName("product_code")
    var productCode: String = "",
    @SerializedName("gateway_id")
    var gatewayId: String = "",
    @SerializedName("tenure")
    var tenure: Int = 0
) : Parcelable

