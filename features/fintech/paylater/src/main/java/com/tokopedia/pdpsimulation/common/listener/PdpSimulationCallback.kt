package com.tokopedia.pdpsimulation.common.listener

import android.os.Bundle
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent

interface PdpSimulationCallback {
    fun showNoNetworkView()
    fun <T : Any> openBottomSheet(bundle: Bundle, modelClass: Class<T>)
    fun switchPaymentMode()
    fun showRegisterWidget()
    fun onRegisterWidgetClicked()
    fun getSimulationProductInfo()
    fun sendAnalytics(pdpSimulationEvent: PdpSimulationEvent)
}