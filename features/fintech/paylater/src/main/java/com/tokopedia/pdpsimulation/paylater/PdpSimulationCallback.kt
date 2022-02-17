package com.tokopedia.pdpsimulation.paylater

import android.os.Bundle
import com.tokopedia.pdpsimulation.common.analytics.PayLaterAnalyticsBase

interface PdpSimulationCallback {
    fun <T : Any> openBottomSheet(bundle: Bundle, modelClass: Class<T>)
    fun sendAnalytics(pdpSimulationEvent: PayLaterAnalyticsBase)
}