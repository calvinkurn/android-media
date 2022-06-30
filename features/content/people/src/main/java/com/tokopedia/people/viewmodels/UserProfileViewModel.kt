package com.tokopedia.people.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.people.Resources
import com.tokopedia.people.Success
import com.tokopedia.people.domains.PlayPostContentUseCase
import com.tokopedia.people.model.UserPostModel
import kotlinx.coroutines.Dispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.views.uimodel.MutationUiModel
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.profile.*
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*

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

    private var userPostError = MutableLiveData<Throwable>()
    val userPostErrorLiveData : LiveData<Throwable> get() = userPostError





    /** Public Getter */
    val isFollowed: Boolean
        get() = _followInfo.value.status

    val isSelfProfile: Boolean
        get() = _profileType.value == ProfileType.Self

    private val _profileInfo = MutableStateFlow(ProfileUiModel.Empty)
    private val _followInfo = MutableStateFlow(FollowInfoUiModel.Empty)
    private val _profileWhitelist = MutableStateFlow(ProfileWhitelistUiModel.Empty)
    private val _profileType = MutableStateFlow(ProfileType.Unknown)

    private val _uiEvent = MutableSharedFlow<UserProfileUiEvent>()

    val uiEvent: Flow<UserProfileUiEvent>
        get() = _uiEvent

    val uiState = combine(
        _profileInfo,
        _followInfo,
        _profileType,
        _profileWhitelist,
    ) { profileInfo, followInfo, profileType, profileWhitelist ->
        UserProfileUiState(
            profileInfo = profileInfo,
            followInfo = followInfo,
            profileType = profileType,
            profileWhitelist = profileWhitelist,
        )
    }

    fun submitAction(action: UserProfileAction) {
        when(action) {
            is UserProfileAction.LoadProfile -> handleLoadProfile(action.isRefresh)
            is UserProfileAction.LoadPlayVideo -> handleLoadPlayVideo(action.cursor)
            is UserProfileAction.ClickFollowButton -> handleClickFollowButton(action.isFromLogin)
            is UserProfileAction.ClickUpdateReminder -> handleClickUpdateReminder(action.channelId, action.isActive)
        }
    }

    /** Handle Action */
    private fun handleLoadProfile(isRefresh: Boolean) {
        launchCatchError(block = {
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
        launchCatchError(block = {
            val data = repo.getPlayVideo(username, cursor)
            if (data != null) {
                playPostContent.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
            userPostError.value = it
        })
    }

    private fun handleClickFollowButton(isFromLogin: Boolean) {
        launchCatchError(block = {
            if(isFromLogin) {
                loadProfileInfo(false)
            }

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

    private fun handleClickUpdateReminder(channelId: String, isActive: Boolean) {
        launchCatchError(block = {
            val result = repo.updateReminder(channelId, isActive)

            _uiEvent.emit(
                when(result) {
                    is MutationUiModel.Success -> UserProfileUiEvent.SuccessUpdateReminder(result.message)
                    is MutationUiModel.Error -> UserProfileUiEvent.ErrorUpdateReminder(Exception(result.message))
                }
            )
        }, onError = {
            _uiEvent.emit(UserProfileUiEvent.ErrorUpdateReminder(it))
        })
    }

    /** Helper */
    private suspend fun loadProfileInfo(isRefresh: Boolean) {
        val deferredProfileInfo = asyncCatchError(block = {
            repo.getProfile(username)
        }) {
            _uiEvent.emit(UserProfileUiEvent.ErrorLoadProfile(it))
            ProfileUiModel.Empty
        }

        val deferredFollowInfo = asyncCatchError(block = {
            repo.getFollowInfo(listOf(username))
        }) {
            FollowInfoUiModel.Empty
        }

        val profileInfo = deferredProfileInfo.await() ?: ProfileUiModel.Empty
        val followInfo = deferredFollowInfo.await() ?: FollowInfoUiModel.Empty
        val profileType = if(userSession.isLoggedIn) {
            if(userSession.userId == followInfo.userID)
                ProfileType.Self
            else ProfileType.OtherUser
        }
        else ProfileType.NotLoggedIn


        _profileInfo.update { profileInfo }
        _followInfo.update { followInfo }
        _profileType.update { profileType }

        if(profileType == ProfileType.Self) {
            _profileWhitelist.update {  repo.getWhitelist(followInfo.userID) }
        }

        /**
         * play video will be moved to dedicated fragment when
         * developing another tab user profile eventually. so gonna leave as is for now
         * */
        userPost.value = isRefresh
    }
}
