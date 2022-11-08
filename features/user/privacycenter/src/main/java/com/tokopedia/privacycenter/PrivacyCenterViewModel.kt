package com.tokopedia.privacycenter

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.privacycenter.domain.UserUseCase
import javax.inject.Inject

class PrivacyCenterViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    fun getUserName(): String {
        return userUseCase.getUserName()
    }

}
