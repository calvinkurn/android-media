package com.tokopedia.chooseaccount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chooseaccount.data.GetOclAccountParam
import com.tokopedia.chooseaccount.data.OclAccount
import com.tokopedia.chooseaccount.domain.usecase.GetOclAccountUseCase
import com.tokopedia.sessioncommon.data.ocl.OclPreference
import com.tokopedia.utils.lifecycle.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class OclChooseAccountViewModel @Inject constructor(
    val getOclAccountUseCase: GetOclAccountUseCase,
    val oclPreference: OclPreference,
    dispatcher: CoroutineDispatchers,
): BaseViewModel(dispatcher.main) {

    private val _mainLoader = SingleLiveEvent<Boolean>()
    val mainLoader: LiveData<Boolean> = _mainLoader

    private val _toasterError = SingleLiveEvent<String>()
    val toasterError: LiveData<String> = _toasterError

    private val _navigateToNormalLogin = SingleLiveEvent<Boolean>()
    val navigateToNormalLogin: LiveData<Boolean> = _navigateToNormalLogin

    private val _oclAccounts = MutableLiveData<ArrayList<OclAccount>>()
    val oclAccounts: LiveData<ArrayList<OclAccount>> = _oclAccounts

    fun getOclAccounts() {
        launch {
            try {
                val param = GetOclAccountParam(oclPreference.getToken())
                val result = getOclAccountUseCase(param)
                if(result.users.isNotEmpty()) {
                    _oclAccounts.value = result.users
                } else {
                    _navigateToNormalLogin.value = true
                }
            } catch (e: Exception) {
                _toasterError.value = e.message
            }
        }
    }
}
