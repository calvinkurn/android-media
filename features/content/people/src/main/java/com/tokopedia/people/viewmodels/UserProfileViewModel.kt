package com.tokopedia.people.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.people.Resources
import com.tokopedia.people.Success
import com.tokopedia.people.di.UserProfileScope
import com.tokopedia.people.domains.PlayPostContentUseCase
import com.tokopedia.people.model.UserPostModel
import kotlinx.coroutines.Dispatchers
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.utils.setValue
import com.tokopedia.people.views.uimodel.MutationUiModel
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.profile.*
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@UserProfileScope
class UserProfileViewModel @Inject constructor(
    private var playVodUseCase: PlayPostContentUseCase,
    private val repo: UserProfileRepository,
    private val userSession: UserSessionInterface,
) : BaseViewModel(Dispatchers.Main) {

    /** Public Getter */
    val isFollow: Boolean
        get() = _followInfo.value.status

    val isSelfProfile: Boolean
        get() = _profileType.value == ProfileType.Self

    private val userPost = MutableLiveData<Boolean>()
    val userPostLiveData : LiveData<Boolean> get() = userPost

    private val playPostContent = MutableLiveData<Resources<UserPostModel>>()
    val playPostContentLiveData : LiveData<Resources<UserPostModel>> get() = playPostContent

    private var followErrorMessage = MutableLiveData<Throwable>()
    val followErrorMessageLiveData : LiveData<Throwable> get() = followErrorMessage

    private var unFollowErrorMessage = MutableLiveData<Throwable>()
    val unFollowErrorMessageLiveData : LiveData<Throwable> get() = unFollowErrorMessage

    private var userPostError = MutableLiveData<Throwable>()
    val userPostErrorLiveData : LiveData<Throwable> get() = userPostError

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
            is UserProfileAction.LoadProfile -> handleLoadProfile(action.username, action.isRefresh)
            UserProfileAction.ClickFollowButton -> handleClickFollowButton()
            is UserProfileAction.ClickUpdateReminder -> handleClickUpdateReminder(action.channelId, action.isActive)
        }
    }

    /** Handle Action */
    private fun handleLoadProfile(username: String, isRefresh: Boolean) {
        launchCatchError(block = {
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


            _profileInfo.value = profileInfo
            _followInfo.value = followInfo
            _profileType.value = profileType

            if(profileType == ProfileType.Self) {
                _profileWhitelist.value = repo.getWhitelist(followInfo.userID)
            }

            /** TODO: refactor - gonna find a better way to trigger load video */
            userPost.value = isRefresh
        }) {
            _uiEvent.emit(UserProfileUiEvent.ErrorLoadProfile(it))
        }
    }

    private fun handleClickFollowButton() {
        launchCatchError(block = {
            val followInfo = _followInfo.value

            val result = if(followInfo.status) repo.unFollowProfile(followInfo.encryptedUserID)
                        else repo.followProfile(followInfo.encryptedUserID)

            when(result) {
                is MutationUiModel.Success -> {
                    _followInfo.setValue { copy(status = !followInfo.status) }
                    _profileInfo.value = repo.getProfile(followInfo.userID)
                }
                is MutationUiModel.Error -> unFollowErrorMessage.value = Exception(result.message)
            }
        }) {
            unFollowErrorMessage.value = it
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

    /** TODO: refactor - gonna move to repo */
    fun getUPlayVideos(group: String, cursor: String, sourceType: String, sourceId: String) {
        launchCatchError(block = {
            val data = playVodUseCase.getPlayPost(group, cursor, sourceType, sourceId)
            if (data != null) {
                playPostContent.value = Success(data)

            } else throw NullPointerException("data is null")
        }, onError = {
            userPostError.value = it
        })
    }
}
