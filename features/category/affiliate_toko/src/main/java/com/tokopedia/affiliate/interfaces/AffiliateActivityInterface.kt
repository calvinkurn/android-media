package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.request.OnboardAffiliateRequest

interface AffiliateActivityInterface {

    fun navigateToTermsFragment(channels : ArrayList<OnboardAffiliateRequest.OnboardAffiliateChannelRequest>)

    fun navigateToPortfolioFragment()

    fun validateUserStatus()

    fun onRegistrationSuccessful()

    fun handleBackButton()

}