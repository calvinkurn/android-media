package com.tokopedia.pdpsimulation.common.analytics

sealed class PdpSimulationEvent {
    sealed class PayLater {
        data class TabChangeEvent(val tabTitle: String) : PdpSimulationEvent()
        data class RegisterWidgetClickEvent(val tag: String) : PdpSimulationEvent()
        data class ChoosePayLaterOptionClickEvent(val payLaterProduct: String) : PdpSimulationEvent()
        data class PayLaterProductImpressionEvent(val payLaterProduct: String, val actionType: String?) : PdpSimulationEvent()
        data class RegisterPayLaterOptionClickEvent(val payLaterProduct: String) : PdpSimulationEvent()
    }

    sealed class CreditCard {
        data class TabChangeEvent(val tabTitle: String) : PdpSimulationEvent()
        data class CCNotAvailableEvent(val tag: String) : PdpSimulationEvent()
        data class ApplyCreditCardEvent(val tag: String) : PdpSimulationEvent()
        data class ChooseBankClickEvent(val bankName: String) : PdpSimulationEvent()
        data class SeeMoreBankClickEvent(val tag: String) : PdpSimulationEvent()
        data class ChooseCardClickEvent(val cardName: String) : PdpSimulationEvent()
        data class SeeMoreCardClickEvent(val tag: String) : PdpSimulationEvent()
    }
}