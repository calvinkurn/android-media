package com.tokopedia.common_digital.common.data.api

interface DigitalError {
    companion object {
        val STATUS_UNDER_MAINTENANCE = "UNDER_MAINTENANCE"
        val STATUS_REQUEST_DENIED = "REQUEST_DENIED"
        val STATUS_FORBIDDEN = "FORBIDDEN"
    }
}
