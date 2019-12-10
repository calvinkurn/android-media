package com.tokopedia.play_common.media

import android.os.Parcel
import android.os.Parcelable

class PlaybackInfo(
    var resumeWindow: Int = INDEX_UNSET,
    var resumePosition: Long = TIME_UNSET,
    var volumeInfo: VolumeInfo = VolumeInfo(false, 1f)
) : Parcelable{

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readLong(),
            parcel.readParcelable(VolumeInfo::class.java.classLoader)) {
    }

    fun reset(){
        resumeWindow = INDEX_UNSET
        resumePosition = TIME_UNSET
        volumeInfo = VolumeInfo(false, 1f)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaybackInfo

        if (resumeWindow != other.resumeWindow) return false
        if (resumePosition != other.resumePosition) return false
        if (volumeInfo != other.volumeInfo) return false

        return true
    }

    override fun hashCode(): Int {
        var result = resumeWindow
        result = 31 * result + resumePosition.hashCode()
        result = 31 * result + volumeInfo.hashCode()
        return result
    }

    override fun toString(): String {
        return "PlaybackInfo(resumeWindow=$resumeWindow, resumePosition=$resumePosition, volumeInfo=$volumeInfo)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(resumeWindow)
        parcel.writeLong(resumePosition)
        parcel.writeParcelable(volumeInfo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaybackInfo> {
        private const val INDEX_UNSET = -1
        private const val TIME_UNSET = Long.MIN_VALUE + 1
        override fun createFromParcel(parcel: Parcel): PlaybackInfo {
            return PlaybackInfo(parcel)
        }

        override fun newArray(size: Int): Array<PlaybackInfo?> {
            return arrayOfNulls(size)
        }
    }


}