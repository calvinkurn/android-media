package com.tokopedia.home.beranda.domain.model.walletapp


import com.google.gson.annotations.SerializedName

data class WalletAppData(
    @SerializedName("activation_cta")
    val activationCta: String = "",
    @SerializedName("balance")
    val balance: List<Balance> = listOf(),
    @SerializedName("icon_url")
    val iconUrl: String = "",
    @SerializedName("is_linked")
    val isLinked: Boolean = false,
    @SerializedName("masked_phone")
    val maskedPhone: String = "",
    @SerializedName("redirect_url")
    val redirectUrl: String = "",
    @SerializedName("wallet_name")
    val walletName: String = "",
    @SerializedName("wallet_type")
    val walletType: String = ""
)