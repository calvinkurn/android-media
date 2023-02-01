package com.tokopedia.analyticsdebugger.websocket.ui.uimodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info.PlayWebSocketLogGeneralInfoUiModel
import com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info.TopchatWebSocketLogDetailInfoUiModel

/**
 * Created By : Jonathan Darwin on December 01, 2021
 */

sealed class WebSocketLog

data class WebSocketLogUiModel(
    val id: Long = 0,
    val event: String,
    val message: String,
    val dateTime: String = "",
    val play: PlayWebSocketLogGeneralInfoUiModel? = null,
    val topchat: TopchatWebSocketLogDetailInfoUiModel? = null
) : WebSocketLog(), Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readParcelable(PlayWebSocketLogGeneralInfoUiModel::class.java.classLoader),
        parcel.readParcelable(TopchatWebSocketLogDetailInfoUiModel::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(event)
        parcel.writeString(message)
        parcel.writeString(dateTime)
        parcel.writeParcelable(play, flags)
        parcel.writeParcelable(topchat, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WebSocketLogUiModel> {
        override fun createFromParcel(parcel: Parcel): WebSocketLogUiModel {
            return WebSocketLogUiModel(parcel)
        }

        override fun newArray(size: Int): Array<WebSocketLogUiModel?> {
            return arrayOfNulls(size)
        }
    }
}
