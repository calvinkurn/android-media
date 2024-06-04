package com.tokopedia.people.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.util.UiEventManager
import com.tokopedia.content.common.util.combine
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.people.data.UserFollowRepository
import com.tokopedia.people.views.uimodel.FollowListType
import com.tokopedia.people.views.uimodel.FollowListUiModel
import com.tokopedia.people.views.uimodel.PeopleUiModel
import com.tokopedia.people.views.uimodel.action.FollowListAction
import com.tokopedia.people.views.uimodel.setIsFollowed
import com.tokopedia.people.views.uimodel.state.FollowListEvent
import com.tokopedia.people.views.uimodel.state.FollowListState
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private data class PerPageResult(
    val result: Result<List<PeopleUiModel>>,
    val cursor: String,
    val total: String?
)
internal class FollowListViewModel @AssistedInject constructor(
    @Assisted internal val type: FollowListType,
    @Assisted private val profileIdentifier: String,
    private val userFollowRepo: UserFollowRepository,
    private val dispatchers: CoroutineDispatchers,
    private val uiEventManager: UiEventManager<FollowListEvent>,
    private val userSession: UserSessionInterface
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(type: FollowListType, profileIdentifier: String): FollowListViewModel
    }

    private val _followMap = MutableStateFlow(emptyMap<PeopleIdentifier, PeopleUiModel>())
    private val _nextCursor = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _isRefreshing = MutableStateFlow(false)
    private val _result = MutableStateFlow<Result<Unit>?>(null)
    private val _countFmt = MutableStateFlow("")

    val uiState = combine(
        _followMap,
        _nextCursor,
        _result,
        _isLoading,
        _isRefreshing,
        _countFmt
    ) { followMap, nextCursor, result, isLoading, isRefreshing, countFmt ->
        FollowListState(
            followList = followMap.values.toList(),
            hasNextPage = nextCursor == null || nextCursor.isNotBlank(),
            result = result,
            isLoading = isLoading,
            isRefreshing = isRefreshing,
            countFmt = countFmt
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FollowListState.Empty
    )

    val uiEvent: Flow<FollowListEvent?>
        get() = uiEventManager.event

    fun onAction(action: FollowListAction) {
        when (action) {
            FollowListAction.Init -> onInit()
            FollowListAction.Refresh -> onRefresh()
            FollowListAction.LoadMore -> onLoadMore()
            is FollowListAction.Follow -> onFollow(action.people)
            is FollowListAction.UpdateUserFollowFromResult -> {
                onUpdateFollowFromResult(User(action.id), action.isFollowing)
            }
            is FollowListAction.UpdateShopFollowFromResult -> {
                onUpdateFollowFromResult(Shop(action.id), action.isFollowing)
            }
            is FollowListAction.ConsumeEvent -> onConsumeEvent(action.event)
        }
    }

    private fun onInit() {
        loadData(cursor = "")
    }

    private fun onRefresh() {
        loadData(cursor = "", shouldRefresh = true)
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

    private fun onUpdateFollowFromResult(
        identifier: PeopleIdentifier,
        isFollowing: Boolean
    ) {
        _followMap.update {
            val people = it[identifier] ?: return@update it
            it + mapOf(identifier to people.setIsFollowed(isFollowing))
        }
    }

    private fun onConsumeEvent(event: FollowListEvent) {
        viewModelScope.launch { uiEventManager.clearEvent(event.id) }
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
                result.total.totalFollowers
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
                result.total.totalFollowing
            )
        } catch (e: Throwable) {
            PerPageResult(
                Result.failure(e),
                nextCursor,
                null
            )
        }
    }

    private fun loadData(
        cursor: String = "",
        shouldRefresh: Boolean = false
    ) {
        if (_isLoading.value) return

        if (shouldRefresh) _isRefreshing.update { true }
        _isLoading.update { true }

        viewModelScope.launch(dispatchers.io) {
            val (result, nextCursor, total) = when (type) {
                FollowListType.Follower -> getFollowers(cursor)
                FollowListType.Following -> getFollowings(cursor)
            }

            result
                .onSuccess { followList ->
                    _followMap.update {
                        if (shouldRefresh) {
                            followList.transformToMap()
                        } else {
                            it + followList.transformToMap()
                        }
                    }
                }
                .onFailure {
                    if (!shouldRefresh) return@onFailure
                    _followMap.update { emptyMap() }
                }

            _result.update { result.map {} }
            _nextCursor.update { nextCursor }
            if (total != null) _countFmt.update { total }
        }.invokeOnCompletion {
            _isLoading.update { false }
            _isRefreshing.update { false }
        }
    }

    private fun followUser(user: PeopleUiModel.UserUiModel) {
        if (!userSession.isLoggedIn) {
            emitEvent(FollowListEvent.LoginToFollow(user))
            return
        }

        viewModelScope.launch {
            val shouldFollow = !user.isFollowed

            runCatching {
                when (val result = userFollowRepo.followUser(user.encryptedId, shouldFollow)) {
                    is MutationUiModel.Error -> error(result.message)
                    is MutationUiModel.Success -> result.message
                }
            }.onFailure {
                emitEvent(FollowListEvent.FailedFollow(shouldFollow))
            }.onSuccess { message ->
                _followMap.update {
                    it + mapOf(user.identifier to user.copy(isFollowed = shouldFollow))
                }
                if (message.isBlank()) return@onSuccess
                emitEvent(FollowListEvent.SuccessFollow(shouldFollow, message))
            }
        }
    }

    private fun followShop(shop: PeopleUiModel.ShopUiModel) {
        if (!userSession.isLoggedIn) {
            emitEvent(FollowListEvent.LoginToFollow(shop))
            return
        }

        viewModelScope.launch {
            val shouldFollow = !shop.isFollowed

            runCatching {
                when (
                    val result = userFollowRepo.followShop(
                        shop.id,
                        ShopFollowAction.getActionByState(!shouldFollow)
                    )
                ) {
                    is MutationUiModel.Error -> error(result.message)
                    is MutationUiModel.Success -> result.message
                }
            }.onFailure {
                emitEvent(FollowListEvent.FailedFollow(shouldFollow))
            }.onSuccess { message ->
                _followMap.update {
                    it + mapOf(shop.identifier to shop.copy(isFollowed = shouldFollow))
                }
                if (message.isBlank()) return@onSuccess
                emitEvent(FollowListEvent.SuccessFollow(shouldFollow, message))
            }
        }
    }

    private fun emitEvent(event: FollowListEvent) {
        viewModelScope.launch { uiEventManager.emitEvent(event) }
    }

    private fun List<PeopleUiModel>.transformToMap() = associateBy { it.identifier }

    private val PeopleUiModel.identifier: PeopleIdentifier
        get() = when (this) {
            is PeopleUiModel.UserUiModel -> User(id)
            is PeopleUiModel.ShopUiModel -> Shop(id)
        }

    private sealed interface PeopleIdentifier

    @JvmInline
    private value class User(val id: String) : PeopleIdentifier

    @JvmInline
    private value class Shop(val id: String) : PeopleIdentifier
}
