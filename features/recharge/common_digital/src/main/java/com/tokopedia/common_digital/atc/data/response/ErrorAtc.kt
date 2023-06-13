package com.tokopedia.common_digital.atc.data.response

import com.google.gson.annotations.SerializedName

/**
 * created by @bayazidnasir on 3/8/2022
 */

data class Errors(
    @SerializedName("errors")
    val errors: List<ErrorAtc> = emptyList()
)

data class ErrorAtc(
    @SerializedName("status")
    val status: Int = 0,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("applink_url")
    val appLinkUrl: String = "",

    @SerializedName("error_page")
    val atcErrorPage: AtcErrorPage = AtcErrorPage()
)

data class AtcErrorPage(
    @SerializedName("show_error_page")
    val isShowErrorPage: Boolean = false,

    @SerializedName("title")
    val title: String = "",

    @SerializedName("sub_title")
    val subTitle: String = "",

    @SerializedName("image_url")
    val imageUrl:String = "",

    @SerializedName("buttons")
    val buttons: List<AtcErrorButton> = emptyList()
)

data class AtcErrorButton(
    @SerializedName("label")
    val label: String = "",

    @SerializedName("url")
    val url: String = "",

    @SerializedName("applink_url")
    val appLinkUrl: String = "",

    @SerializedName("type")
    val type: String = "",

    @SerializedName("action_type")
    val actionType: String = ""
) {
    companion object{
        const val TYPE_PHONE_VERIFICATION = "verify_phone"
    }
}
