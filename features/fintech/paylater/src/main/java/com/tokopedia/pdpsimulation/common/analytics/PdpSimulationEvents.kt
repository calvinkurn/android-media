package com.tokopedia.pdpsimulation.common.analytics

sealed class PdpSimulationEvent {
    sealed class PayLater {
        data class PayLaterProductImpressionEvent(
            val payLaterProduct: String,
            val actionType: String?,
            val tenure: Int
        ) : PdpSimulationEvent()

        data class MainBottomSheetImpression(val payLaterProduct: String, val tenure: Int) :
            PdpSimulationEvent()

        data class MainBottomSheetClickEvent(
            val payLaterProduct: String,
            val tenure: Int,
            val url: String
        ) : PdpSimulationEvent()

        data class ClickCardButton(
            val tenure: Int,
            val partnerName: String,
            val buttonName: String,
            val redirectionUrl: String
        ) : PdpSimulationEvent()

        object SelectedPayLater : PdpSimulationEvent()

        data class TenureListImpression(val tenure: String) : PdpSimulationEvent()

        data class GopayBottomSheetImpression(
            val productId: String,
            val tenure: String,
            val partnerName: String,
            val emiAmount: String
        ) : PdpSimulationEvent()

        data class GopayBottomSheetButtonClick(
            val productId: String,
            val tenure: String,
            val partnerName: String,
            val emiAmount: String,
            val url: String
        ) : PdpSimulationEvent()
    }
}