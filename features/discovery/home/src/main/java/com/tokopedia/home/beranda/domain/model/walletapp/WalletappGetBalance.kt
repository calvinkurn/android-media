package com.tokopedia.home.beranda.domain.model.walletapp


import com.google.gson.annotations.SerializedName

data class WalletappGetBalance(
    @SerializedName("activation_cta")
    val activationCta: String = "",
    @SerializedName("balance")
    val balance: List<Balance> = listOf(),
    @SerializedName("code")
    val code: String = "",
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("is_linked")
    val isLinked: Boolean = false,
    @SerializedName("masked_phone")
    val maskedPhone: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("redirect_url")
    val redirectUrl: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("wallet_name")
    val walletName: String = ""
)