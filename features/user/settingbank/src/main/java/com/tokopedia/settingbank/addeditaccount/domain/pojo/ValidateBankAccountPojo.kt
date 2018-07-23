package com.tokopedia.settingbank.addeditaccount.domain.pojo

data class ValidateBankAccountPojo(
        val is_valid: Boolean? = false,
        val is_data_change: Boolean? = false,
        val form_info: List<FormInfo> = ArrayList()
)

data class FormInfo(
        val param_name: String? = "",
        val messages: String? = ""
)