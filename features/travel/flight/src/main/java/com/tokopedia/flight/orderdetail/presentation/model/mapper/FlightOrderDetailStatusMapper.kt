package com.tokopedia.flight.orderdetail.presentation.model.mapper

import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailStatusEnum

/**
 * @author by furqan on 26/10/2020
 */
class FlightOrderDetailStatusMapper {
    companion object {

        const val SUCCESS = 1
        const val FAILED = 2
        const val IN_PROGRESS = 3
        const val WAITING_FOR_PAYMENT = 4
        const val REFUNDED = 5

        fun getStatusOrder(status: Int): Int =
                when (status) {
                    FlightOrderDetailStatusEnum.CONFIRMED.value, FlightOrderDetailStatusEnum.FINISHED.value -> SUCCESS
                    FlightOrderDetailStatusEnum.EXPIRED.value, FlightOrderDetailStatusEnum.FAILED.value -> FAILED
                    FlightOrderDetailStatusEnum.READY_FOR_QUEUE.value, FlightOrderDetailStatusEnum.PROGRESS.value -> IN_PROGRESS
                    FlightOrderDetailStatusEnum.WAITING_FOR_THIRD_PARTY.value, FlightOrderDetailStatusEnum.WAITING_FOR_PAYMENT.value, FlightOrderDetailStatusEnum.WAITING_FOR_TRANSFER.value -> WAITING_FOR_PAYMENT
                    FlightOrderDetailStatusEnum.FLIGHT_CANCELLED.value, FlightOrderDetailStatusEnum.REFUNDED.value -> REFUNDED
                    else -> 0
                }

    }
}