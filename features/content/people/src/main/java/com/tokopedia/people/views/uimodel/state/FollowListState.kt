package com.tokopedia.people.views.uimodel.state

import androidx.compose.runtime.Immutable
import com.tokopedia.people.views.uimodel.PeopleUiModel

@Immutable
data class FollowListState(
    val followList: List<PeopleUiModel>,
    val hasNextPage: Boolean
) {
    companion object {
        val Empty = FollowListState(
            followList = emptyList(),
            hasNextPage = false
        )
    }
}
