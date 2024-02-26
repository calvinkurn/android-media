package com.tokopedia.people.views.uimodel.action

import com.tokopedia.people.views.uimodel.PeopleUiModel

internal sealed interface FollowListAction {

    object Init : FollowListAction

    object Refresh : FollowListAction

    object LoadMore : FollowListAction

    data class Follow(val people: PeopleUiModel) : FollowListAction

    data class UpdateUserFollowFromResult(val id: String, val isFollowing: Boolean) : FollowListAction

    data class UpdateShopFollowFromResult(val id: String, val isFollowing: Boolean) : FollowListAction
}
