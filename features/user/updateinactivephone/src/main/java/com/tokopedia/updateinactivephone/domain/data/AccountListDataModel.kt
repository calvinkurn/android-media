package com.tokopedia.updateinactivephone.domain.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountListDataModel(
        @SerializedName("accountsGetAccountsList")
        @Expose
        var accountList: AccountList = AccountList()
) : Parcelable {

    @Parcelize
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
            var userDetailDataModels: MutableList<UserDetailDataModel> = mutableListOf(),
            @SerializedName("errors")
            @Expose
            var errors: List<Error> = listOf()
    ): Parcelable

    @Parcelize
    data class UserDetailDataModel(
            @SerializedName("fullname")
            @Expose
            var fullname: String = "",
            @SerializedName("email")
            @Expose
            var email: String = "",
            @SerializedName("image")
            @Expose
            var image: String = "",
            @SerializedName("user_id_index")
            @Expose
            var index: Int = 0
    ): Parcelable

    @Parcelize
    data class Error(
            @SerializedName("name")
            @Expose
            var name: String = "",
            @SerializedName("message")
            @Expose
            var message: String = ""
    ) : Parcelable
}