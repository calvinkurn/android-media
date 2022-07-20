package com.tokopedia.topchat.chatlist.domain.pojo.operational_insight

import com.google.gson.annotations.SerializedName

data class ShopChatTicker(
    @SerializedName("Date")
    var date: String? = "",

    @SerializedName("Data")
    var data: ShopChatMetricData? = ShopChatMetricData(),

    @SerializedName("ColorLight")
    var colorLight: ShopChatMetricColorLight? = ShopChatMetricColorLight(),

    @SerializedName("ColorDark")
    var colorDark: ShopChatMetricColorDark? = ShopChatMetricColorDark(),

    @SerializedName("Target")
    var target: ShopChatMetricTarget? = ShopChatMetricTarget(),

    @SerializedName("URL")
    var url: String? = "",

    @SerializedName("AppLink")
    var applink: String? = "",

    @SerializedName("IsMaintain")
    var isMaintain: Boolean? = false,

    @SerializedName("ShowTicker")
    var showTicker: Boolean? = false
)
