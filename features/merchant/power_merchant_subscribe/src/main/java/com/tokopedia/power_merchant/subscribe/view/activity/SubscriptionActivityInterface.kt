package com.tokopedia.power_merchant.subscribe.view.activity

/**
 * Created By @ilhamsuaib on 27/03/21
 */

interface SubscriptionActivityInterface {

    fun fetchPowerMerchantBasicInfo()

    fun showLoadingState()

    fun showErrorState()

    fun setViewForRegistrationPage()

    fun setViewForPmSuccessState()

    fun hideActivationProgress()

    fun showActivationProgress()
}