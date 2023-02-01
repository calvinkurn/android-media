package com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info

import android.os.Parcel
import android.os.Parcelable

data class TopchatWebSocketLogDetailInfoUiModel(
    val source: String = "",
    val code: String = "",
    val messageId: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(source)
        parcel.writeString(code)
        parcel.writeString(messageId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopchatWebSocketLogDetailInfoUiModel> {
        override fun createFromParcel(parcel: Parcel): TopchatWebSocketLogDetailInfoUiModel {
            return TopchatWebSocketLogDetailInfoUiModel(parcel)
        }

        override fun newArray(size: Int): Array<TopchatWebSocketLogDetailInfoUiModel?> {
            return arrayOfNulls(size)
        }
    }
}
