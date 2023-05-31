package com.tokopedia.chooseaccount.data

import com.google.gson.annotations.SerializedName

data class GetOclAccountResponse(
    @SerializedName("get_ocl")
    val data: GetOclAccountData
)

data class GetOclAccountData(
    @SerializedName("list_users")
    val users: ArrayList<OclAccount> = arrayListOf(),
    @SerializedName("ocl_jwt_token")
    val token: String = "",
    @SerializedName("message_error")
    val error: String = "",
)

data class OclAccount(
    @SerializedName("full_name")
    val fullName: String = "",
    @SerializedName("profile_picture")
    val profilePicture: String = "",
    @SerializedName("ocl_token")
    val token: String = "",
    @SerializedName("login_type")
    val loginType: String = "",
    @SerializedName("login_type_wording")
    val loginTypeWording: String = ""
)
