package com.tokopedia.pdpsimulation.common.analytics

sealed class PdpSimulationEvent {
    sealed class PayLater {
        data class TabChangeEvent(val tabTitle: String) : PdpSimulationEvent()
        data class ChoosePayLaterOptionClickEvent(val payLaterProduct: String) :
            PdpSimulationEvent()

        data class PayLaterProductImpressionEvent(
            val payLaterProduct: String,
            val actionType: String?,
            val tenure: Int
        ) : PdpSimulationEvent()

        data class MainBottomSheetImpression(val payLaterProduct: String, val tenure: Int) :
            PdpSimulationEvent()

        data class TenureSortFilterClicker(val tenureSelector: String) : PdpSimulationEvent()

        data class MainBottomSheetClickEvent(
            val payLaterProduct: String,
            val tenure: Int,
            val url: String
        ) : PdpSimulationEvent()

        data class FaqImpression(val partnerName: String, val tenure: Int) : PdpSimulationEvent()
        data class FaqClickWebImpression(
            val partnerName: String,
            val tenure: Int,
            val url: String
        ) : PdpSimulationEvent()

        data class FaqOptionClicked(val partnerName: String, val tenure: Int) : PdpSimulationEvent()

        data class ClickCardButton(
            val tenure: Int,
            val partnerName: String,
            val buttonName: String,
            val redirectionUrl: String
        ) : PdpSimulationEvent()

        object SelectedPayLater : PdpSimulationEvent()

        data class TenureListImpression(val tenure: String) : PdpSimulationEvent()

        data class GopayBottomSheetImpression(val productId: String,val tenure: String,val partnerName: String,val emiAmount: String) : PdpSimulationEvent()

        data class GopayBottomSheetButtonClick(
            val productId: String,
            val tenure: String,
            val partnerName: String,
            val emiAmount: String,
            val url: String
        ) : PdpSimulationEvent()
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