package com.tokopedia.createpost.uprofile.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.createpost.uprofile.Resources
import com.tokopedia.createpost.uprofile.Success
import com.tokopedia.createpost.uprofile.di.UserProfileScope
import com.tokopedia.createpost.uprofile.domains.FollowerFollowingListingUseCase
import com.tokopedia.createpost.uprofile.domains.ProfileDoFollowUseCase
import com.tokopedia.createpost.uprofile.domains.UserDetailsUseCase
import com.tokopedia.createpost.uprofile.model.ProfileDoFollowModelBase
import com.tokopedia.createpost.uprofile.model.ProfileFollowerListBase
import com.tokopedia.createpost.uprofile.model.ProfileFollowingListBase
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.Dispatchers

import java.lang.NullPointerException
import javax.inject.Inject

@UserProfileScope
class FollowerFollowingViewModel @Inject constructor(
    private val useCaseFollowersList: FollowerFollowingListingUseCase,
    private val useCaseFollowingList: FollowerFollowingListingUseCase,
    private val useCaseDoFollow: ProfileDoFollowUseCase
) : BaseViewModel(Dispatchers.Main) {

    val profileFollowersListLiveData = MutableLiveData<Resources<ProfileFollowerListBase>>()
    val profileFollowingsListLiveData = MutableLiveData<Resources<ProfileFollowingListBase>>()
    val profileDoFollowLiveData = MutableLiveData<Resources<ProfileDoFollowModelBase>>()

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
        }) {
        }
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
}
