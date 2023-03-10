package com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TopchatUiModel(
    @SerializedName("source")
    val source: String = "",
    @SerializedName("code")
    val code: String = "",
    @SerializedName("messageId")
    val messageId: String = "",
    @SerializedName("header")
    val header: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(source)
        parcel.writeString(code)
        parcel.writeString(messageId)
        parcel.writeString(header)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopchatUiModel> {
        override fun createFromParcel(parcel: Parcel): TopchatUiModel {
            return TopchatUiModel(parcel)
        }

        override fun newArray(size: Int): Array<TopchatUiModel?> {
            return arrayOfNulls(size)
        }
    }
}
