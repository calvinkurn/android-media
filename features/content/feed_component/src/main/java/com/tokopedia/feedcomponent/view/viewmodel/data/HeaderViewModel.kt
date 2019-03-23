package com.tokopedia.feedcomponent.view.viewmodel.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class HeaderViewModel(
        val avatar: String = "",
        val avatarApplink: String = "",
        val avatarBadgeImage: String = "",
        var avatarDate: String = "",
        val avatarDescription: String = "",
        val avatarTitle: String = "",
        val avatarWeblink: String = "",
        val deletable: Boolean = false,
        val editable: Boolean = false,
        val followCta: FollowCtaViewModel = FollowCtaViewModel(),
        val reportable: Boolean = false
) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                source.readString(),
                1 == source.readInt(),
                1 == source.readInt(),
                source.readParcelable<FollowCtaViewModel>(FollowCtaViewModel::class.java.classLoader),
                1 == source.readInt()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(avatar)
                writeString(avatarApplink)
                writeString(avatarBadgeImage)
                writeString(avatarDate)
                writeString(avatarDescription)
                writeString(avatarTitle)
                writeString(avatarWeblink)
                writeInt((if (deletable) 1 else 0))
                writeInt((if (editable) 1 else 0))
                writeParcelable(followCta, 0)
                writeInt((if (reportable) 1 else 0))
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<HeaderViewModel> = object : Parcelable.Creator<HeaderViewModel> {
                        override fun createFromParcel(source: Parcel): HeaderViewModel = HeaderViewModel(source)
                        override fun newArray(size: Int): Array<HeaderViewModel?> = arrayOfNulls(size)
                }
        }
}