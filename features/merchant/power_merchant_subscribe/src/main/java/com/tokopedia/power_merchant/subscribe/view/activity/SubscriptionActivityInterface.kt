package com.tokopedia.power_merchant.subscribe.view.activity

/**
 * Created By @ilhamsuaib on 27/03/21
 */

interface SubscriptionActivityInterface {

    fun showLoadingState()

    fun showErrorState(throwable: Throwable)

    fun setViewForRegistrationPage()

    fun renderFooterView()

    fun hideFooterView()

    fun setViewForPmSuccessState()

    fun hideActivationProgress()

    fun showActivationProgress()

    fun stopRenderPerformanceMonitoring()

    fun startCustomMetricPerformanceMonitoring(tag: String)

    fun stopCustomMetricPerformanceMonitoring(tag: String)
}