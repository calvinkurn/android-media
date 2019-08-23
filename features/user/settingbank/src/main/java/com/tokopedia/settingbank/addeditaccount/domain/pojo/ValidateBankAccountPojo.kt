package com.tokopedia.settingbank.addeditaccount.domain.pojo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
data class ValidateBankAccountPojo(
        @Expose
        @SerializedName("is_valid")
        val is_valid: Boolean? = false,
        @Expose
        @SerializedName("is_data_change")
        val is_data_change: Boolean? = false,
        @Expose
        @SerializedName("form_info")
        val form_info: List<FormInfoPojo> = ArrayList()
)