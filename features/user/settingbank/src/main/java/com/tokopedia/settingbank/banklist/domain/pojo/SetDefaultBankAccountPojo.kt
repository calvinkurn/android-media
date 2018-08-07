package com.tokopedia.settingbank.banklist.domain.pojo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
/**
 * @author by nisie on 6/8/18.
 */
data class SetDefaultBankAccountPojo(
        @Expose
        @SerializedName("is_success")
        val is_success: Boolean? = false
)

