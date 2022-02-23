package com.tokopedia.createpost.uprofile.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.createpost.uprofile.Resources
import com.tokopedia.createpost.uprofile.Success
import com.tokopedia.createpost.uprofile.di.UserProfileScope
import com.tokopedia.createpost.uprofile.domains.PlayPostContentUseCase
import com.tokopedia.createpost.uprofile.domains.UserDetailsUseCase
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.createpost.uprofile.model.UserPostModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.Dispatchers

import java.lang.NullPointerException
import javax.inject.Inject

@UserProfileScope
class UserProfileViewModel @Inject constructor(private val userDetailsUseCase: UserDetailsUseCase, private var playVodUseCase: PlayPostContentUseCase
) : BaseViewModel(Dispatchers.Main) {

    public val userDetailsLiveData = MutableLiveData<Resources<ProfileHeaderBase>>()
    public val playPostContentLiveData = MutableLiveData<Resources<UserPostModel>>()

    public fun getUserDetails(userName: String) {
        launchCatchError(block = {
           val data = userDetailsUseCase.getUserProfileDetail(userName)
            if (data != null) {
                userDetailsLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }) {
        }
    }

    public fun getUPlayVideos(group: String, cursor: String, sourceType: String, sourceId: String) {
        launchCatchError(block = {
           val data = playVodUseCase.getPlayPost(group,cursor,sourceType,sourceId)
            if (data != null) {
                playPostContentLiveData.value = Success(data)

            } else throw NullPointerException("data is null")
        }) {
        }
    }
}
