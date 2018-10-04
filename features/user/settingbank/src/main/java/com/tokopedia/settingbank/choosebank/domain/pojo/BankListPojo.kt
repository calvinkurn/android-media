package com.tokopedia.settingbank.choosebank.domain.pojo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 7/2/18.
 */
data class BankListPojo(
        @Expose
        @SerializedName("has_next_page")
        val has_next_page: Boolean = false,
        @Expose
        @SerializedName("banks")
        val banks: List<BankAccount> = ArrayList()
)

data class BankAccount(
        @Expose
        @SerializedName("bank_id")
        val bank_id: String? = "",
        @Expose
        @SerializedName("bank_name")
        val bank_name: String? = "",
        @Expose
        @SerializedName("clearing_code")
        val clearing_code: String? = "",
        @Expose
        @SerializedName("abbreviation")
        val abbreviation: String? = ""
)