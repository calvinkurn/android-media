package com.tokopedia.settingbank.addeditaccount.domain.pojo

data class ValidateBankAccountPojo(
        @Expose
        @SerializedName("is_valid")
        val is_valid: Boolean? = false,
        @Expose
        @SerializedName("is_data_change")
        val is_data_change: Boolean? = false,
        @Expose
        @SerializedName("form_info")
        val form_info: List<FormInfo> = ArrayList()
)

data class FormInfo(
        @Expose
        @SerializedName("param_name")
        val param_name: String? = "",
        @Expose
        @SerializedName("messages")
        val messages: String? = ""
)