package com.tokopedia.profilecompletion.changebiousername.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.profilecompletion.changebiousername.data.SubmitBioUsername
import com.tokopedia.profilecompletion.changebiousername.data.SubmitProfileParam
import com.tokopedia.profilecompletion.changebiousername.data.UsernameValidation
import com.tokopedia.profilecompletion.changebiousername.domain.usecase.SubmitBioUsernameUseCase
import com.tokopedia.profilecompletion.changebiousername.domain.usecase.ValidateUsernameUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

class ChangeBioUsernameViewModel @Inject constructor(
    private val validateUseCase: ValidateUsernameUseCase,
    private val submitProfileUseCase: SubmitBioUsernameUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _resultValidationUsername = MutableLiveData<Result<UsernameValidation>>()

    val resultValidationUsername: LiveData<Result<UsernameValidation>>
        get() = _resultValidationUsername

    private val _resultSubmitUsername = MutableLiveData<Result<SubmitBioUsername>>()

    val resultSubmitUsername: LiveData<Result<SubmitBioUsername>>
        get() = _resultSubmitUsername

    val _resultSubmitBio = MutableLiveData<Result<SubmitBioUsername>>()

    val resultSubmitBio: LiveData<Result<SubmitBioUsername>>
        get() = _resultSubmitBio

    var validationUsernameJob: Job? = null

    val _loadingState = MutableLiveData<Boolean>()

    val loadingState: LiveData<Boolean>
        get() = _loadingState

    fun validateUsername(username: String) {
        if (username.length < 4) {
            return
        }
        if (validationUsernameJob != null && validationUsernameJob?.isActive == true) {
            validationUsernameJob?.cancel()
        }
        validationUsernameJob = launchCatchError(block = {
            delay(2000)
            _loadingState.value = true
            val result = validateUseCase(username)
            _loadingState.value = false
            _resultValidationUsername.value = Success(result.response)
        }, onError = {
            _resultValidationUsername.value = Fail(it)
        })
    }

    fun submitUsername(username: String) {
        launchCatchError(block = {
            val isValidUsername = validateUseCase(username).response.isValid
            if (isValidUsername) {
                val result = submitProfileUseCase(
                    SubmitProfileParam(
                        username = username,
                        isUpdateUsername = true
                    )
                )
                val status = result.response.status
                if (status) {
                    _resultSubmitUsername.value = Success(result.response)
                }
            }
        }, onError = {
            _resultSubmitUsername.value = Fail(it)
        })
    }

    fun submitBio(bio: String) {
        launchCatchError(block = {
            val result =
                submitProfileUseCase(SubmitProfileParam(bio = bio, isUpdateBioGraphy = true))
            _resultSubmitBio.value = Success(result.response)
        }, onError = {
            _resultSubmitBio.value = Fail(it)
        })

    }
}