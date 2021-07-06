package com.tokopedia.loginphone.chooseaccount.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 16/04/19.
 */
data class AccountsDataModel(
        @SerializedName("accountsGetAccountsList")
        @Expose
        var accountListDataModel: AccountListDataModel = AccountListDataModel()
)

data class AccountListDataModel(
        @SerializedName("key")
        @Expose
        var key: String = "",
        @SerializedName("msisdn_view")
        @Expose
        var msisdnView: String = "",
        @SerializedName("msisdn")
        @Expose
        var msisdn: String = "",
        @SerializedName("users_details")
        @Expose
        var userDetailDataModels: List<UserDetailDataModel> = listOf(),
        @SerializedName("users_count")
        @Expose
        var userCount: Int = 0,
        @SerializedName("errors")
        @Expose
        var errorResponseDataModels: List<ErrorResponseDataModel> = listOf()
)

data class UserDetailDataModel(
        @SerializedName("user_id")
        @Expose
        var userId: String = "",
        @SerializedName("fullname")
        @Expose
        var fullname: String = "",
        @SerializedName("email")
        @Expose
        var email: String = "",
        @SerializedName("msisdn_verified")
        @Expose
        var msisdnVerified: Boolean = false,
        @SerializedName("challenge_2fa")
        @Expose
        var challenge2Fa: Boolean = false,
        @SerializedName("user_id_enc")
        @Expose
        var userIdEnc: String = "",
        @SerializedName("image")
        @Expose
        var image: String = "",
        @SerializedName("shop_detail")
        @Expose
        var shopDetailDataModel: ShopDetailDataModel?
)

data class ErrorResponseDataModel(
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("message")
        @Expose
        var message: String = ""
)

data class ShopDetailDataModel(
        @SerializedName("id")
        @Expose
        var shopId: String = "0",
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("domain")
        @Expose
        var domain: String = ""
)