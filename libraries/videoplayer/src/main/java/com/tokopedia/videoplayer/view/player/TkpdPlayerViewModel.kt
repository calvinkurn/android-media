package com.tokopedia.videoplayer.view.player

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.videoplayer.utils.RepeatMode

class TkpdPlayerViewModel(
        var videoSource: String = "",
        var repeatMode: Int = RepeatMode.REPEAT_MODE_OFF): Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(videoSource)
        parcel.writeInt(repeatMode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val TAG: String = TkpdPlayerViewModel::class.java.simpleName

        @JvmField
        val CREATOR: Parcelable.Creator<TkpdPlayerViewModel> = object : Parcelable.Creator<TkpdPlayerViewModel> {
            override fun createFromParcel(source: Parcel): TkpdPlayerViewModel = TkpdPlayerViewModel(source)
            override fun newArray(size: Int): Array<TkpdPlayerViewModel?> = arrayOfNulls(size)
        }
    }

}