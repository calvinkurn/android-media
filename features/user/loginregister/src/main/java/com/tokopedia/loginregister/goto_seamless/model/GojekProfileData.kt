package com.tokopedia.loginregister.goto_seamless.model

import com.google.gson.annotations.SerializedName

data class GojekProfileData (
    @SerializedName("countryCode")
    val countryCode: String = "",
    @SerializedName("customerId")
    val customerId: String = "",
    @SerializedName("email")
    val email: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("phone")
    val phone: String = "",
    @SerializedName("authCode")
    var authCode: String = "",
    @Transient
    var tokopediaName: String = ""
)
