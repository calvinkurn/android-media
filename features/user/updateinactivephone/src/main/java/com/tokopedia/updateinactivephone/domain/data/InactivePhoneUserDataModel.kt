package com.tokopedia.updateinactivephone.domain.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InactivePhoneUserDataModel (
    @Expose
    @SerializedName("userIdEnc")
    var userIdEnc: String = "",
    @Expose
    @SerializedName("email")
    var email: String = "",
    @Expose
    @SerializedName("oldPhoneNumber")
    var oldPhoneNumber: String = "",
    @Expose
    @SerializedName("newPhoneNumber")
    var newPhoneNumber: String = "",
    @Expose
    @SerializedName("userIndex")
    var userIndex: Int = 0,
    @Expose
    @SerializedName("validateToken")
    var validateToken: String = ""
): Parcelable