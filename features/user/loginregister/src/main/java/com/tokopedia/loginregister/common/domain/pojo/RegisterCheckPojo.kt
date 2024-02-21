package com.tokopedia.loginregister.common.domain.pojo

import com.google.gson.annotations.SerializedName

data class RegisterCheckPojo(
    @SerializedName("registerCheck")
    var data: RegisterCheckData = RegisterCheckData()
)

data class RegisterCheckData(
    @SerializedName("isExist")
    var isExist: Boolean = false,
    @SerializedName("registerOvoEnable")
    var isShowRegisterOvo: Boolean = false,
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
    @SerializedName("uh")
    var useHash: Boolean = false,
    @SerializedName("errors")
    var errors: ArrayList<String> = arrayListOf()
)
