package com.tokopedia.people.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.people.data.UserFollowRepository
import com.tokopedia.people.views.uimodel.FollowListType
import com.tokopedia.people.views.uimodel.FollowListUiModel
import com.tokopedia.people.views.uimodel.PeopleUiModel
import com.tokopedia.people.views.uimodel.action.FollowListAction
import com.tokopedia.people.views.uimodel.state.FollowListState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class FollowListViewModel @AssistedInject constructor(
    @Assisted private val type: FollowListType,
    @Assisted private val profileIdentifier: String,
    private val userRepo: UserFollowRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(type: FollowListType, profileIdentifier: String): FollowListViewModel
    }

    private val _followList = MutableStateFlow<List<PeopleUiModel>>(emptyList())

    val state = combine(
        _followList,
        flow { emit("a") }
    ) { followList, _ ->
        FollowListState(
            followList = followList
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FollowListState.Empty
    )

    fun onAction(action: FollowListAction) {
        when (action) {
            FollowListAction.Init -> onInit()
        }
    }

    private fun onInit() {
        viewModelScope.launch {
            val followList = when (type) {
                FollowListType.Follower -> getFollowers("")
                FollowListType.Following -> getFollowings("")
            }
            _followList.value = followList
        }
    }

    private suspend fun getFollowers(cursor: String): List<PeopleUiModel> {
        return try {
            var result: FollowListUiModel.Follower
            var nextCursor = cursor

            do {
                result = userRepo.getMyFollowers(profileIdentifier, nextCursor, 10)
                nextCursor = cursor
            } while (result.followers.isEmpty() && nextCursor.isNotEmpty())

            result.followers
        } catch (e: Throwable) { emptyList() }
    }

    private suspend fun getFollowings(cursor: String): List<PeopleUiModel> {
        return try {
            var result: FollowListUiModel.Following
            var nextCursor = cursor

            do {
                result = userRepo.getMyFollowing(profileIdentifier, nextCursor, 10)
                nextCursor = cursor
            } while (result.followingList.isEmpty() && nextCursor.isNotEmpty())

            result.followingList
        } catch (e: Throwable) { emptyList() }
    }
}
