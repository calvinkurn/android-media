package com.tokopedia.privacycenter.dsar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.dsar.domain.DsarAddEmailUseCase
import com.tokopedia.privacycenter.dsar.domain.DsarCheckEmailUseCase
import com.tokopedia.privacycenter.dsar.model.AddEmailParam
import kotlinx.coroutines.launch
import javax.inject.Inject

class DsarAddEmailViewModel @Inject constructor(
    private val dsarCheckEmailUseCase: DsarCheckEmailUseCase,
    private val dsarAddEmailUseCase: DsarAddEmailUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val checkEmail = MutableLiveData<String>()
    val _checkEmail: LiveData<String> = checkEmail

    private val addEmail = MutableLiveData<Boolean>()
    val _addEmail: LiveData<Boolean> = addEmail

    fun checkEmail(email: String) {
        launch {
            try {
                val param = mapOf("email" to email)
                val result = dsarCheckEmailUseCase(param).data
                if(result.isValid) {
                    checkEmail.value = email
                } else {
                    checkEmail.value = ""
                }
            } catch (e: Exception) {
                checkEmail.value = ""
            }
        }
    }

    fun addEmail(email: String, otpCode: String, otpType: String) {
        launch {
            try {
                val param = AddEmailParam(email, otpCode, otpType)
                addEmail.value = dsarAddEmailUseCase(param).data.isSuccess
            } catch (e: Exception) {
                addEmail.value = false
            }
        }
    }

}
