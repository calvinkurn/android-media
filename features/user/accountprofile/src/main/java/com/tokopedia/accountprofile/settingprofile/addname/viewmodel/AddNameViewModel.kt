package com.tokopedia.accountprofile.settingprofile.addname.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.sessioncommon.domain.usecase.RegisterUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by nisie on 23/04/19.
 * Don't forget to remove basic token after success / fail
 */
class AddNameViewModel @Inject constructor(private val registerUseCase: RegisterUseCase, private val dispatchers: CoroutineDispatchers) :
    BaseViewModel(dispatchers.main) {

    private val _registerValue = MutableLiveData<Result<RegisterInfo>>()
    val registerLiveData: LiveData<Result<RegisterInfo>>
        get() = _registerValue

    fun registerPhoneNumberAndName(name: String, phoneNumber: String, token: String) {
        viewModelScope.launch(dispatchers.main) {
            try {
                val param = RegisterUseCase.generateParamRegisterPhone(name, phoneNumber, token)
                val data = registerUseCase(param)
                val registerInfo = data.register

                if (registerInfo.errors.isEmpty()) {
                    _registerValue.value = Success(registerInfo)
                } else {
                    _registerValue.value = Fail(MessageErrorException(registerInfo.errors[0].message))
                }
            } catch (e: Exception) {
                _registerValue.value = Fail(e)
            }
        }
    }
}
