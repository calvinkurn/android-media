package com.tokopedia.privacycenter.main

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.common.domain.UserUseCase
import javax.inject.Inject

class PrivacyCenterViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    fun getUserName(): String {
        return userUseCase.getUserName()
    }

    fun isLoggedIn(): Boolean {
        return userUseCase.isLoggedIn()
    }
}
