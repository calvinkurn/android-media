package com.tokopedia.home.beranda.domain.model.walletapp


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Balance(
    @Expose
    @SerializedName("active")
    val active: Boolean = false,
    @Expose
    @SerializedName("amount")
    val amount: Int = 0,
    @Expose
    @SerializedName("amount_fmt")
    val amountFmt: String = "",
    @Expose
    @SerializedName("message")
    val message: String = "",
    @Expose
    @SerializedName("wallet_code")
    val walletCode: String = "",
    @Expose
    @SerializedName("code")
    val code: String = "",
    @Expose
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @Expose
    @SerializedName("is_linked")
    val isLinked: Boolean = false,
    @Expose
    @SerializedName("masked_phone")
    val maskedPhone: String = "",
    @Expose
    @SerializedName("redirect_url")
    val redirectUrl: String = "",
    @Expose
    @SerializedName("type")
    val type: String = "",
    @Expose
    @SerializedName("wallet_name")
    val walletName: String = ""
)