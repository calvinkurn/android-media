package com.tokopedia.analyticsdebugger.websocket.ui.uimodel.info

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PlayUiModel(
    @SerializedName("source")
    val source: String = "",
    @SerializedName("channelId")
    val channelId: String = "",
    @SerializedName("warehouseId")
    val warehouseId: String = "",
    @SerializedName("gcToken")
    val gcToken: String = "",
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(source)
        parcel.writeString(channelId)
        parcel.writeString(warehouseId)
        parcel.writeString(gcToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlayUiModel> {
        override fun createFromParcel(parcel: Parcel): PlayUiModel {
            return PlayUiModel(parcel)
        }

        override fun newArray(size: Int): Array<PlayUiModel?> {
            return arrayOfNulls(size)
        }
    }
}
