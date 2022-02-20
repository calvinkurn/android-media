package com.tokopedia.createpost.uprofile.viewmodels

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.createpost.uprofile.Resources
import com.tokopedia.createpost.uprofile.Success
import com.tokopedia.createpost.uprofile.di.UserProfileScope
import com.tokopedia.createpost.uprofile.domains.UserDetailsUseCase
import com.tokopedia.createpost.uprofile.model.ProfileHeaderBase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import kotlinx.coroutines.Dispatchers

import java.lang.NullPointerException
import javax.inject.Inject

@UserProfileScope
class UserProfileViewModel @Inject constructor(private val userDetailsUseCase: UserDetailsUseCase
) : BaseViewModel(Dispatchers.Main) {

    public val userDetailsLiveData = MutableLiveData<Resources<ProfileHeaderBase>>()

    public fun getUserDetails(userName: String) {
        launchCatchError(block = {
           val data = userDetailsUseCase.getUserProfileDetail(userName)
            if (data != null) {
                userDetailsLiveData.value = Success(data)
            } else throw NullPointerException("data is null")
        }) {
        }
    }
}
