package com.tokopedia.settingbank.addeditaccount.domain.pojo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
/**
 * @author by nisie on 6/22/18.
 */
data class AddBankAccountPojo(
        @Expose
        @SerializedName("is_success")
        val is_success: Boolean = false,

        @Expose
        @SerializedName("acc_id")
        val acc_id: String = ""

)