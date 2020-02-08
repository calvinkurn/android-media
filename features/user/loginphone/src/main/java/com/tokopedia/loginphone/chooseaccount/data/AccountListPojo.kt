package com.tokopedia.loginphone.chooseaccount.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 16/04/19.
 */
data class AccountListPojo(
        @SerializedName("accountsGetAccountsList")
        @Expose
        var accountList: AccountList = AccountList()
)

data class AccountList(
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
        var userDetails: ArrayList<UserDetail> = ArrayList(),
        @SerializedName("users_count")
        @Expose
        var userCount: Int = 0,
        @SerializedName("errors")
        @Expose
        var errors: ArrayList<Error> = ArrayList()
)

data class UserDetail(
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
        var msisdn_verified: Boolean = false,
        @SerializedName("image")
        @Expose
        var image: String = "",
        @SerializedName("shop_detail")
        @Expose
        var shopDetail: ShopDetail = ShopDetail()
)

data class Error(
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("message")
        @Expose
        var message: String = ""
)

data class ShopDetail(
        @SerializedName("id")
        @Expose
        var id: String = "",
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("domain")
        @Expose
        var domain: String = ""
)