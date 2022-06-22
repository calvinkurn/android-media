package com.tokopedia.loginregister.goto_seamless

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.loginregister.goto_seamless.usecase.GetTemporaryKeyUseCase
import com.tokopedia.loginregister.goto_seamless.usecase.LoginSeamlessUseCase
import javax.inject.Inject

class GotoSeamlessLoginViewModel @Inject constructor(
    val getTemporaryKeyUseCase: GetTemporaryKeyUseCase,
    val loginSeamlessUseCase: LoginSeamlessUseCase,
    val dispatchers: CoroutineDispatchers
): BaseViewModel(dispatchers.main) {

}