package com.tokopedia.flight.search.presentation.model.filter

import com.tokopedia.flight.R

/**
 * @author by furqan on 25/06/2020
 */
enum class RefundableEnum(val id: Int, val valueRes: Int) {
    REFUNDABLE(1, R.string.refundable),
    PARTIAL_REFUNDABLE(2, R.string.partial_refundable),
    NOT_REFUNDABLE(3, R.string.non_refundable)
}