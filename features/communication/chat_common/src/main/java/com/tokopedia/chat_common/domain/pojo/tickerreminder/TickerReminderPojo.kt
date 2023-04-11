package com.tokopedia.chat_common.domain.pojo.tickerreminder

import com.google.gson.annotations.SerializedName

data class TickerReminderPojo (
    @SerializedName("enable")
    var isEnable: Boolean? = false,

    @SerializedName("enable_close")
    var isEnableClose: Boolean? = false,

    @SerializedName("feature_id")
    var featureId: Long? = 0,

    @SerializedName("main_text")
    var mainText: String? = "",

    @SerializedName("regex_message")
    var regexMessage: String? = "",

    @SerializedName("local_id")
    var localId: String? = "",

    @SerializedName("sub_text")
    var subText: String? = "",

    @SerializedName("ticker_type")
    var tickerType: String? = "",

    @SerializedName("url")
    var url: String? = "",

    @SerializedName("url_label")
    var urlLabel: String? = ""
)