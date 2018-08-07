package com.tokopedia.settingbank.banklist.domain.pojo
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
/**
 * @author by nisie on 6/8/18.
 */
data class BankAccountListPojo(
        @Expose
        @SerializedName("paging")
        val paging: PagingModel? = PagingModel(),
        @Expose
        @SerializedName("bank_accounts")
        val bank_accounts: List<BankAccount> = ArrayList(),
        @Expose
        @SerializedName("user_info")
        val user_info: UserInfo = UserInfo()
)

data class PagingModel(
        @Expose
        @SerializedName("uri_next")
        val uri_next: String? = "",
        @Expose
        @SerializedName("uri_previous")
        val uri_previous: String? = "",
        @Expose
        @SerializedName("current")
        val current: String? = ""
)

data class BankAccount(
        @Expose
        @SerializedName("bank_id")
        val bank_id: String? = "",
        @Expose
        @SerializedName("acc_id")
        val acc_id: String? = "",
        @Expose
        @SerializedName("acc_name")
        val acc_name: String? = "",
        @Expose
        @SerializedName("branch")
        val branch: String? = "",
        @Expose
        @SerializedName("bank_name")
        val bank_name: String? = "",
        @Expose
        @SerializedName("acc_number")
        val acc_number: String? = "",
        @Expose
        @SerializedName("primary")
        val primary: Boolean = false,
        @Expose
        @SerializedName("bank_image_url")
        val bank_image_url: String? = "",
        @Expose
        @SerializedName("type")
        val type: Int? = 0
)

data class UserInfo(
        @Expose
        @SerializedName("is_verified")
        val is_verified: Boolean? = false,
        @Expose
        @SerializedName("messages")
        val messages: String? = ""
)