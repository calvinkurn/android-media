package com.tokopedia.flight.orderdetail.presentation.model.mapper

import com.tokopedia.flight.orderdetail.presentation.model.OrderDetailStatusEnum

/**
 * @author by furqan on 26/10/2020
 */
class OrderDetailStatusMapper {
    companion object {

        const val SUCCESS = 1
        const val FAILED = 2
        const val IN_PROGRESS = 3
        const val WAITING_FOR_PAYMENT = 4
        const val REFUNDED = 5

        fun getStatusOrder(status: Int): Int =
                when (status) {
                    OrderDetailStatusEnum.CONFIRMED.value, OrderDetailStatusEnum.FINISHED.value -> SUCCESS
                    OrderDetailStatusEnum.EXPIRED.value, OrderDetailStatusEnum.FAILED.value -> FAILED
                    OrderDetailStatusEnum.READY_FOR_QUEUE.value, OrderDetailStatusEnum.PROGRESS.value -> IN_PROGRESS
                    OrderDetailStatusEnum.WAITING_FOR_THIRD_PARTY.value, OrderDetailStatusEnum.WAITING_FOR_PAYMENT.value, OrderDetailStatusEnum.WAITING_FOR_TRANSFER.value -> WAITING_FOR_PAYMENT
                    OrderDetailStatusEnum.FLIGHT_CANCELLED.value, OrderDetailStatusEnum.REFUNDED.value -> REFUNDED
                    else -> 0
                }

    }
}