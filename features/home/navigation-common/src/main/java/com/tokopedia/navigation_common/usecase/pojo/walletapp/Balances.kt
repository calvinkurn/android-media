package com.tokopedia.navigation_common.usecase.pojo.walletapp

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Balances(
    @Expose
    @SerializedName("activation_cta")
    val activationCta: String = "",
    @Expose
    @SerializedName("balance")
    val balance: List<Balance> = listOf(),
    @Expose
    @SerializedName("reserve_balance")
    val reserveBalance: List<ReserveBalance> = listOf(),
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
    @SerializedName("message")
    val message: String = "",
    @Expose
    @SerializedName("redirect_url")
    val redirectUrl: String = "",
    @Expose
    @SerializedName("type")
    val type: String = "",
    @Expose
    @SerializedName("wallet_name")
    val walletName: String = "",
    @Expose
    @SerializedName("global_menu_text")
    val globalMenuText: GlobalMenuText = GlobalMenuText()
)