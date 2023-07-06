package com.tokopedia.chooseaccount.view.ocl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chooseaccount.data.DeleteOclParam
import com.tokopedia.chooseaccount.data.GetOclAccountParam
import com.tokopedia.chooseaccount.data.OclAccount
import com.tokopedia.chooseaccount.domain.usecase.DeleteOclAccountUseCase
import com.tokopedia.chooseaccount.domain.usecase.GetOclAccountUseCase
import com.tokopedia.sessioncommon.data.ocl.LoginOclParam
import com.tokopedia.sessioncommon.data.ocl.OclPreference
import com.tokopedia.sessioncommon.domain.usecase.GetUserInfoAndSaveSessionUseCase
import com.tokopedia.sessioncommon.domain.usecase.LoginOclUseCase
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class OclChooseAccountViewModel @Inject constructor(
    val getOclAccountUseCase: GetOclAccountUseCase,
    val loginOclUseCase: LoginOclUseCase,
    val getUserInfoAndSaveSessionUseCase: GetUserInfoAndSaveSessionUseCase,
    val deleteOclAccountUseCase: DeleteOclAccountUseCase,
    val oclPreference: OclPreference,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _mainLoader = MutableLiveData<Boolean>()
    val mainLoader: LiveData<Boolean> = _mainLoader

    private val _toasterError = SingleLiveEvent<String>()
    val toasterError: LiveData<String> = _toasterError

    private val _navigateToNormalLogin = SingleLiveEvent<Unit>()
    val navigateToNormalLogin: LiveData<Unit> = _navigateToNormalLogin

    private val _oclAccounts = MutableLiveData<ArrayList<OclAccount>>()
    val oclAccounts: LiveData<ArrayList<OclAccount>> = _oclAccounts

    private val _navigateToSuccessPage = SingleLiveEvent<Unit>()
    val navigateToSuccessPage: LiveData<Unit> = _navigateToSuccessPage

    private val _loginFailedToaster = SingleLiveEvent<String>()
    val loginFailedToaster: LiveData<String> = _loginFailedToaster

    fun getOclAccounts() {
        _mainLoader.value = true
        launch {
            try {
                val param = GetOclAccountParam(oclPreference.getToken())
                val result = getOclAccountUseCase(param)
                if (result.token.isNotEmpty()) {
                    oclPreference.storeToken(result.token)
                }
                if (result.users.isNotEmpty()) {
                    _oclAccounts.value = result.users
                    _mainLoader.value = false
                } else {
                    _navigateToNormalLogin.value = Unit
                }
            } catch (e: Exception) {
                _mainLoader.value = false
                _toasterError.value = e.message
            }
        }
    }

    fun loginOcl(token: String) {
        launch {
            try {
                val param = LoginOclParam(oclToken = oclPreference.getToken(), accountToken = token)
                val result = loginOclUseCase(param)
                if (result.accessToken.isNotEmpty()) {
                    getUserInfoAndSaveSessionUseCase(Unit)
                }
                _navigateToSuccessPage.value = Unit
            } catch (e: Exception) {
                _loginFailedToaster.value = e.message
            }
        }
    }

    fun deleteAccount(user: OclAccount) {
        launch {
            try {
                val param = DeleteOclParam(token = oclPreference.getToken(), userToken = user.token)
                val result = deleteOclAccountUseCase(param)
                if (result.token.isNotEmpty()) {
                    oclPreference.storeToken(result.token)
                }
                val newItem = _oclAccounts.value
                newItem?.remove(user)
                if (newItem?.isEmpty() == true) {
                    _navigateToNormalLogin.value = Unit
                } else {
                    newItem?.let {
                        _oclAccounts.value = it
                    }
                }
            } catch (e: Exception) {
                _toasterError.value = e.message
            }
        }
    }
}
