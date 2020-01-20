package com.tokopedia.profile.following_list.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by yfsx on 29/12/17.
 */
data class UserFollowingResultViewModel(
        override val isCanLoadMore: Boolean,
        override val followingViewModelList: List<UserFollowingViewModel>,
        val lastCursor: String,
        val buttonText: String,
        val buttonApplink: String
) : FollowingResultViewModel<UserFollowingViewModel>, Parcelable {

    constructor() : this(
            false,
            emptyList(),
            "",
            "",
            ""
    )

    constructor(source: Parcel) : this(
            1 == source.readInt(),
            source.createTypedArrayList(UserFollowingViewModel.CREATOR),
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
        val CREATOR: Parcelable.Creator<UserFollowingResultViewModel> = object : Parcelable.Creator<UserFollowingResultViewModel> {
            override fun createFromParcel(source: Parcel): UserFollowingResultViewModel = UserFollowingResultViewModel(source)
            override fun newArray(size: Int): Array<UserFollowingResultViewModel?> = arrayOfNulls(size)
        }
    }
}