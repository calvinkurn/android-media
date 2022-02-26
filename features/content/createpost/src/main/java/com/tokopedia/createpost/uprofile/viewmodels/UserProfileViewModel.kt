package com.tokopedia.createpost.uprofile.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.createpost.uprofile.Resources
import com.tokopedia.createpost.uprofile.Success
import com.tokopedia.createpost.uprofile.di.UserProfileScope
import com.tokopedia.createpost.uprofile.domains.*
import com.tokopedia.createpost.uprofile.model.*
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.Dispatchers

import java.lang.NullPointerException
import javax.inject.Inject

@UserProfileScope
class UserProfileViewModel @Inject constructor(
    private val userDetailsUseCase: UserDetailsUseCase,
    private var playVodUseCase: PlayPostContentUseCase,
    private val useCaseDoFollow: ProfileFollowUseCase,
    private val useCaseDoUnFollow: ProfileUnfollowedUseCase,
    private val profileIsFollowing: ProfileTheyFollowedUseCase
) : BaseViewModel(Dispatchers.Main) {

    public val userDetailsLiveData = MutableLiveData<Resources<ProfileHeaderBase>>()
    public val playPostContentLiveData = MutableLiveData<Resources<UserPostModel>>()
    public val profileDoFollowLiveData = MutableLiveData<Resources<ProfileDoFollowModelBase>>()
    public val profileDoUnFollowLiveData = MutableLiveData<Resources<ProfileDoUnFollowModelBase>>()
    public val profileTheyFollowLiveData = MutableLiveData<Resources<UserProfileIsFollow>>()
    public var errorMessageLiveData = MutableLiveData<Throwable>()

    public fun getUserDetails(userName: String) {
        launchCatchError(block = {
            val data = userDetailsUseCase.getUserProfileDetail(userName, mutableListOf(userName))
            if (data != null) {
                userDetailsLiveData.value = Success(data.getData(ProfileHeaderBase::class.java))
                //profileTheyFollowLiveData.value = Success(data.getData(UserProfileIsFollow::class.java))
            } else throw NullPointerException("data is null")
        }, onError = {
            errorMessageLiveData.value = it
        })
    }


    public fun getUPlayVideos(group: String, cursor: String, sourceType: String, sourceId: String) {
        launchCatchError(block = {
            val data = playVodUseCase.getPlayPost(group, cursor, sourceType, sourceId)
            if (data != null) {
                playPostContentLiveData.value = Success(data)

            } else throw NullPointerException("data is null")
        }) {
        }
    }

    fun doFollow(followingUserIdEnc: String) {
        launchCatchError(block = {
            val data = useCaseDoFollow.doFollow(followingUserIdEnc)
            if (data != null) {
                profileDoFollowLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }) {
        }
    }

    fun doUnFollow(unFollowingUserIdEnc: String) {
        launchCatchError(block = {
            val data = useCaseDoUnFollow.doUnfollow(unFollowingUserIdEnc)
            if (data != null) {
                profileDoUnFollowLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }) {
        }
    }

    fun getFollowingStatus(profileIds: MutableList<String>) {
        launchCatchError(block = {
            val data = profileIsFollowing.profileIsFollowing(profileIds)
            if (data != null) {
                profileTheyFollowLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }) {
        }
    }
}
