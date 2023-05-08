package com.tokopedia.feedcomponent.domain.usecase.shopfollow

enum class ShopFollowAction(val value: String) {
    Follow("follow"),
    UnFollow("unfollow");

    companion object {
        fun getActionByState(isFollowed: Boolean): ShopFollowAction {
            return if (isFollowed) UnFollow else Follow
        }
    }
}
