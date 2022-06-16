package com.tokopedia.loginregister.goto_seamless

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import javax.inject.Inject

class GotoSeamlessLoginViewModel @Inject constructor(
    val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

}