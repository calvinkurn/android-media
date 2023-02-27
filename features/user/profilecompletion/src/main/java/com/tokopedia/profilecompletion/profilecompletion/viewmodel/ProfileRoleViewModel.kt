package com.tokopedia.profilecompletion.profilecompletion.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.profilecompletion.profileinfo.data.ProfileRoleData
import com.tokopedia.profilecompletion.profileinfo.usecase.ProfileRoleUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-08-01.
 * ade.hadian@tokopedia.com
 */

class ProfileRoleViewModel @Inject constructor(
    private val userProfileRoleUseCase: ProfileRoleUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val mutableUserProfileRole = MutableLiveData<Result<ProfileRoleData>>()
    val userProfileRole: LiveData<Result<ProfileRoleData>>
	get() = mutableUserProfileRole

    fun getUserProfileRole() {
        launchCatchError(block = {
            val response = userProfileRoleUseCase(Unit)

            mutableUserProfileRole.value = Success(response.profileRole)
        }, onError = {
            mutableUserProfileRole.value = Fail(it)
        })
    }
}
