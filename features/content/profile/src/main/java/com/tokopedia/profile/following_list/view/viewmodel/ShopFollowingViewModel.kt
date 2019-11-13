package com.tokopedia.profile.following_list.view.viewmodel

/**
 * Created by jegul on 2019-10-23
 */
data class ShopFollowingViewModel(
        override val id: Int,
        override val avatarUrl: String,
        override val name: String,
        override val isLoadingItem: Boolean,
        val etalase: String,
        val product: String
) : FollowingViewModel