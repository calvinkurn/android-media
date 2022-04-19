package com.tokopedia.profile.following_list.view.viewmodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by yfsx on 28/12/17.
 */
@Parcelize
class UserFollowingViewModel(
        override val id: Int,
        override val avatarUrl: String,
        val profileApplink: String,
        val profileUrl: String,
        val isInfluencer: Boolean,
        override val name: String
) : FollowingViewModel, Parcelable {
    override var isLoadingItem: Boolean = false

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

}
