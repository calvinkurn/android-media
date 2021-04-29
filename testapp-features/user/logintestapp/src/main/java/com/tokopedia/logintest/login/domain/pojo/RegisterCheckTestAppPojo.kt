package com.tokopedia.logintest.login.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-10-15.
 * ade.hadian@tokopedia.com
 */

data class RegisterCheckPojo(
        @SerializedName("registerCheck")
        @Expose
        var data: RegisterCheckData = RegisterCheckData()

)

data class RegisterCheckData(
        @SerializedName("isExist")
        @Expose
        var isExist: Boolean = false,
        @SerializedName("isPending")
        @Expose
        var isPending: Boolean = false,
        @SerializedName("status")
        @Expose
        var status: Int = 0,
        @SerializedName("registerType")
        @Expose
        var registerType: String = "",
        @SerializedName("userID")
        @Expose
        var userID: String = "",
        @SerializedName("view")
        @Expose
        var view: String = "",
        @SerializedName("uh")
        @Expose
        var useHash: Boolean = false,
        @SerializedName("errors")
        @Expose
        var errors: ArrayList<String> = arrayListOf()
)