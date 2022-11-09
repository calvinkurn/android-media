package com.tokopedia.privacycenter.common.domain

import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val userSessionInterface: UserSessionInterface
) {

    fun getUserName(): String = "Hi, ${userSessionInterface.name}"

}
