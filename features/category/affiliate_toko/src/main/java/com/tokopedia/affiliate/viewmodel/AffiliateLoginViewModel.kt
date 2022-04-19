package com.tokopedia.affiliate.viewmodel

import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateLoginViewModel  @Inject constructor(
        private val userSessionInterface: UserSessionInterface,
) : BaseViewModel() {


    fun getUserName(): String {
        return userSessionInterface.name
    }

    fun getUserEmail(): String {
        return userSessionInterface.email
    }

    fun getUserProfilePicture(): String {
        return userSessionInterface.profilePicture
    }

    fun isUserLoggedIn(): Boolean {
        return userSessionInterface.isLoggedIn
    }

}