package com.tokopedia.kol.feature.following_list.view.viewmodel

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by yfsx on 29/12/17.
 */
data class KolFollowingResultViewModel(
        override val isCanLoadMore: Boolean,
        override val followingViewModelList: List<KolFollowingViewModel>,
        val lastCursor: String,
        val buttonText: String,
        val buttonApplink: String
) : FollowingResultViewModel<KolFollowingViewModel>, Parcelable {

    constructor() : this(
            false,
            emptyList(),
            "",
            "",
            ""
    )

    constructor(source: Parcel) : this(
            1 == source.readInt(),
            source.createTypedArrayList(KolFollowingViewModel.CREATOR),
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
        val CREATOR: Parcelable.Creator<KolFollowingResultViewModel> = object : Parcelable.Creator<KolFollowingResultViewModel> {
            override fun createFromParcel(source: Parcel): KolFollowingResultViewModel = KolFollowingResultViewModel(source)
            override fun newArray(size: Int): Array<KolFollowingResultViewModel?> = arrayOfNulls(size)
        }
    }
}