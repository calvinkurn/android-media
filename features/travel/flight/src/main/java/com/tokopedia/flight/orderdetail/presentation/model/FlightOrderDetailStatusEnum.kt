package com.tokopedia.flight.orderdetail.presentation.model

/**
 * @author by furqan on 26/10/2020
 */
enum class FlightOrderDetailStatusEnum(val value: Int) {
    EXPIRED(0),
    WAITING_FOR_PAYMENT(100),
    WAITING_FOR_THIRD_PARTY(101),
    WAITING_FOR_TRANSFER(102),
    READY_FOR_QUEUE(200),
    PROGRESS(300),
    FAILED(600),
    FLIGHT_CANCELLED(610),
    REFUNDED(650),
    CONFIRMED(700),
    FINISHED(800)
}