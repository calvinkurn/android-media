package com.tokopedia.feedcomponent.view.viewmodel.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 23/03/19.
 */
data class CommentViewModel(
        var fmt: String = "",
        var value: Int = 0
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(fmt)
        writeInt(value)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CommentViewModel> = object : Parcelable.Creator<CommentViewModel> {
            override fun createFromParcel(source: Parcel): CommentViewModel = CommentViewModel(source)
            override fun newArray(size: Int): Array<CommentViewModel?> = arrayOfNulls(size)
        }
    }
}