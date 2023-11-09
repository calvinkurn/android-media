package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.mapper

import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.orderextension.presentation.model.StringRes
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model.ToasterQueue
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class PofToasterMapper @Inject constructor() {
    fun mapToasterCannotEmptyAllProduct(): ToasterQueue {
        return ToasterQueue(text = StringRes(R.string.som_pof_toaster_error_cannot_empty_all_products))
    }

    fun mapToasterCannotExceedCheckoutQuantity(): ToasterQueue {
        return ToasterQueue(text = StringRes(R.string.som_pof_toaster_error_cannot_exceed_checkout_quantity))
    }

    fun mapToasterErrorSendPof(throwable: Throwable): ToasterQueue {
        val errorCode = ErrorHandler.getErrorMessagePair(null, throwable, ErrorHandler.Builder().build()).second
        return ToasterQueue(
            text = StringRes(R.string.som_pof_toaster_error_send_pof, listOf(errorCode)),
            type = Toaster.TYPE_ERROR
        )
    }

    fun mapToasterErrorReFetchPofEstimate(throwable: Throwable): ToasterQueue {
        val errorCode = ErrorHandler.getErrorMessagePair(null, throwable, ErrorHandler.Builder().build()).second
        return ToasterQueue(
            text = StringRes(R.string.som_pof_toaster_error_re_fetch_pof_estimate, listOf(errorCode)),
            type = Toaster.TYPE_ERROR
        )
    }
}
