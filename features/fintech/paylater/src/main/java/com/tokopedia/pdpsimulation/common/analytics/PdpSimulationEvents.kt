package com.tokopedia.pdpsimulation.common.analytics

import com.tokopedia.pdpsimulation.common.helper.PaymentMode

sealed class PdpSimulationEvent {
    sealed class PayLater {
        data class TabChangeEvent(val mode: PaymentMode, val tabTitle: String) : PdpSimulationEvent()
        data class RegisterWidgetClickEvent(val tag: String) : PdpSimulationEvent()
        data class ChoosePayLaterOptionClickEvent(val payLaterProduct: String) : PdpSimulationEvent()
        data class PayLaterProductImpressionEvent(val payLaterProduct: String, val actionType: String?) : PdpSimulationEvent()
        data class RegisterPayLaterOptionClickEvent(val payLaterProduct: String): PdpSimulationEvent()
    }
    sealed class CreditCard {

    }
}