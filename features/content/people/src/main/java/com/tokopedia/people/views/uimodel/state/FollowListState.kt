package com.tokopedia.people.views.uimodel.state

import androidx.compose.runtime.Immutable
import com.tokopedia.people.views.uimodel.PeopleUiModel

@Immutable
data class FollowListState(
    val followList: List<PeopleUiModel>,
    val hasNextPage: Boolean,
    val result: Result<Unit>?,
    val isLoading: Boolean,
    val countFmt: String,
) {
    companion object {
        val Empty = FollowListState(
            followList = emptyList(),
            hasNextPage = false,
            result = Result.success(),
            isLoading = false,
            countFmt = "",
        )
    }
}

internal fun Result.Companion.success() = success(Unit)
