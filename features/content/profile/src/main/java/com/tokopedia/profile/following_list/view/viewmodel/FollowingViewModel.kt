package com.tokopedia.profile.following_list.view.viewmodel

/**
 * Created by jegul on 2019-10-23
 */
interface FollowingViewModel {
    val id: Int
    val avatarUrl: String
    val name: String
    val isLoadingItem: Boolean
}