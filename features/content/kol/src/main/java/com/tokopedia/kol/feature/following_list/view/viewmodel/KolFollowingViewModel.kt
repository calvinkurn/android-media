package com.tokopedia.kol.feature.following_list.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by yfsx on 28/12/17.
 */
class KolFollowingViewModel(
        override val id: Int,
        override val avatarUrl: String,
        val profileApplink: String,
        val profileUrl: String,
        val isInfluencer: Boolean,
        override val name: String
) : FollowingViewModel, Parcelable {
    override var isLoadingItem: Boolean = false

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readString()
    )

    constructor(isLoadingItem: Boolean): this(
            0,
            "",
            "",
            "",
            false,
            ""
    ) {
        this.isLoadingItem = isLoadingItem
    }

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(avatarUrl)
        writeString(profileApplink)
        writeString(profileUrl)
        writeInt((if (isInfluencer) 1 else 0))
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<KolFollowingViewModel> = object : Parcelable.Creator<KolFollowingViewModel> {
            override fun createFromParcel(source: Parcel): KolFollowingViewModel = KolFollowingViewModel(source)
            override fun newArray(size: Int): Array<KolFollowingViewModel?> = arrayOfNulls(size)
        }
    }
}
