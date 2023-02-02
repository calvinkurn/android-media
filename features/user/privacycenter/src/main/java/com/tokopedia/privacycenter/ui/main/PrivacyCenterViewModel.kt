package com.tokopedia.privacycenter.ui.main

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class PrivacyCenterViewModel @Inject constructor(
    private val userSessionInterface: UserSessionInterface,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun isLoggedIn(): Boolean {
        return userSessionInterface.isLoggedIn
    }
}
