package com.tokopedia.profile.following_list.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by yfsx on 29/12/17.
 */
@Parcelize
data class UserFollowingResultViewModel(
        override val isCanLoadMore: Boolean = false,
        override val followingViewModelList: List<UserFollowingViewModel> = emptyList(),
        val lastCursor: String = "",
        val buttonText: String = "",
        val buttonApplink: String = ""
) : FollowingResultViewModel<UserFollowingViewModel>, Parcelable