package com.tokopedia.settingbank.banklist.domain.pojo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
/**
 * @author by nisie on 6/20/18.
 */

data class DeleteBankAccountPojo(
        @Expose
        @SerializedName("is_success")
        val is_success: Boolean? = false
)