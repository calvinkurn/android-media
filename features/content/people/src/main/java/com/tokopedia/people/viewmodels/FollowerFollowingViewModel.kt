package com.tokopedia.people.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.people.Resources
import com.tokopedia.people.Success
import com.tokopedia.people.data.UserFollowRepository
import com.tokopedia.people.di.UserProfileScope
import com.tokopedia.people.views.uimodel.profile.ProfileUiModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@UserProfileScope
class FollowerFollowingViewModel @Inject constructor(
    private val repo: UserFollowRepository,
) : BaseViewModel(Dispatchers.Main) {

    private val profileFollowers = MutableLiveData<Resources<List<ProfileUiModel.PeopleUiModel>>>()
    val profileFollowersListLiveData: LiveData<Resources<List<ProfileUiModel.PeopleUiModel>>>
        get() = profileFollowers

    private val profileFollowingsList = MutableLiveData<Resources<List<ProfileUiModel.PeopleUiModel>>>()
    val profileFollowingsListLiveData: LiveData<Resources<List<ProfileUiModel.PeopleUiModel>>> get() = profileFollowingsList

    private val _followResult = MutableLiveData<MutationUiModel>()
    val followResult: LiveData<MutationUiModel> get() = _followResult

    private val followersError = MutableLiveData<Throwable>()
    val followersErrorLiveData: LiveData<Throwable> get() = followersError

    var username: String = ""

    fun getFollowers(
        cursor: String,
        limit: Int,
    ) {
        launchCatchError(block = {

            var profileList: List<ProfileUiModel.PeopleUiModel>
            var currentCursor: String = cursor
            var result: Pair<List<ProfileUiModel.PeopleUiModel>, String>

            do {
                result = repo.getMyFollowers(username, currentCursor, limit)

                profileList = result.first
                currentCursor = result.second

            } while (profileList.isEmpty() && currentCursor.isNotEmpty())

            profileFollowers.value = Success(result.first, result.second)
        }, onError = {
                followersError.value = it
            },)
    }

    fun getFollowings(
        cursor: String,
        limit: Int,
    ) {
        launchCatchError(block = {
            var profileList: List<ProfileUiModel.PeopleUiModel>
            var currentCursor: String = cursor
            var result: Pair<List<ProfileUiModel.PeopleUiModel>, String>

            do {
                result = repo.getMyFollowing(username, currentCursor, limit)

                profileList = result.first
                currentCursor = result.second

            } while (profileList.isEmpty() && currentCursor.isNotEmpty())

            profileFollowingsList.value = Success(result.first, result.second)
        }, onError = {
                followersError.value = it
            },)
    }

    fun followUser(id: String, isFollowed: Boolean) {
        viewModelScope.launchCatchError(block = {
            val result = repo.followUser(id, !isFollowed)
            _followResult.value = result
        }) {
            _followResult.value = MutationUiModel.Error("")
        }
    }
}
