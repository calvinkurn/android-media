package com.tokopedia.profile.following_list.view.viewmodel

/**
 * Created by jegul on 2019-10-23
 */
data class ShopFollowingResultViewModel(
        override val isCanLoadMore: Boolean,
        override val followingViewModelList: List<ShopFollowingViewModel>,
        val totalCount: Int,
        val currentPage: Int
) : FollowingResultViewModel<ShopFollowingViewModel>