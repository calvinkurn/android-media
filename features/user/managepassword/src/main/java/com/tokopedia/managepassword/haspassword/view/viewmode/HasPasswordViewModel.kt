package com.tokopedia.managepassword.haspassword.view.viewmode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.managepassword.haspassword.domain.data.ProfileDataModel
import com.tokopedia.managepassword.haspassword.domain.usecase.GetProfileCompletionUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class HasPasswordViewModel @Inject constructor(
        private val getProfileCompletionUseCase: GetProfileCompletionUseCase,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _profileData = MutableLiveData<Result<ProfileDataModel>>()
    val profileDataModel: LiveData<Result<ProfileDataModel>>
        get() = _profileData

    fun checkPassword() {
        getProfileCompletionUseCase.getData(onSuccess = {
            _profileData.postValue(Success(it))
        }, onError = {
            _profileData.postValue(Fail(it))
        })
    }
}