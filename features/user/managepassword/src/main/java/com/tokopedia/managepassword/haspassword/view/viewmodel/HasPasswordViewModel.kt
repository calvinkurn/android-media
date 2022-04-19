package com.tokopedia.managepassword.haspassword.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.managepassword.haspassword.domain.data.ProfileDataModel
import com.tokopedia.managepassword.haspassword.domain.usecase.GetProfileCompletionUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class HasPasswordViewModel @Inject constructor(
    private val getProfileCompletionUseCase: GetProfileCompletionUseCase,
    dispatchers: CoroutineDispatchers
) : BaseViewModel(dispatchers.main) {

    private val _profileData = MutableLiveData<Result<ProfileDataModel>>()
    val profileDataModel: LiveData<Result<ProfileDataModel>>
        get() = _profileData

    fun checkPassword() {
        launchCatchError(coroutineContext, {
            val response = getProfileCompletionUseCase(Unit)
            _profileData.value = Success(response)
        }, {
            _profileData.value = Fail(it)
        })
    }
}