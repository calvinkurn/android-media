package com.tokopedia.videoplayer.view.player

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.videoplayer.utils.RepeatMode

class TkpdPlayerViewModel(
        var videoSource: String = "",
        var nativeController: Boolean = true,
        var repeatMode: Int = RepeatMode.REPEAT_MODE_OFF): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString()?:"",
            parcel.readByte() != 0.toByte(),
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(videoSource)
        parcel.writeByte(if (nativeController) 1 else 0)
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