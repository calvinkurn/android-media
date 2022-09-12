package com.tokopedia.loginregister.redefine_register_email.view.activity

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class RedefineRegisterViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private var _isAllowBackPressed = true
    val isAllowBackPressed get() = _isAllowBackPressed

    fun isAllowBackPressed(isAllow: Boolean) {
        _isAllowBackPressed = isAllow
    }

}