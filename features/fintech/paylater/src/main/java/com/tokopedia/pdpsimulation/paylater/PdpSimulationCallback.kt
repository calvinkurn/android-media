package com.tokopedia.pdpsimulation.paylater

import android.os.Bundle
import com.tokopedia.pdpsimulation.common.analytics.PayLaterAnalyticsBase
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent

interface PdpSimulationCallback {
    fun <T : Any> openBottomSheet(bundle: Bundle, modelClass: Class<T>)
    fun sendOtherAnalytics(pdpSimulationEvent: PdpSimulationEvent)
    fun sendAnalytics(pdpSimulationEvent: PayLaterAnalyticsBase)
}