package com.tokopedia.kol.feature.postdetail.view.datamodel.type

import com.tokopedia.shop.common.domain.interactor.UpdateFollowStatusUseCase

/**
 * Created by meyta.taliti on 03/08/22.
 */
enum class ShopFollowAction(val value: String) {
    Follow(UpdateFollowStatusUseCase.ACTION_FOLLOW),
    UnFollow(UpdateFollowStatusUseCase.ACTION_UNFOLLOW);

    val isFollowing: Boolean
        get() = this == Follow

    val isUnFollowing: Boolean
        get() = this == UnFollow

    companion object {

        fun getFollowAction(isFollowed: Boolean): ShopFollowAction {
            return if (isFollowed) UnFollow else Follow
        }
    }
}