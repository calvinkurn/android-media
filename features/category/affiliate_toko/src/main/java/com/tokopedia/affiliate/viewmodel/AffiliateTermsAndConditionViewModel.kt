package com.tokopedia.affiliate.viewmodel


import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliateTermsAndConditionViewModel@Inject constructor(
    private val userSessionInterface: UserSessionInterface)
    :BaseViewModel() {

}