package com.tokopedia.people.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.people.Resources
import com.tokopedia.people.Success
import com.tokopedia.people.di.UserProfileScope
import com.tokopedia.people.domains.*
import com.tokopedia.people.model.*
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

    private val profileFollowers = MutableLiveData<Resources<ProfileFollowerListBase>>()
    val profileFollowersListLiveData: LiveData<Resources<ProfileFollowerListBase>> get() = profileFollowers

    private val profileFollowingsList = MutableLiveData<Resources<ProfileFollowingListBase>>()
    val profileFollowingsListLiveData: LiveData<Resources<ProfileFollowingListBase>> get() = profileFollowingsList

    private val profileDoFollow = MutableLiveData<Resources<ProfileDoFollowModelBase>>()
    val profileDoFollowLiveData: LiveData<Resources<ProfileDoFollowModelBase>> get() = profileDoFollow

    private val profileDoUnFollow = MutableLiveData<Resources<ProfileDoUnFollowModelBase>>()
    val profileDoUnFollowLiveData: LiveData<Resources<ProfileDoUnFollowModelBase>> get() = profileDoUnFollow

    private val followersError = MutableLiveData<Throwable>()
    val followersErrorLiveData: LiveData<Throwable> get() = followersError

    fun getFollowers(
        userName: String,
        cursor: String,
        limit: Int
    ) {
        launchCatchError(block = {
            val data = useCaseFollowersList.getProfileFollowerList(userName, cursor, limit)
            if (data != null) {
                profileFollowers.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
            followersError.value = it
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
                profileFollowingsList.value = Success(data)
            } else throw NullPointerException("data is null")
        }, onError = {
            followersError.value = it
        })
    }

    fun doFollow(followingUserIdEnc: String) {
        launchCatchError(block = {
            val data = useCaseDoFollow.doFollow(followingUserIdEnc)
            if (data != null) {
                profileDoFollow.value = Success(data)
            } else throw NullPointerException("data is null")
        }) {
        }
    }

    fun doUnFollow(unFollowingUserIdEnc: String) {
        launchCatchError(block = {
            val data = useCaseDoUnFollow.doUnfollow(unFollowingUserIdEnc)
            if (data != null) {
                profileDoUnFollow.value = Success(data)
            } else throw NullPointerException("data is null")
        }) {
        }
    }
}
