package com.tokopedia.settingbank.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SettingBankTNC(
        @SerializedName("RichieGetTNCBankAccount")
        @Expose
        val richieTNC: RichieGetTNCBankAccount
)

data class RichieGetTNCBankAccount (
        @SerializedName("status")
        @Expose
        val status : Long,
        @SerializedName("message")
        @Expose
        val message : String,
        @SerializedName("data")
        @Expose
        val data : TemplateData
)

data class TemplateData (
        @SerializedName("template")
        @Expose
        val template : String
)