package com.tokopedia.privacycenter.dsar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.dsar.domain.DsarAddEmailUseCase
import com.tokopedia.privacycenter.dsar.domain.DsarCheckEmailUseCase
import com.tokopedia.privacycenter.dsar.model.AddEmailParam
import com.tokopedia.privacycenter.dsar.model.uimodel.AddEmailModel
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class DsarAddEmailViewModel @Inject constructor(
    private val dsarCheckEmailUseCase: DsarCheckEmailUseCase,
    private val dsarAddEmailUseCase: DsarAddEmailUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _addEmailModel = MutableLiveData(AddEmailModel())
    val addEmailModel: LiveData<AddEmailModel> = _addEmailModel

    private val _routeToSuccessPage = SingleLiveEvent<Void>()
    val routeToSuccessPage: LiveData<Void> = _routeToSuccessPage

    private val _routeToVerification = SingleLiveEvent<String>()
    val routeToVerification: LiveData<String> = _routeToVerification

    private val _toasterError = SingleLiveEvent<String>()
    val toasterError: LiveData<String> = _toasterError

    fun checkEmail(email: String) {
        _addEmailModel.value = _addEmailModel.value?.copy(btnLoading = true)
        launch {
            try {
                val result = dsarCheckEmailUseCase(email).data
                _addEmailModel.value = _addEmailModel.value?.copy(inputText = email, inputError = result.errorMessage, btnLoading = false)
                if(result.isValid) {
                    _routeToVerification.value = email
                }
            } catch (e: Exception) {
                _addEmailModel.value = _addEmailModel.value?.copy(inputText = "", inputError = e.message ?: "", btnLoading = false)
            }
        }
    }

    fun addEmail(email: String, otpCode: String, otpType: String) {
        _addEmailModel.value = _addEmailModel.value?.copy(btnLoading = true)
        launch {
            try {
                val param = AddEmailParam(email, otpCode, otpType)
                val result = dsarAddEmailUseCase(param).data
                if(result.isSuccess && result.errorMessage.isNotEmpty()) {
                    _routeToSuccessPage.call()
                } else if(result.errorMessage.isNotEmpty()) {
                    _toasterError.value = result.errorMessage
                }
            } catch (e: Exception) {
                _toasterError.value = e.message
            }
        }
    }

}
