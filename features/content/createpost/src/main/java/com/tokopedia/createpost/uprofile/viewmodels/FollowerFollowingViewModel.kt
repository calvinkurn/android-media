package com.tokopedia.createpost.uprofile.viewmodels

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
class FollowerFollowingViewModel @Inject constructor(
    private val useCaseFollowersList: FollowerFollowingListingUseCase,
    private val useCaseFollowingList: FollowerFollowingListingUseCase,
    private val useCaseDoFollow: ProfileFollowUseCase,
    private val useCaseDoUnFollow: ProfileUnfollowedUseCase,
) : BaseViewModel(Dispatchers.Main) {

    val profileFollowersListLiveData = MutableLiveData<Resources<ProfileFollowerListBase>>()
    val profileFollowingsListLiveData = MutableLiveData<Resources<ProfileFollowingListBase>>()
    val profileDoFollowLiveData = MutableLiveData<Resources<ProfileDoFollowModelBase>>()
    val profileDoUnFollowLiveData = MutableLiveData<Resources<ProfileDoUnFollowModelBase>>()
    val followersErrorLiveData = MutableLiveData<Throwable>()

    fun getFollowers(
        userName: String,
        cursor: String,
        limit: Int
    ) {
        launchCatchError(block = {
            val data = useCaseFollowersList.getProfileFollowerList(userName, cursor, limit)
            if (data != null) {
                profileFollowersListLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
            followersErrorLiveData.value = it
        })
    }

    fun getFollowings(
        userName: String,
        cursor: String,
        limit: Int
    ) {
        launchCatchError(block = {
            val data = useCaseFollowingList.getProfileFollowingList(userName, cursor, limit)
            if (data != null) {
                profileFollowingsListLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
            followersErrorLiveData.value = it
        })
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
}
