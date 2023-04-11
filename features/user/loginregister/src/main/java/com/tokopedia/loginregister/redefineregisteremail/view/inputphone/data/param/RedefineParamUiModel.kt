@file:Suppress("ParamFieldAnnotation")

package com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.param

import android.os.Parcel
import android.os.Parcelable

data class RedefineParamUiModel(
    val source: String = "",
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val isRequiredInputPhone: Boolean = false,
    var token: String = "",
    val hash: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(source)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(name)
        parcel.writeByte(if (isRequiredInputPhone) 1 else 0)
        parcel.writeString(token)
        parcel.writeString(hash)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RedefineParamUiModel> {
        override fun createFromParcel(parcel: Parcel): RedefineParamUiModel {
            return RedefineParamUiModel(parcel)
        }

        override fun newArray(size: Int): Array<RedefineParamUiModel?> {
            return arrayOfNulls(size)
        }
    }
}
