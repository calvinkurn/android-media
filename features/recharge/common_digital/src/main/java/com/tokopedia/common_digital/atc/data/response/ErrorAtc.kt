package com.tokopedia.common_digital.atc.data.response

import com.google.gson.annotations.SerializedName

/**
 * created by @bayazidnasir on 3/8/2022
 */

data class ErrorAtc(
    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("subtitle")
    val subTitle: String = "",

    @SerializedName("atc_error_page")
    val atcErrorPage: AtcErrorPage = AtcErrorPage()
)

data class AtcErrorPage(
    @SerializedName("show_error_page")
    val isShowErrorPage: Boolean = false,

    @SerializedName("buttons")
    val buttons: List<AtcErrorButton> = emptyList()
)

data class AtcErrorButton(
    @SerializedName("label")
    val label: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("applink_url")
    val appLinkUrl: String = ""
)
