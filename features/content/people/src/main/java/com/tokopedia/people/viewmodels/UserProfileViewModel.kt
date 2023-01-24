package com.tokopedia.people.viewmodels

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.content.common.producttag.util.extension.combine
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction.Follow
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction.UnFollow
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomFollowState.*
import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.model.PlayGetContentSlot
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.content.UserFeedPostsUiModel
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.profile.*
import com.tokopedia.people.views.uimodel.saved.SavedReminderData
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileViewModel @AssistedInject constructor(
    @Assisted private val username: String,
    private val repo: UserProfileRepository,
    private val userSession: UserSessionInterface
) : BaseViewModel(Dispatchers.Main) {

    @AssistedFactory
    interface Factory {
        fun create(username: String): UserProfileViewModel
    }

    /** Public Getter */
    val displayName: String
        get() = _profileInfo.value.name

    val profileUserID: String
        get() = _profileInfo.value.userID

    val profileUserEncryptedID: String
        get() = _profileInfo.value.encryptedUserID

    val profileUsername: String
        get() = _profileInfo.value.username

    val profileCover: String
        get() = _profileInfo.value.imageCover

    val isBlocking: Boolean
        get() = _profileInfo.value.isBlocking

    val totalFollower: String
        get() = _profileInfo.value.stats.totalFollowerFmt

    val totalFollowing: String
        get() = _profileInfo.value.stats.totalFollowingFmt

    val totalPost: String
        get() = _profileInfo.value.stats.totalPostFmt

    val isFollowed: Boolean
        get() = _followInfo.value.status

    val isSelfProfile: Boolean
        get() = _profileType.value == ProfileType.Self

    val profileWebLink: String
        get() = _profileInfo.value.shareLink.webLink

    val needOnboarding: Boolean
        get() = _profileWhitelist.value.hasAcceptTnc.not()

    val isShopRecomShow: Boolean get() = _shopRecom.value.isShown

    private val _savedReminderData = MutableStateFlow<SavedReminderData>(SavedReminderData.NoData)
    private val _profileInfo = MutableStateFlow(ProfileUiModel.Empty)
    private val _followInfo = MutableStateFlow(FollowInfoUiModel.Empty)
    private val _profileWhitelist = MutableStateFlow(ProfileWhitelistUiModel.Empty)
    private val _profileType = MutableStateFlow(ProfileType.Unknown)
    private val _shopRecom = MutableStateFlow(ShopRecomUiModel())
    private val _profileTab = MutableStateFlow(ProfileTabUiModel())
    private val _feedPostsContent = MutableStateFlow(UserFeedPostsUiModel())
    private val _videoPostContent = MutableStateFlow(UserPostModel())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<Throwable?>(null)

    private val _uiEvent = MutableSharedFlow<UserProfileUiEvent>()

    val uiEvent: Flow<UserProfileUiEvent>
        get() = _uiEvent

    val uiState = combine(
        _profileInfo,
        _followInfo,
        _profileType,
        _profileWhitelist,
        _shopRecom,
        _profileTab,
        _feedPostsContent,
        _videoPostContent,
        _isLoading,
        _error
    ) { profileInfo, followInfo, profileType, profileWhitelist, shopRecom, profileTab, feedPostsContent, videoPostContent,
        isLoading, error ->
        UserProfileUiState(
            profileInfo = profileInfo,
            followInfo = followInfo,
            profileType = profileType,
            profileWhitelist = profileWhitelist,
            shopRecom = shopRecom,
            profileTab = profileTab,
            feedPostsContent = feedPostsContent,
            videoPostsContent = videoPostContent,
            isLoading = isLoading,
            error = error
        )
    }

    fun submitAction(action: UserProfileAction) {
        when (action) {
            is UserProfileAction.ClickFollowButton -> handleClickFollowButton(action.isFromLogin)
            is UserProfileAction.ClickFollowButtonShopRecom -> handleClickFollowButtonShopRecom(
                action.itemID
            )
            is UserProfileAction.ClickUpdateReminder -> handleClickUpdateReminder(action.isFromLogin)
            is UserProfileAction.LoadFeedPosts -> handleLoadFeedPosts(action.cursor, action.isRefresh)
            is UserProfileAction.LoadPlayVideo -> handleLoadPlayVideo(action.cursor)
            is UserProfileAction.LoadProfile -> handleLoadProfile(action.isRefresh)
            is UserProfileAction.LoadNextPageShopRecom -> handleLoadNextPageShopRecom(action.nextCurSor)
            is UserProfileAction.RemoveShopRecomItem -> handleRemoveShopRecomItem(action.itemID)
            is UserProfileAction.SaveReminderActivityResult -> handleSaveReminderActivityResult(
                action.channelId,
                action.position,
                action.isActive
            )
            UserProfileAction.BlockUser -> handleBlockUser()
            UserProfileAction.UnblockUser -> handleUnblockUser()
        }
    }

    /** Handle Action */
    private fun handleLoadProfile(isRefresh: Boolean) {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launchCatchError(block = {
            loadProfileInfo(isRefresh)
            _isLoading.value = false
            _error.value = null
        }) {
            _isLoading.value = false
            _error.value = it
            _uiEvent.emit(UserProfileUiEvent.ErrorLoadProfile(it))
        }
    }

    private fun handleLoadFeedPosts(cursor: String, isRefresh: Boolean) {
        viewModelScope.launchCatchError(
            block = {
                val currentSize = _feedPostsContent.value.posts.size
                val data = if (isRefresh) {
                    repo.getFeedPosts(profileUserID, "", if (currentSize == 0) DEFAULT_LIMIT else currentSize)
                } else {
                    repo.getFeedPosts(profileUserID, cursor, DEFAULT_LIMIT)
                }

                val finalPosts = (
                    if (cursor.isEmpty() || isRefresh) {
                        data.posts
                    } else {
                        _feedPostsContent.value.posts + data.posts
                    }
                    ).distinctBy { it.id }

                _feedPostsContent.update {
                    it.copy(
                        pagination = data.pagination,
                        posts = finalPosts
                    )
                }
            },
            onError = {
                _uiEvent.emit(UserProfileUiEvent.ErrorFeedPosts(it))
            }
        )
    }

    private fun handleLoadPlayVideo(cursor: String) {
        viewModelScope.launchCatchError(
            block = {
                val data = repo.getPlayVideo(profileUserID, cursor)
                val finalPosts = if (cursor.isEmpty()) {
                    data.playGetContentSlot.data
                } else {
                    _videoPostContent.value.playGetContentSlot.data + data.playGetContentSlot.data
                }

                _videoPostContent.update {
                    it.copy(
                        playGetContentSlot = PlayGetContentSlot(
                            data = finalPosts,
                            playGetContentSlot = data.playGetContentSlot.playGetContentSlot
                        )
                    )
                }
            },
            onError = {
                _uiEvent.emit(UserProfileUiEvent.ErrorVideoPosts(it))
            }
        )
    }

    private fun handleBlockUser() {
        viewModelScope.launchCatchError(block = {
            repo.blockUser(profileUserID)
            _profileInfo.update { it.copy(isBlocking = true) }
            _uiEvent.emit(UserProfileUiEvent.SuccessBlockUser(isBlocking = true))
        }) {
            _uiEvent.emit(UserProfileUiEvent.ErrorBlockUser(isBlocking = true))
        }
    }

    private fun handleUnblockUser() {
        viewModelScope.launchCatchError(block = {
            repo.unblockUser(profileUserID)
            _profileInfo.update { it.copy(isBlocking = false) }
            _uiEvent.emit(UserProfileUiEvent.SuccessBlockUser(isBlocking = false))
        }) {
            _uiEvent.emit(UserProfileUiEvent.ErrorBlockUser(isBlocking = false))
        }
    }

    private fun handleClickFollowButton(isFromLogin: Boolean) {
        viewModelScope.launchCatchError(
            block = {
                if (isFromLogin) loadProfileInfo(false)

                val followInfo = _followInfo.value

                if (userSession.isLoggedIn.not() || (isFollowed && isFromLogin) || isSelfProfile) {
                    return@launchCatchError
                }

                val result = if (followInfo.status) {
                    repo.unFollowProfile(followInfo.encryptedUserID)
                } else {
                    repo.followProfile(followInfo.encryptedUserID)
                }

                when (result) {
                    is MutationUiModel.Success -> {
                        _followInfo.update { it.copy(status = !followInfo.status) }
                        _profileInfo.update { repo.getProfile(followInfo.userID) }
                    }
                    is MutationUiModel.Error -> {
                        _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(Throwable(result.message)))
                    }
                }
            }
        ) {
            _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(Throwable()))
        }
    }

    private fun handleClickUpdateReminder(isFromLogin: Boolean) {
        viewModelScope.launchCatchError(
            block = {
                if (isFromLogin) loadProfileInfo(false)

                val data = _savedReminderData.value

                if (data is SavedReminderData.Saved) {
                    val result = repo.updateReminder(data.channelId, data.isActive)

                    _uiEvent.emit(
                        when (result) {
                            is MutationUiModel.Success -> {
                                handleRemoveReminderActivityResult()
                                UserProfileUiEvent.SuccessUpdateReminder(
                                    result.message,
                                    data.position
                                )
                            }
                            is MutationUiModel.Error -> UserProfileUiEvent.ErrorUpdateReminder(
                                Exception(result.message)
                            )
                        }
                    )
                }
            },
            onError = {
                _uiEvent.emit(UserProfileUiEvent.ErrorUpdateReminder(it))
            }
        )
    }

    private fun handleSaveReminderActivityResult(
        channelId: String,
        position: Int,
        isActive: Boolean
    ) {
        _savedReminderData.update {
            SavedReminderData.Saved(
                channelId = channelId,
                position = position,
                isActive = isActive
            )
        }
    }

    private fun handleRemoveReminderActivityResult() {
        _savedReminderData.update { SavedReminderData.NoData }
    }

    private fun handleClickFollowButtonShopRecom(itemId: Long) {
        val currentItem = _shopRecom.value.items.find { it.id == itemId } ?: return
        val currentState =
            if (currentItem.state == LOADING_FOLLOW || currentItem.state == LOADING_UNFOLLOW) {
                return
            } else {
                currentItem.state
            }
        val isCurrentStateFollow = currentState == FOLLOW
        val loadingState = if (isCurrentStateFollow) LOADING_FOLLOW else LOADING_UNFOLLOW
        val followInfo = _followInfo.value

        viewModelScope.launchCatchError(
            block = {
                updateLoadingStateFollowShopRecom(itemId, loadingState)

                val result = when (currentItem.type) {
                    FOLLOW_TYPE_SHOP -> {
                        repo.shopFollowUnfollow(
                            currentItem.id.toString(),
                            if (currentState == FOLLOW) UnFollow else Follow
                        )
                    }
                    FOLLOW_TYPE_BUYER -> {
                        if (currentState == FOLLOW) {
                            repo.unFollowProfile(currentItem.encryptedID)
                        } else {
                            repo.followProfile(currentItem.encryptedID)
                        }
                    }
                    else -> return@launchCatchError
                }

                when (result) {
                    is MutationUiModel.Success -> {
                        _profileInfo.update { repo.getProfile(followInfo.userID) }
                        _shopRecom.update { data ->
                            data.copy(
                                items = data.items.map {
                                    if (currentItem.id == it.id) {
                                        it.copy(state = if (currentState == FOLLOW) UNFOLLOW else FOLLOW)
                                    } else {
                                        it
                                    }
                                }
                            )
                        }
                    }
                    is MutationUiModel.Error -> {
                        updateLoadingStateFollowShopRecom(itemId, currentState)
                        _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(Throwable(result.message)))
                    }
                }
            },
            onError = {
                updateLoadingStateFollowShopRecom(itemId, currentState)
                _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(Throwable()))
            }
        )
    }

    private fun updateLoadingStateFollowShopRecom(itemID: Long, state: ShopRecomFollowState) {
        _shopRecom.update { data ->
            data.copy(
                items = data.items.map {
                    if (itemID == it.id) {
                        it.copy(state = state)
                    } else {
                        it
                    }
                }
            )
        }
    }

    private fun handleRemoveShopRecomItem(itemID: Long) {
        _shopRecom.update { data ->
            data.copy(items = data.items.filterNot { it.id == itemID })
        }
    }

    /** Helper */
    private suspend fun loadProfileInfo(isRefresh: Boolean) {
        val profileInfo = repo.getProfile(username)

        val profileType = if (userSession.isLoggedIn) {
            if (userSession.userId == profileInfo.userID) {
                ProfileType.Self
            } else {
                ProfileType.OtherUser
            }
        } else {
            ProfileType.NotLoggedIn
        }

        val followInfo = if (userSession.isLoggedIn) {
            repo.getFollowInfo(listOf(profileInfo.userID))
        } else {
            FollowInfoUiModel.Empty
        }

        _profileInfo.update { profileInfo }
        _followInfo.update { followInfo }
        _profileType.update { profileType }

        if (profileType == ProfileType.Self) _profileWhitelist.update { repo.getWhitelist() }

        if (_profileInfo.value.isBlocking) {
            _feedPostsContent.value = UserFeedPostsUiModel()
            viewModelScope.launch {
                _uiEvent.emit(UserProfileUiEvent.BlockingUserState(
                    MessageErrorException("User ini diblokir")
                ))
            }
            return
        }

        if (isRefresh) loadProfileTab()
    }

    private suspend fun loadProfileTab() {
        viewModelScope.launchCatchError(
            block = {
                val result = repo.getUserProfileTab(_profileInfo.value.userID)
                val isEmpty = result == ProfileTabUiModel()
                _profileTab.update { result }
                _uiEvent.emit(UserProfileUiEvent.SuccessLoadTabs(isEmpty))

                if (isEmpty && isSelfProfile) loadShopRecom()
            },
            onError = {
                _uiEvent.emit(UserProfileUiEvent.ErrorGetProfileTab(it))
            }
        )
    }

    private fun loadShopRecom(cursor: String = "") {
        viewModelScope.launchCatchError(
            block = {
                val result = repo.getShopRecom(cursor)
                if (result.isShown) {
                    val items = if (cursor.isEmpty()) {
                        result.items
                    } else {
                        _shopRecom.value.items + result.items
                    }

                    _shopRecom.update {
                        it.copy(
                            isShown = true,
                            nextCursor = result.nextCursor,
                            title = result.title,
                            loadNextPage = result.loadNextPage,
                            items = items,
                            isRefresh = cursor.isEmpty()
                        )
                    }
                } else {
                    _shopRecom.update { ShopRecomUiModel() }
                }
            },
            onError = {
                _shopRecom.update { ShopRecomUiModel() }
            }
        )
    }

    private fun handleLoadNextPageShopRecom(nextCursor: String) {
        viewModelScope.launchCatchError(
            block = {
                if (nextCursor.isEmpty()) return@launchCatchError
                loadShopRecom(nextCursor)
            },
            onError = {
                _uiEvent.emit(UserProfileUiEvent.ErrorLoadNextPageShopRecom(it))
            }
        )
    }

    companion object {
        const val UGC_ONBOARDING_OPEN_FROM_LIVE = 1
        const val UGC_ONBOARDING_OPEN_FROM_POST = 2
        private const val FOLLOW_TYPE_SHOP = 2
        private const val FOLLOW_TYPE_BUYER = 3
        private const val DEFAULT_LIMIT = 10
    }
}
