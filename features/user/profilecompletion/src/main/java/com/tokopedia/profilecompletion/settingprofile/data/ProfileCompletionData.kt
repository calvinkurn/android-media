package com.tokopedia.profilecompletion.settingprofile.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 2019-07-08.
 * ade.hadian@tokopedia.com
 */

data class ProfileCompletionData(
    @SerializedName("fullName") @Expose
    var fullName: String = "",
    @SerializedName("birthDay") @Expose
    var birthDay: String = "",
    @SerializedName("gender") @Expose
    var gender: Int = 0,
    @SerializedName("email") @Expose
    var email: String = "",
    @SerializedName("msisdn") @Expose
    var phone: String = "",
    @SerializedName("isMsisdnVerified")
    var isPhoneVerified: Boolean = false,
    var profilePicture: String = ""
)