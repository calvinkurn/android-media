package com.tokopedia.videoplayer.view.player

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.videoplayer.state.PlayerController
import com.tokopedia.videoplayer.state.PlayerType
import com.tokopedia.videoplayer.state.RepeatMode

class TkpdPlayerViewModel(
        var videoSource: String = "",
        var stateVideoPosition: Long = 0,
        var nativeController: Boolean = PlayerController.ON,
        var playerType: Int = PlayerType.DEFAULT,
        var repeatMode: Int = RepeatMode.REPEAT_MODE_OFF): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()?:"",
            parcel.readLong(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(videoSource)
        parcel.writeLong(stateVideoPosition)
        parcel.writeByte(if (nativeController) 1 else 0)
        parcel.writeInt(playerType)
        parcel.writeInt(repeatMode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TkpdPlayerViewModel> {
        override fun createFromParcel(parcel: Parcel): TkpdPlayerViewModel {
            return TkpdPlayerViewModel(parcel)
        }

        override fun newArray(size: Int): Array<TkpdPlayerViewModel?> {
            return arrayOfNulls(size)
        }
    }


}