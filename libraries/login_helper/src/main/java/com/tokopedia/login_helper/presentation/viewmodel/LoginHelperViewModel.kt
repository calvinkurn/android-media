package com.tokopedia.login_helper.presentation.viewmodel

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class LoginHelperViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {



}
