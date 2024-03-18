package com.tokopedia.people.views.uimodel.state

import androidx.compose.runtime.Immutable
import com.tokopedia.content.common.util.UiEvent
import com.tokopedia.people.views.uimodel.PeopleUiModel
import java.util.UUID

@Immutable
data class FollowListState(
    val followList: List<PeopleUiModel>,
    val hasNextPage: Boolean,
    val result: Result<Unit>?,
    val isLoading: Boolean,
    val isRefreshing: Boolean,
    val countFmt: String
) {
    companion object {
        val Empty = FollowListState(
            followList = emptyList(),
            hasNextPage = false,
            result = Result.success(),
            isLoading = false,
            isRefreshing = false,
            countFmt = ""
        )
    }
}

sealed class FollowListEvent : UiEvent {

    override val id: Long = UUID.randomUUID().mostSignificantBits

    data class SuccessFollow(val isGoingToFollow: Boolean, val message: String) : FollowListEvent()

    data class FailedFollow(val isGoingToFollow: Boolean) : FollowListEvent()

    data class LoginToFollow(val people: PeopleUiModel) : FollowListEvent()
}

internal fun Result.Companion.success() = success(Unit)
