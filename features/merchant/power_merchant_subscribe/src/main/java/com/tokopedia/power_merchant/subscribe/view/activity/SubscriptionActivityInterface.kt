package com.tokopedia.power_merchant.subscribe.view.activity

/**
 * Created By @ilhamsuaib on 27/03/21
 */

interface SubscriptionActivityInterface {

    fun showLoadingState()

    fun showErrorState(throwable: Throwable)

    fun setViewForRegistrationPage()

    fun setViewForPmSuccessState()

    fun hideActivationProgress()

    fun showActivationProgress()
}