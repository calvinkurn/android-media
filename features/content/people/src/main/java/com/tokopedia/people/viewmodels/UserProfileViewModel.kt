package com.tokopedia.people.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.people.Resources
import com.tokopedia.people.Success
import com.tokopedia.people.di.UserProfileScope
import com.tokopedia.people.domains.PlayPostContentUseCase
import com.tokopedia.people.domains.ProfileFollowUseCase
import com.tokopedia.people.domains.ProfileTheyFollowedUseCase
import com.tokopedia.people.domains.ProfileUnfollowedUseCase
import com.tokopedia.people.domains.UserDetailsUseCase
import com.tokopedia.people.domains.VideoPostReminderUseCase
import com.tokopedia.people.model.ProfileDoFollowModelBase
import com.tokopedia.people.model.ProfileDoUnFollowModelBase
import com.tokopedia.people.model.ProfileHeaderBase
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.model.UserProfileIsFollow
import com.tokopedia.people.model.VideoPostReimderModel
import kotlinx.coroutines.Dispatchers
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase
import com.tokopedia.feedcomponent.domain.usecase.GetWhitelistUseCase.Companion.WHITELIST_ENTRY_POINT
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.utils.setValue
import com.tokopedia.people.views.uimodel.action.UserProfileAction
import com.tokopedia.people.views.uimodel.event.UserProfileUiEvent
import com.tokopedia.people.views.uimodel.profile.*
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@UserProfileScope
class UserProfileViewModel @Inject constructor(
    private var playVodUseCase: PlayPostContentUseCase,
    private val useCaseDoFollow: ProfileFollowUseCase,
    private val useCaseDoUnFollow: ProfileUnfollowedUseCase,
    private val videoPostReminderUseCase: VideoPostReminderUseCase,
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

    private val profileDoFollow = MutableLiveData<Resources<ProfileDoFollowModelBase>>()
    val profileDoFollowLiveData : LiveData<Resources<ProfileDoFollowModelBase>> get() = profileDoFollow

    private val profileDoUnFollow = MutableLiveData<Resources<ProfileDoUnFollowModelBase>>()
    val profileDoUnFollowLiveData : LiveData<Resources<ProfileDoUnFollowModelBase>> get() = profileDoUnFollow

    private var profileHeaderErrorMessage = MutableLiveData<Throwable>()
    val profileHeaderErrorMessageLiveData : LiveData<Throwable> get() = profileHeaderErrorMessage

    private var followErrorMessage = MutableLiveData<Throwable>()
    val followErrorMessageLiveData : LiveData<Throwable> get() = followErrorMessage

    private var unFollowErrorMessage = MutableLiveData<Throwable>()
    val unFollowErrorMessageLiveData : LiveData<Throwable> get() = unFollowErrorMessage

    private var userPostError = MutableLiveData<Throwable>()
    val userPostErrorLiveData : LiveData<Throwable> get() = userPostError

    private var postReminder = MutableLiveData<Resources<VideoPostReimderModel>>()
    val postReminderLiveData : LiveData<Resources<VideoPostReimderModel>> get() = postReminder

    private var postReminderErrorMessage = MutableLiveData<Throwable>()
    val postReminderErrorMessageLiveData : LiveData<Throwable> get() = postReminderErrorMessage

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
            UserProfileAction.ClickFollowButton -> handleClickFollowButton()
        }
    }

    /** Handle Action */
    private fun handleClickFollowButton() {
        launchCatchError(block = {
            val followInfo = _followInfo.value

            if(followInfo.status) {
                /** TODO: refactor - gonna move to repo */
                val data = useCaseDoUnFollow.doUnfollow(followInfo.encryptedUserID)
                if (data != null) {
                    _followInfo.setValue { copy(status = !followInfo.status) }
                } else throw NullPointerException("data is null")
            }
            else {
                val data = useCaseDoFollow.doFollow(followInfo.encryptedUserID)
                if (data != null) {
                    _followInfo.setValue { copy(status = !followInfo.status) }
                } else throw NullPointerException("data is null")
            }

            _profileInfo.value = repo.getProfile(followInfo.userID)
        }) {
            unFollowErrorMessage.value = it
        }
    }

    fun getUserDetails(username: String, isRefresh: Boolean) {
        launchCatchError(block = {
            val profileInfo = asyncCatchError(block = {
                repo.getProfile(username)
            }) {
                profileHeaderErrorMessage.value = it
                ProfileUiModel.Empty
            }

            val followInfo = asyncCatchError(block = {
                repo.getFollowInfo(listOf(username))
            }) {
                FollowInfoUiModel.Empty
            }

            _profileInfo.value = profileInfo.await() ?: ProfileUiModel.Empty
            _followInfo.value = followInfo.await() ?: FollowInfoUiModel.Empty
            _profileType.value = if(userSession.isLoggedIn) {
                if(userSession.userId == _followInfo.value.userID)
                    ProfileType.Self
                else ProfileType.OtherUser
            }
            else ProfileType.NotLoggedIn

            /** TODO: refactor - gonna find a better way to trigger load video */
            userPost.value = isRefresh
        }) {
            profileHeaderErrorMessage.value = it
        }
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

    /** TODO: refactor - gonna move to repo */
    fun updatePostReminderStatus(channelId: String, isActive: Boolean) {
        launchCatchError(block = {
            val data = videoPostReminderUseCase.updateReminder(channelId, isActive)
            if (data != null) {
                postReminder.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
            postReminderErrorMessage.value = it
        })
    }
}
