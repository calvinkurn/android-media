package com.tokopedia.exploreCategory.viewmodel

import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateHomeViewModel @Inject constructor(
        private val userSessionInterface: UserSessionInterface
) : BaseViewModel() {

    fun getUserName() : String{
        return userSessionInterface.name
    }

    fun isUserLoggedIn() : Boolean {
        return userSessionInterface.isLoggedIn
    }
}