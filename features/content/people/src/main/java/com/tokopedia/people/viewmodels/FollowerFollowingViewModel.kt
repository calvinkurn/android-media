package com.tokopedia.people.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.people.Resources
import com.tokopedia.people.Success
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.di.UserProfileScope
import com.tokopedia.people.model.*
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@UserProfileScope
class FollowerFollowingViewModel @Inject constructor(
    private val repo: UserProfileRepository,
) : BaseViewModel(Dispatchers.Main) {

    private val profileFollowers = MutableLiveData<Resources<ProfileFollowerListBase>>()
    val profileFollowersListLiveData: LiveData<Resources<ProfileFollowerListBase>> get() = profileFollowers

    private val profileFollowingsList = MutableLiveData<Resources<ProfileFollowingListBase>>()
    val profileFollowingsListLiveData: LiveData<Resources<ProfileFollowingListBase>> get() = profileFollowingsList

    private val profileDoFollow = MutableLiveData<MutationUiModel>()
    val profileDoFollowLiveData: LiveData<MutationUiModel> get() = profileDoFollow

    private val profileDoUnFollow = MutableLiveData<MutationUiModel>()
    val profileDoUnFollowLiveData: LiveData<MutationUiModel> get() = profileDoUnFollow

    private val followersError = MutableLiveData<Throwable>()
    val followersErrorLiveData: LiveData<Throwable> get() = followersError

    var username: String = ""

    fun getFollowers(
        cursor: String,
        limit: Int,
    ) {
        launchCatchError(block = {

            var profileList: List<ProfileFollowerV2>
            var currentCursor: String = cursor
            var result: ProfileFollowerListBase

            do {
                result = repo.getFollowerList(username, currentCursor, limit)

                profileList = result.profileFollowers.profileFollower
                currentCursor = result.profileFollowers.newCursor

            } while (profileList.isEmpty() && currentCursor.isNotEmpty())

            profileFollowers.value = Success(result)
        }, onError = {
                followersError.value = it
            },)
    }

    fun getFollowings(
        cursor: String,
        limit: Int,
    ) {
        launchCatchError(block = {

            var profileList: List<ProfileFollowerV2>
            var currentCursor: String = cursor
            var result: ProfileFollowingListBase

            do {
                result = repo.getFollowingList(username, currentCursor, limit)

                profileList = result.profileFollowings.profileFollower
                currentCursor = result.profileFollowings.newCursor

            } while (profileList.isEmpty() && currentCursor.isNotEmpty())

            profileFollowingsList.value = Success(result)
        }, onError = {
                followersError.value = it
            },)
    }

    fun doFollow(followingUserIdEnc: String) {
        launchCatchError(block = {
            val result = repo.followProfile(followingUserIdEnc)

            profileDoFollow.value = result
        },) {
            profileDoFollow.value = MutationUiModel.Error("")
        }
    }

    fun doUnFollow(unFollowingUserIdEnc: String) {
        launchCatchError(block = {
            val result = repo.unFollowProfile(unFollowingUserIdEnc)

            profileDoUnFollow.value = result
        },) {
            profileDoUnFollow.value = MutationUiModel.Error("")
        }
    }
}
