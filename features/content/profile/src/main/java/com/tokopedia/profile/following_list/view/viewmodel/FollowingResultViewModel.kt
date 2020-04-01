package com.tokopedia.profile.following_list.view.viewmodel

/**
 * Created by jegul on 2019-10-23
 */
interface FollowingResultViewModel<out T : FollowingViewModel> {
    val isCanLoadMore: Boolean
    val followingViewModelList: List<T>
}