package com.tokopedia.sessioncommon.data.register

import com.google.gson.annotations.SerializedName

data class RegisterV2Model(

    @SerializedName("register_v2")
    val register: Register = Register()

)

data class PopupError(

    @SerializedName("header")
    val header: String = "",

    @SerializedName("action")
    val action: String = "",

    @SerializedName("body")
    val body: String = ""

)

data class ErrorsItem(

    @SerializedName("name")
    val name: String = "",

    @SerializedName("message")
    val message: String = ""

)

data class Register(

    @SerializedName("access_token")
    val accessToken: String = "",

    @SerializedName("refresh_token")
    val refreshToken: String = "",

    @SerializedName("token_type")
    var tokenType: String = "",

    @SerializedName("is_active")
    val isActive: Int = 0,

    @SerializedName("user_id")
    val userId: String = "",

    @SerializedName("enable_skip_2fa")
    val enableSkip2fa: Boolean = false,

    @SerializedName("enable_2fa")
    val enable2fa: Boolean = false,

    @SerializedName("errors")
    val errors: List<ErrorsItem> = emptyList(),

    @SerializedName("popup_error")
    val popupError: PopupError = PopupError(),

    @SerializedName("sid")
    val sid: String = ""

)
