package com.tokopedia.feedcomponent.view.viewmodel.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 23/03/19.
 */
data class LikeViewModel(
        var fmt: String = "",
        var value: Int = 0,
        var isChecked: Boolean = false
) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString(),
                source.readInt(),
                1 == source.readInt()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(fmt)
                writeInt(value)
                writeInt((if (isChecked) 1 else 0))
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<LikeViewModel> = object : Parcelable.Creator<LikeViewModel> {
                        override fun createFromParcel(source: Parcel): LikeViewModel = LikeViewModel(source)
                        override fun newArray(size: Int): Array<LikeViewModel?> = arrayOfNulls(size)
                }
        }
}