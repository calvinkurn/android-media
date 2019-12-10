package com.tokopedia.play_common.media

import android.os.Parcel
import android.os.Parcelable

class VolumeInfo (
        val mute: Boolean,
        val volume: Float
) : Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readFloat()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (mute) 1 else 0)
        parcel.writeFloat(volume)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VolumeInfo

        if (mute != other.mute) return false
        if (volume != other.volume) return false

        return true
    }

    override fun hashCode(): Int {
        var result = mute.hashCode()
        result = 31 * result + volume.hashCode()
        return result
    }

    override fun toString(): String {
        return "VolumeInfo(mute=$mute, volume=$volume)"
    }

    companion object CREATOR : Parcelable.Creator<VolumeInfo> {
        override fun createFromParcel(parcel: Parcel): VolumeInfo {
            return VolumeInfo(parcel)
        }

        override fun newArray(size: Int): Array<VolumeInfo?> {
            return arrayOfNulls(size)
        }
    }


}