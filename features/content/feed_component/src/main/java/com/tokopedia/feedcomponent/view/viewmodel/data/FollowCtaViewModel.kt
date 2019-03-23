package com.tokopedia.feedcomponent.view.viewmodel.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FollowCtaViewModel(
        val authorID: String = "",
        val authorType: String = "",
        var isFollow: Boolean = false,
        val textFalse: String = "",
        val textTrue: String = ""
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(authorID)
        writeString(authorType)
        writeInt((if (isFollow) 1 else 0))
        writeString(textFalse)
        writeString(textTrue)
    }

    companion object {
        const val AUTHOR_USER = "user"

        const val AUTHOR_SHOP = "shop"

        @JvmField
        val CREATOR: Parcelable.Creator<FollowCtaViewModel> = object : Parcelable.Creator<FollowCtaViewModel> {
            override fun createFromParcel(source: Parcel): FollowCtaViewModel = FollowCtaViewModel(source)
            override fun newArray(size: Int): Array<FollowCtaViewModel?> = arrayOfNulls(size)
        }
    }
}