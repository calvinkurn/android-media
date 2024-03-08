package com.tokopedia.home_account.account_settings.data.model

import com.google.gson.annotations.SerializedName

data class AccountSettingResponse(
    @SerializedName("accountSettingConfig")
    val config: Config = Config()
) {
    data class Config(
        @SerializedName("daftarAlamat")
        val daftarAlamat: Boolean = true,
        @SerializedName("dataDiri")
        val dataDiri: Boolean = true,
        @SerializedName("dokumenDataDiri")
        val dokumenDataDiri: Boolean = false,
        @SerializedName("tokopediaCorner")
        val tokopediaCorner: Boolean = false,
        @SerializedName("ubahKataSandi")
        val ubahKataSandi: Boolean = true
    )
}
