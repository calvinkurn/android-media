package com.tokopedia.loginregister.shopcreation.data

import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-12-27.
 * ade.hadian@tokopedia.com
 */

data class RegisterCheckPojo(
    @SerializedName("registerCheck")
    var data: RegisterCheckData = RegisterCheckData()

)

data class RegisterCheckData(
    @SerializedName("isExist")
    var isExist: Boolean = false,
    @SerializedName("isPending")
    var isPending: Boolean = false,
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("registerType")
    var registerType: String = "",
    @SerializedName("userID")
    var userID: String = "",
    @SerializedName("view")
    var view: String = "",
    @SerializedName("errors")
    var errors: ArrayList<String> = arrayListOf()
)
