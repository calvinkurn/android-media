package com.tokopedia.people.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.people.model.MutationUiModel
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
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private data class PerPageResult(
    val result: Result<List<PeopleUiModel>>,
    val cursor: String,
    val total: String?,
)
internal class FollowListViewModel @AssistedInject constructor(
    @Assisted private val type: FollowListType,
    @Assisted private val profileIdentifier: String,
    private val userFollowRepo: UserFollowRepository
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(type: FollowListType, profileIdentifier: String): FollowListViewModel
    }

    private val _followList = MutableStateFlow<List<PeopleUiModel>>(emptyList())
    private val _nextCursor = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _result = MutableStateFlow<Result<Unit>?>(null)
    private val _countFmt = MutableStateFlow("")

    val state = combine(
        _followList,
        _nextCursor,
        _result,
        _isLoading,
        _countFmt,
    ) { followList, nextCursor, result, isLoading, countFmt ->
        FollowListState(
            followList = followList,
            hasNextPage = nextCursor == null || nextCursor.isNotBlank(),
            result = result,
            isLoading = isLoading,
            countFmt = countFmt,
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FollowListState.Empty
    )

    fun onAction(action: FollowListAction) {
        when (action) {
            FollowListAction.Init -> onInit()
            FollowListAction.Refresh -> onRefresh()
            FollowListAction.LoadMore -> onLoadMore()
            is FollowListAction.Follow -> onFollow(action.people)
        }
    }

    private fun onInit() {
        loadData(cursor = "")
    }

    private fun onRefresh() {
        loadData(cursor = "")
    }

    private fun onLoadMore() {
        val lastCursor = _nextCursor.value ?: return
        loadData(cursor = lastCursor)
    }

    private fun onFollow(people: PeopleUiModel) {
        when (people) {
            is PeopleUiModel.ShopUiModel -> followShop(people)
            is PeopleUiModel.UserUiModel -> followUser(people)
        }
    }

    private suspend fun getFollowers(cursor: String): PerPageResult {
        var nextCursor = cursor
        return try {
            var result: FollowListUiModel.Follower

            do {
                result = userFollowRepo.getFollowers(profileIdentifier, nextCursor, 10)
                nextCursor = result.nextCursor
            } while (result.followers.isEmpty() && nextCursor.isNotEmpty())

            PerPageResult(
                Result.success(result.followers),
                nextCursor,
                result.total.totalFollowers,
            )
        } catch (e: Throwable) {
            PerPageResult(
                Result.failure(e),
                nextCursor,
                null
            )
        }
    }

    private suspend fun getFollowings(cursor: String): PerPageResult {
        var nextCursor = cursor
        return try {
            var result: FollowListUiModel.Following

            do {
                result = userFollowRepo.getFollowing(profileIdentifier, nextCursor, 10)
                nextCursor = result.nextCursor
            } while (result.followingList.isEmpty() && nextCursor.isNotEmpty())

            PerPageResult(
                Result.success(result.followingList),
                nextCursor,
                result.total.totalFollowing,
            )
        } catch (e: Throwable) {
            PerPageResult(
                Result.failure(e),
                nextCursor,
                null
            )
        }
    }

    private fun loadData(cursor: String = "") {
        if (_isLoading.value) return

        _isLoading.value = true
        viewModelScope.launch {
            val (result, nextCursor, total) = when (type) {
                FollowListType.Follower -> getFollowers(cursor)
                FollowListType.Following -> getFollowings(cursor)
            }
            _result.value = result.onSuccess { followList -> _followList.update { it + followList } }.map {}
            _nextCursor.value = nextCursor
            if (total != null) {
                _countFmt.value = total
            }
            _isLoading.value = false
        }
    }

    private fun followUser(user: PeopleUiModel.UserUiModel) {
        viewModelScope.launch {
            val shouldFollow = !user.isFollowed
            val result = userFollowRepo.followUser(user.encryptedId, shouldFollow)
            if (result !is MutationUiModel.Success) return@launch

            _followList.update {
                it.map { people ->
                    if (people is PeopleUiModel.UserUiModel && people.id == user.id) {
                        people.copy(isFollowed = shouldFollow)
                    } else { people }
                }
            }
        }
    }

    private fun followShop(shop: PeopleUiModel.ShopUiModel) {
        viewModelScope.launch {
            val shouldFollow = !shop.isFollowed
            val result = userFollowRepo.followShop(shop.id, ShopFollowAction.getActionByState(!shouldFollow))
            if (result !is MutationUiModel.Success) return@launch

            _followList.update {
                it.map { people ->
                    if (people is PeopleUiModel.ShopUiModel && people.id == shop.id) {
                        people.copy(isFollowed = shouldFollow)
                    } else { people }
                }
            }
        }
    }
}
