package com.tokopedia.pdpsimulation.common.helper

sealed class PdpSimulationException(errMessage: String) : Throwable(errMessage) {
    class PayLaterNotApplicableException(errMessage: String) : PdpSimulationException(errMessage)
    class PayLaterNullDataException(errMessage: String) : PdpSimulationException(errMessage)
    class CreditCardNullDataException(errMessage: String) : PdpSimulationException(errMessage)
    class CreditCardSimulationNotAvailableException(errMessage: String) : PdpSimulationException(errMessage)
}