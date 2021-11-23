package com.tokopedia.pdpsimulation.common.listener

import android.os.Bundle
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.utils.Utils

interface PdpSimulationCallback {
    fun showNoNetworkView()
    fun <T : Any> openBottomSheet(bundle: Bundle, modelClass: Class<T>)
    fun switchPaymentMode()
    fun showRegisterWidget()
    fun getSimulationProductInfo()
    fun sendAnalytics(pdpSimulationEvent: PdpSimulationEvent)
    fun reloadProductDetail()
    fun setViewModelData(updateViewModelVariable: Utils.UpdateViewModelVariable, value: Any)
}