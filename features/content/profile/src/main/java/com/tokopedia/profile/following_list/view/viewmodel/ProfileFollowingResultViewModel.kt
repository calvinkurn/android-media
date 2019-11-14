package com.tokopedia.profile.following_list.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by yfsx on 29/12/17.
 */
data class ProfileFollowingResultViewModel(
        override val isCanLoadMore: Boolean,
        override val followingViewModelList: List<ProfileFollowingViewModel>,
        val lastCursor: String,
        val buttonText: String,
        val buttonApplink: String
) : FollowingResultViewModel<ProfileFollowingViewModel>, Parcelable {

    constructor() : this(
            false,
            emptyList(),
            "",
            "",
            ""
    )

    constructor(source: Parcel) : this(
            1 == source.readInt(),
            source.createTypedArrayList(ProfileFollowingViewModel.CREATOR),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt((if (isCanLoadMore) 1 else 0))
        writeTypedList(followingViewModelList)
        writeString(lastCursor)
        writeString(buttonText)
        writeString(buttonApplink)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProfileFollowingResultViewModel> = object : Parcelable.Creator<ProfileFollowingResultViewModel> {
            override fun createFromParcel(source: Parcel): ProfileFollowingResultViewModel = ProfileFollowingResultViewModel(source)
            override fun newArray(size: Int): Array<ProfileFollowingResultViewModel?> = arrayOfNulls(size)
        }
    }
}