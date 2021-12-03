package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.request.OnBoardingRequest

interface AffiliateActivityInterface {

    fun navigateToTermsFragment(channels : ArrayList<OnBoardingRequest.Channel>)

    fun navigateToPortfolioFragment()

    fun validateUserStatus()

    fun onRegistrationSuccessful()

    fun handleBackButton()

}