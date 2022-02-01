package com.tokopedia.pdpsimulation.paylater.helper

sealed class PdpSimulationException(errMessage: String) : Throwable(errMessage) {
    class PayLaterNullDataException(errMessage: String) : PdpSimulationException(errMessage)
}