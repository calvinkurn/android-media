package com.tokopedia.groupchat.room.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * @author : Steven 19/06/19
 */

class VideoStreamViewModel constructor(var isActive: Boolean = false,
                                       var isLive: Boolean = false,
                                       var orientation: String = ORIENTATION_VERTICAL,
                                       var rtmpStandard: String = "",
                                       var rtmpHigh: String = "",
                                       var hlsStandard: String = "",
                                       var hlsHigh: String = "")
    : Visitable<Any>, Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    companion object {
        const val TYPE = "video_stream"
        const val ORIENTATION_VERTICAL = "vertical"
        const val ORIENTATION_HORIZONTAL = "horizontal"

        val CREATOR: Parcelable.Creator<VideoStreamViewModel> = object : Parcelable.Creator<VideoStreamViewModel> {
            override fun createFromParcel(parcel: Parcel): VideoStreamViewModel {
                return VideoStreamViewModel(parcel)
            }

            override fun newArray(size: Int): Array<VideoStreamViewModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (isActive) 1 else 0)
        parcel.writeByte(if (isLive) 1 else 0)
        parcel.writeString(rtmpStandard)
        parcel.writeString(rtmpHigh)
        parcel.writeString(hlsStandard)
        parcel.writeString(hlsHigh)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun type(typeFactory: Any?): Int {
        return 0
    }
}