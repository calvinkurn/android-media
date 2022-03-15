package com.tokopedia.people.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.people.Resources
import com.tokopedia.people.Success
import com.tokopedia.people.di.UserProfileScope
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.people.domains.*
import com.tokopedia.people.model.*
import kotlinx.coroutines.Dispatchers

import java.lang.NullPointerException
import javax.inject.Inject

@UserProfileScope
class UserProfileViewModel @Inject constructor(
    private val userDetailsUseCase: UserDetailsUseCase,
    private var playVodUseCase: PlayPostContentUseCase,
    private val useCaseDoFollow: ProfileFollowUseCase,
    private val useCaseDoUnFollow: ProfileUnfollowedUseCase,
    private val profileIsFollowing: ProfileTheyFollowedUseCase,
    private val videoPostReminderUseCase: VideoPostReminderUseCase
) : BaseViewModel(Dispatchers.Main) {

    public val userDetailsLiveData = MutableLiveData<Resources<ProfileHeaderBase>>()
    public val userPostLiveData = MutableLiveData<Boolean>()
    public val playPostContentLiveData = MutableLiveData<Resources<UserPostModel>>()
    public val profileDoFollowLiveData = MutableLiveData<Resources<ProfileDoFollowModelBase>>()
    public val profileDoUnFollowLiveData = MutableLiveData<Resources<ProfileDoUnFollowModelBase>>()
    public val profileTheyFollowLiveData = MutableLiveData<Resources<UserProfileIsFollow>>()
    public var profileHeaderErrorMessageLiveData = MutableLiveData<Throwable>()
    public var followErrorMessageLiveData = MutableLiveData<Throwable>()
    public var unFollowErrorMessageLiveData = MutableLiveData<Throwable>()
    public var userPostErrorLiveData = MutableLiveData<Throwable>()
    public var postReminderLiveData = MutableLiveData<Resources<VideoPostReimderModel>>()

    public fun getUserDetails(userName: String, isRefreshPost: Boolean = false) {
        launchCatchError(block = {
            val data = userDetailsUseCase.getUserProfileDetail(userName, mutableListOf(userName))
            if (data != null) {
                userDetailsLiveData.value = Success(data.getData(ProfileHeaderBase::class.java))
                userPostLiveData.value = isRefreshPost
            } else throw NullPointerException("data is null")
        }, onError = {
            profileHeaderErrorMessageLiveData.value = it
        })
    }

    public fun getUPlayVideos(group: String, cursor: String, sourceType: String, sourceId: String) {
        launchCatchError(block = {
            val data = playVodUseCase.getPlayPost(group, cursor, sourceType, sourceId)
            if (data != null) {
                playPostContentLiveData.value = Success(data)

            } else throw NullPointerException("data is null")
        }, onError = {
            userPostErrorLiveData.value = it
        })
    }

    fun doFollow(followingUserIdEnc: String) {
        launchCatchError(block = {
            val data = useCaseDoFollow.doFollow(followingUserIdEnc)
            if (data != null) {
                profileDoFollowLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
            followErrorMessageLiveData.value = it
        })
    }

    fun doUnFollow(unFollowingUserIdEnc: String) {
        launchCatchError(block = {
            val data = useCaseDoUnFollow.doUnfollow(unFollowingUserIdEnc)
            if (data != null) {
                profileDoUnFollowLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
            unFollowErrorMessageLiveData.value = it
        })
    }

    fun getFollowingStatus(profileIds: MutableList<String>) {
        launchCatchError(block = {
            val data = profileIsFollowing.profileIsFollowing(profileIds)
            if (data != null) {
                profileTheyFollowLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
        })
    }

    fun updatePostReminderStatus(channelId: String, isActive: Boolean) {
        launchCatchError(block = {
            val data = videoPostReminderUseCase.updateReminder(channelId, isActive)
            if (data != null) {
                postReminderLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
        })
    }
}
