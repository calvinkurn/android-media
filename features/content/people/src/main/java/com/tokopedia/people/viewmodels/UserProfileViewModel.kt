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
import com.tokopedia.people.domains.repository.UserProfileRepository
import com.tokopedia.people.views.uimodel.profile.*
import com.tokopedia.people.views.uimodel.state.UserProfileUiState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@UserProfileScope
class UserProfileViewModel @Inject constructor(
    private val userDetailsUseCase: UserDetailsUseCase,
    private var playVodUseCase: PlayPostContentUseCase,
    private val useCaseDoFollow: ProfileFollowUseCase,
    private val useCaseDoUnFollow: ProfileUnfollowedUseCase,
    private val profileIsFollowing: ProfileTheyFollowedUseCase,
    private val videoPostReminderUseCase: VideoPostReminderUseCase,
    private val getWhitelistUseCase: GetWhitelistUseCase,
    private val repo: UserProfileRepository,
) : BaseViewModel(Dispatchers.Main) {

    private val userDetails = MutableLiveData<Resources<ProfileHeaderBase>>()
    val userDetailsLiveData : LiveData<Resources<ProfileHeaderBase>> get() = userDetails

    private val userPost = MutableLiveData<Boolean>()
    val userPostLiveData : LiveData<Boolean> get() = userPost

    private val playPostContent = MutableLiveData<Resources<UserPostModel>>()
    val playPostContentLiveData : LiveData<Resources<UserPostModel>> get() = playPostContent

    private val profileDoFollow = MutableLiveData<Resources<ProfileDoFollowModelBase>>()
    val profileDoFollowLiveData : LiveData<Resources<ProfileDoFollowModelBase>> get() = profileDoFollow

    private val profileDoUnFollow = MutableLiveData<Resources<ProfileDoUnFollowModelBase>>()
    val profileDoUnFollowLiveData : LiveData<Resources<ProfileDoUnFollowModelBase>> get() = profileDoUnFollow

    private val profileTheyFollow = MutableLiveData<Resources<UserProfileIsFollow>>()
    val profileTheyFollowLiveData : LiveData<Resources<UserProfileIsFollow>> get() = profileTheyFollow

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

//    fun getUserDetails(username: String, isRefresh: Boolean) {
//        launchCatchError(block = {
//            val profileInfo = async {
//                val result = userDetailsUseCase.getUserProfileDetail(username)
//                result.getData(ProfileHeaderBase::class.java)
//            }
//        }) {
//            profileHeaderErrorMessage.value = it
//        }
//    }

    fun getUserDetails(userName: String, isRefreshPost: Boolean = false) {
        launchCatchError(block = {
            val data = userDetailsUseCase.getUserProfileDetail(userName)
            if (data != null) {
                userDetails.value = Success(data.getData(ProfileHeaderBase::class.java))
                userPost.value = isRefreshPost
            } else throw NullPointerException("data is null")
        }, onError = {
            profileHeaderErrorMessage.value = it
        })
    }

    public fun getUPlayVideos(group: String, cursor: String, sourceType: String, sourceId: String) {
        launchCatchError(block = {
            val data = playVodUseCase.getPlayPost(group, cursor, sourceType, sourceId)
            if (data != null) {
                playPostContent.value = Success(data)

            } else throw NullPointerException("data is null")
        }, onError = {
            userPostError.value = it
        })
    }

    fun doFollow(followingUserIdEnc: String) {
        launchCatchError(block = {
            val data = useCaseDoFollow.doFollow(followingUserIdEnc)
            if (data != null) {
                profileDoFollow.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
            followErrorMessage.value = it
        })
    }

    fun doUnFollow(unFollowingUserIdEnc: String) {
        launchCatchError(block = {
            val data = useCaseDoUnFollow.doUnfollow(unFollowingUserIdEnc)
            if (data != null) {
                profileDoUnFollow.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
            unFollowErrorMessage.value = it
        })
    }

    fun getFollowingStatus(profileIds: MutableList<String>) {
        launchCatchError(block = {
            val data = profileIsFollowing.profileIsFollowing(profileIds)
            if (data != null) {
                profileTheyFollow.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
        })
    }

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
