package com.tokopedia.people.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomFollowState
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomFollowState.FOLLOW
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomFollowState.UNFOLLOW
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomFollowState.LOADING_FOLLOW
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomFollowState.LOADING_UNFOLLOW
import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModel
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction.Follow
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction.UnFollow
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.people.Resources
import com.tokopedia.people.Success
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.views.uimodel.MutationUiModel
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.profile.*
import com.tokopedia.people.views.uimodel.saved.SavedReminderData
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update

class UserProfileViewModel @AssistedInject constructor(
    @Assisted private val username: String,
    private val repo: UserProfileRepository,
    private val userSession: UserSessionInterface,
) : BaseViewModel(Dispatchers.Main) {

    @AssistedFactory
    interface Factory {
        fun create(username: String): UserProfileViewModel
    }

    /**
     * play video will be moved to dedicated fragment when
     * developing another tab user profile eventually. so gonna leave as is for now
     * */
    private val userPost = MutableLiveData<Boolean>()
    val userPostLiveData : LiveData<Boolean> get() = userPost

    private val playPostContent = MutableLiveData<Resources<UserPostModel>>()
    val playPostContentLiveData : LiveData<Resources<UserPostModel>> get() = playPostContent

    private val userPostError = MutableLiveData<Throwable>()
    val userPostErrorLiveData : LiveData<Throwable> get() = userPostError





    /** Public Getter */
    val displayName: String
        get() = _profileInfo.value.name

    val profileUserID: String
        get() = _profileInfo.value.userID

    val profileUsername: String
        get() = _profileInfo.value.username

    val profileCover: String
        get() = _profileInfo.value.imageCover

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

    private val _uiEvent = MutableSharedFlow<UserProfileUiEvent>()

    val uiEvent: Flow<UserProfileUiEvent>
        get() = _uiEvent

    val uiState = combine(
        _profileInfo,
        _followInfo,
        _profileType,
        _profileWhitelist,
        _shopRecom,
    ) { profileInfo, followInfo, profileType, profileWhitelist, shopRecom ->
        UserProfileUiState(
            profileInfo = profileInfo,
            followInfo = followInfo,
            profileType = profileType,
            profileWhitelist = profileWhitelist,
            shopRecom = shopRecom
        )
    }

    fun submitAction(action: UserProfileAction) {
        when (action) {
            is UserProfileAction.LoadProfile -> handleLoadProfile(action.isRefresh)
            is UserProfileAction.LoadPlayVideo -> handleLoadPlayVideo(action.cursor)
            is UserProfileAction.ClickFollowButton -> handleClickFollowButton(action.isFromLogin)
            is UserProfileAction.ClickUpdateReminder -> handleClickUpdateReminder(action.isFromLogin)
            is UserProfileAction.SaveReminderActivityResult -> handleSaveReminderActivityResult(action.channelId, action.position, action.isActive)
            is UserProfileAction.RemoveReminderActivityResult -> handleRemoveReminderActivityResult()
            is UserProfileAction.ClickFollowButtonShopRecom -> handleClickFollowButtonShopRecom(action.itemID)
            is UserProfileAction.RemoveShopRecomItem -> handleRemoveShopRecomItem(action.itemID)
        }
    }

    /** Handle Action */
    private fun handleLoadProfile(isRefresh: Boolean) {
        viewModelScope.launchCatchError(block = {
            loadProfileInfo(isRefresh)
        }) {
            _uiEvent.emit(UserProfileUiEvent.ErrorLoadProfile(it))
        }
    }

    /**
     * play video will be moved to dedicated fragment when
     * developing another tab user profile eventually. so gonna leave as is for now
     * */
    private fun handleLoadPlayVideo(cursor: String) {
        viewModelScope.launchCatchError(block = {
            val data = repo.getPlayVideo(profileUserID, cursor)
            if (data != null) playPostContent.value = Success(data)
            else throw NullPointerException("data is null")
        }, onError = {
            userPostError.value = it
        })
    }

    private fun handleClickFollowButton(isFromLogin: Boolean) {
        viewModelScope.launchCatchError(block = {
            if (isFromLogin) loadProfileInfo(false)

            val followInfo = _followInfo.value

            if(userSession.isLoggedIn.not() || (isFollowed && isFromLogin) || isSelfProfile)
                return@launchCatchError

            val result = if(followInfo.status) repo.unFollowProfile(followInfo.encryptedUserID)
                        else repo.followProfile(followInfo.encryptedUserID)

            when(result) {
                is MutationUiModel.Success -> {
                    _followInfo.update { it.copy(status = !followInfo.status) }
                    _profileInfo.update {  repo.getProfile(followInfo.userID) }
                }
                is MutationUiModel.Error -> {
                    _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(result.message))
                }
            }
        }) {
            _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(""))
        }
    }

    private fun handleClickUpdateReminder(isFromLogin: Boolean) {
        viewModelScope.launchCatchError(block = {
            if (isFromLogin) loadProfileInfo(false)

            val data = _savedReminderData.value

            if(data is SavedReminderData.Saved) {
                val result = repo.updateReminder(data.channelId, data.isActive)

                _uiEvent.emit(
                    when(result) {
                        is MutationUiModel.Success -> {
                            submitAction(UserProfileAction.RemoveReminderActivityResult)
                            UserProfileUiEvent.SuccessUpdateReminder(result.message, data.position)
                        }
                        is MutationUiModel.Error -> UserProfileUiEvent.ErrorUpdateReminder(Exception(result.message))
                    }
                )
            }
        }, onError = {
            _uiEvent.emit(UserProfileUiEvent.ErrorUpdateReminder(it))
        })
    }

    private fun handleSaveReminderActivityResult(
        channelId: String,
        position: Int,
        isActive: Boolean,
    ) {
        _savedReminderData.update {
            SavedReminderData.Saved(
                channelId = channelId,
                position = position,
                isActive = isActive,
            )
        }
    }

    private fun handleRemoveReminderActivityResult() {
        _savedReminderData.update { SavedReminderData.NoData }
    }

    private fun handleClickFollowButtonShopRecom(itemId: Long) {
        val currentItem = _shopRecom.value.items.find { it.id == itemId } ?: return
        val currentState = if (currentItem.state == LOADING_FOLLOW || currentItem.state == LOADING_UNFOLLOW) return
        else currentItem.state
        val isCurrentStateFollow = currentState == FOLLOW
        val loadingState = if (isCurrentStateFollow) LOADING_FOLLOW else LOADING_UNFOLLOW
        val followInfo = _followInfo.value

        viewModelScope.launchCatchError(block = {
            updateLoadingStateFollowShopRecom(itemId, loadingState)

            val result = when (currentItem.type) {
                FOLLOW_TYPE_SHOP -> {
                    repo.shopFollowUnfollow(
                        currentItem.id.toString(),
                        if (currentState == FOLLOW) UnFollow else Follow
                    )
                }
                FOLLOW_TYPE_BUYER -> {
                    if (currentState == FOLLOW) repo.unFollowProfile(currentItem.encryptedID)
                    else repo.followProfile(currentItem.encryptedID)
                }
                else -> return@launchCatchError
            }

            when (result) {
                is MutationUiModel.Success -> {
                    _profileInfo.update { repo.getProfile(followInfo.userID) }
                    _shopRecom.update { data ->
                        data.copy(items = data.items.map {
                            if (currentItem.id == it.id) {
                                it.copy(state = if (currentState == FOLLOW) UNFOLLOW else FOLLOW)
                            }
                            else it
                        })
                    }
                }
                is MutationUiModel.Error -> {
                    updateLoadingStateFollowShopRecom(itemId, currentState)
                    _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(result.message))
                }
            }
        }, onError = {
            updateLoadingStateFollowShopRecom(itemId, currentState)
            _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(""))
        })
    }

    private fun updateLoadingStateFollowShopRecom(itemID: Long, state: ShopRecomFollowState) {
        _shopRecom.update { data ->
            data.copy(
                items = data.items.map {
                    if (itemID == it.id) it.copy(state = state)
                    else it
                })
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

        val profileType = if(userSession.isLoggedIn) {
            if(userSession.userId == profileInfo.userID)
                ProfileType.Self
            else ProfileType.OtherUser
        }
        else ProfileType.NotLoggedIn

        val followInfo = if(userSession.isLoggedIn)
            repo.getFollowInfo(listOf(profileInfo.userID))
        else FollowInfoUiModel.Empty

        _profileInfo.update { profileInfo }
        _followInfo.update { followInfo }
        _profileType.update { profileType }

        if (profileType == ProfileType.Self) {
            _profileWhitelist.update { repo.getWhitelist() }
            loadShopRecom()
        }

        /**
         * play video will be moved to dedicated fragment when
         * developing another tab user profile eventually. so gonna leave as is for now
         * */
        userPost.value = isRefresh
    }

    private suspend fun loadShopRecom() {
        val result = repo.getShopRecom()
        if (result.isShown) _shopRecom.emit(result)
        else _shopRecom.emit(ShopRecomUiModel())
    }

    companion object {
        private const val FOLLOW_TYPE_SHOP = 2
        private const val FOLLOW_TYPE_BUYER = 3
    }

}
