package com.tokopedia.pdpsimulation.common.listener

import android.os.Bundle

interface PdpSimulationCallback {
    fun showNoNetworkView()
    fun<T: Any> openBottomSheet(bundle: Bundle, modelClass: Class<T>)
    fun switchPaymentMode()
    fun showRegisterWidget()
    fun onRegisterWidgetClicked()
    fun getSimulationProductInfo()
}