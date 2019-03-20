package com.tokopedia.affiliate.feature.createpost.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by isfaaghyth on 20/03/19.
 * github: @isfaaghyth
 */
data class MediaModel(
        val path: String? = "",
        val type: String? = "image"
): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MediaModel> {
        override fun createFromParcel(parcel: Parcel): MediaModel {
            return MediaModel(parcel)
        }

        override fun newArray(size: Int): Array<MediaModel?> {
            return arrayOfNulls(size)
        }
    }
}

class MediaType {
    companion object {
        const val IMAGE = "image"
        const val VIDEO = "video"
    }
}