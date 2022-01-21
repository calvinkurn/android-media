package com.tokopedia.flight.cancellation.presentation.model

class FlightCancellationStatus {
    companion object {
        var PENDING = 1
        var REFUNDED = 2
        var ABORTED = 3
        var REQUESTED = 4
    }
}
