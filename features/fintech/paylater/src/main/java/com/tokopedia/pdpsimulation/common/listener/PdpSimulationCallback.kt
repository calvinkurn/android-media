package com.tokopedia.pdpsimulation.common.listener

import android.os.Bundle
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.utils.Utils

interface PdpSimulationCallback {
    fun <T : Any> openBottomSheet(bundle: Bundle, modelClass: Class<T>)
    fun sendAnalytics(pdpSimulationEvent: PdpSimulationEvent)
}