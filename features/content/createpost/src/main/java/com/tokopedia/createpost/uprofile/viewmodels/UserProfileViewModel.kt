package com.tokopedia.createpost.uprofile.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.createpost.uprofile.Resources
import com.tokopedia.createpost.uprofile.Success
import com.tokopedia.createpost.uprofile.di.UserProfileScope
import com.tokopedia.createpost.uprofile.domains.PlayPostContentUseCase
import com.tokopedia.createpost.uprofile.domains.ProfileDoFollowUseCase
import com.tokopedia.createpost.uprofile.domains.ProfileTheyFollowedUseCase
import com.tokopedia.createpost.uprofile.domains.UserDetailsUseCase
import com.tokopedia.createpost.uprofile.model.ProfileDoFollowModelBase
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.createpost.uprofile.model.ProfileIsFollowing
import com.tokopedia.createpost.uprofile.model.UserPostModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.Dispatchers

import java.lang.NullPointerException
import javax.inject.Inject

@UserProfileScope
class UserProfileViewModel @Inject constructor(
    private val userDetailsUseCase: UserDetailsUseCase,
    private var playVodUseCase: PlayPostContentUseCase,
    private val useCaseDoFollow: ProfileDoFollowUseCase,
    private val theyFollowedUseCase: ProfileTheyFollowedUseCase
) : BaseViewModel(Dispatchers.Main) {

    public val userDetailsLiveData = MutableLiveData<Resources<ProfileHeaderBase>>()
    public val playPostContentLiveData = MutableLiveData<Resources<UserPostModel>>()
    public val profileDoFollowLiveData = MutableLiveData<Resources<ProfileDoFollowModelBase>>()
    public val profileTheyFollowLiveData = MutableLiveData<Resources<ProfileIsFollowing>>()

    public fun getUserDetails(userName: String) {
        launchCatchError(block = {
            val data = userDetailsUseCase.getUserProfileDetail(userName, mutableListOf())
            if (data != null) {
                userDetailsLiveData.value = Success(data.getData(ProfileHeaderBase::class.java))
                profileTheyFollowLiveData.value = Success(data.getData(ProfileIsFollowing::class.java))
            } else throw NullPointerException("data is null")
        }) {
        }
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

    fun doFollow(followingUserId: String, followStatus: Boolean) {
        launchCatchError(block = {
            val data = useCaseDoFollow.doFollow(followingUserId, followStatus)
            if (data != null) {
                profileDoFollowLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }) {
        }
    }

    fun theyFollowed(profileIds: MutableList<String>) {
        launchCatchError(block = {
            val data = theyFollowedUseCase.profileTheyFollowed(profileIds)
            if (data != null) {
                profileTheyFollowLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }) {
        }
    }
}
