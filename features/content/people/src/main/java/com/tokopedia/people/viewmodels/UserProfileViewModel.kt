package com.tokopedia.people.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.people.Resources
import com.tokopedia.people.Success
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.model.ShopRecomItem
import com.tokopedia.people.model.UserPostModel
import com.tokopedia.people.model.UserShopRecomModel
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.Flow

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

    private val shopRecomContent = MutableLiveData<Resources<UserShopRecomModel>>()
    val shopRecomContentLiveData : LiveData<Resources<UserShopRecomModel>> get() = shopRecomContent

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

    private val _savedReminderData = MutableStateFlow<SavedReminderData>(SavedReminderData.NoData)
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

    var isFirstLoad: Boolean = true
    private val _isFirstLoadEmpty = MutableLiveData<Boolean>()
    val isFirstLoadEmpty: LiveData<Boolean> = _isFirstLoadEmpty

    fun submitAction(action: UserProfileAction) {
        when(action) {
            is UserProfileAction.LoadProfile -> handleLoadProfile(action.isRefresh)
            is UserProfileAction.LoadShopRecom -> handleLoadShopRecom()
            is UserProfileAction.LoadPlayVideo -> handleLoadPlayVideo(action.cursor)
            is UserProfileAction.ClickFollowButton -> handleClickFollowButton(action.isFromLogin)
            is UserProfileAction.ClickUpdateReminder -> handleClickUpdateReminder(action.isFromLogin)
            is UserProfileAction.SaveReminderActivityResult -> handleSaveReminderActivityResult(action.channelId, action.position, action.isActive)
            is UserProfileAction.RemoveReminderActivityResult -> handleRemoveReminderActivityResult()
            is UserProfileAction.ClickFollowButtonShopRecom -> handleClickFollowButtonShopRecom(action.data)
        }
    }

    /** Handle Action */
    private fun handleLoadProfile(isRefresh: Boolean) {
        if (isRefresh) isFirstLoad = true
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
    private fun handleLoadShopRecom() {
        launchCatchError(block = {
            val data = repo.getShopRecom()
            if (data != null) shopRecomContent.value = Success(data)
            else throw NullPointerException("data is null")
        }, onError = {
            userPostError.value = it
        })
    }

    private fun handleLoadPlayVideo(cursor: String) {
        launchCatchError(block = {
            val data = repo.getPlayVideo(profileUserID, cursor)
            if (data != null) {
                if (isSelfProfile && isFirstLoad && data.playGetContentSlot.data.isNullOrEmpty()) {
                    _isFirstLoadEmpty.value = true
                    submitAction(UserProfileAction.LoadShopRecom())
                }
                playPostContent.value = Success(data)
                isFirstLoad = false
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

    private fun handleClickUpdateReminder(isFromLogin: Boolean) {
        launchCatchError(block = {
            if(isFromLogin) {
                loadProfileInfo(false)
            }

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

    private fun handleClickFollowButtonShopRecom(data: ShopRecomItem) {
        launchCatchError(block = {

            val followInfo = _followInfo.value

            val result = if (data.isFollow) repo.unFollowProfile(data.encryptedID)
            else repo.followProfile(data.encryptedID)

            when (result) {
                is MutationUiModel.Success -> {
                    _profileInfo.update { repo.getProfile(followInfo.userID) }
                    _uiEvent.emit(
                        UserProfileUiEvent.SuccessFollowShopRecom(
                            data.copy(isFollow = !data.isFollow)
                        )
                    )
                }
                is MutationUiModel.Error -> {
                    _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(result.message))
                }
            }
        }, onError = {
            _uiEvent.emit(UserProfileUiEvent.ErrorFollowUnfollow(""))
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
            _profileWhitelist.update {  repo.getWhitelist() }
        }

        /**
         * play video will be moved to dedicated fragment when
         * developing another tab user profile eventually. so gonna leave as is for now
         * */
        userPost.value = isRefresh
    }

}
