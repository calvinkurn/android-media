package com.tokopedia.flight.cancellationdetail.presentation.model

/**
 * @author by furqan on 06/01/2021
 */
enum class FlightOrderCancellationStatusEnum(val id: Int) {
    PENDING(1),
    REFUNDED(2),
    ABORTED(3),
    REQUESTED(4)
}