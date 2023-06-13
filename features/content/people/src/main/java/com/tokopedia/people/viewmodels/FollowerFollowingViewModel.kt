package com.tokopedia.people.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.feedcomponent.domain.usecase.shopfollow.ShopFollowAction
import com.tokopedia.feedcomponent.people.model.MutationUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.people.Resources
import com.tokopedia.people.Success
import com.tokopedia.people.data.UserFollowRepository
import com.tokopedia.people.di.UserProfileScope
import com.tokopedia.people.views.uimodel.FollowListUiModel
import com.tokopedia.people.views.uimodel.FollowResultUiModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@UserProfileScope
class FollowerFollowingViewModel @Inject constructor(
    private val repo: UserFollowRepository
) : BaseViewModel(Dispatchers.Main) {

    private val profileFollowers = MutableLiveData<Resources<FollowListUiModel.Follower>>()
    val profileFollowersListLiveData: LiveData<Resources<FollowListUiModel.Follower>>
        get() = profileFollowers

    private val profileFollowingsList = MutableLiveData<Resources<FollowListUiModel.Following>>()
    val profileFollowingsListLiveData: LiveData<Resources<FollowListUiModel.Following>>
        get() = profileFollowingsList

    private val _followResult = MutableLiveData<FollowResultUiModel>()
    val followResult: LiveData<FollowResultUiModel> get() = _followResult

    private val followersError = MutableLiveData<Throwable>()
    val followersErrorLiveData: LiveData<Throwable> get() = followersError

    var username: String = ""

    val followCount: LiveData<FollowListUiModel.FollowCount>
        get() = _followCount
    private val _followCount = MutableLiveData<FollowListUiModel.FollowCount>()

    fun getFollowers(
        cursor: String,
        limit: Int
    ) {
        launchCatchError(block = {
            var result = FollowListUiModel.emptyFollowers.copy(nextCursor = cursor)

            do {
                result = repo.getMyFollowers(username, result.nextCursor, limit)
            } while (result.followers.isEmpty() && result.nextCursor.isNotEmpty())

            profileFollowers.value = Success(result)
            _followCount.value = result.total
        }, onError = {
                followersError.value = it
            })
    }

    fun getFollowings(
        cursor: String,
        limit: Int
    ) {
        launchCatchError(block = {
            var result = FollowListUiModel.emptyFollowingList.copy(nextCursor = cursor)

            do {
                result = repo.getMyFollowing(username, result.nextCursor, limit)
            } while (result.followingList.isEmpty() && result.nextCursor.isNotEmpty())

            profileFollowingsList.value = Success(result)
            _followCount.value = result.total
        }, onError = {
                followersError.value = it
            })
    }

    fun followUser(id: String, isFollowed: Boolean, position: Int) {
        viewModelScope.launchCatchError(block = {
            val result = repo.followUser(id, !isFollowed)
            _followResult.value = when (result) {
                is MutationUiModel.Success -> FollowResultUiModel.Success(result.message)
                is MutationUiModel.Error -> FollowResultUiModel.Fail(
                    result.message,
                    isFollowed,
                    position
                )
            }
        }) {
            _followResult.value = FollowResultUiModel.Fail(
                it.localizedMessage,
                isFollowed,
                position
            )
        }
    }

    fun followShop(id: String, isFollowed: Boolean, position: Int) {
        viewModelScope.launchCatchError(block = {
            val result = repo.followShop(id, ShopFollowAction.getActionByState(isFollowed))
            _followResult.value = when (result) {
                is MutationUiModel.Success -> FollowResultUiModel.Success(result.message)
                is MutationUiModel.Error -> FollowResultUiModel.Fail(
                    result.message,
                    isFollowed,
                    position
                )
            }
        }) {
            _followResult.value = FollowResultUiModel.Fail(
                it.localizedMessage,
                isFollowed,
                position
            )
        }
    }
}
