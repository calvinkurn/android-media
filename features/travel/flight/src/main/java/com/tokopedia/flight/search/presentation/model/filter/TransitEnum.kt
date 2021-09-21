package com.tokopedia.flight.search.presentation.model.filter

import com.tokopedia.flight.R

/**
 * @author by furqan on 25/06/2020
 */
enum class TransitEnum(val id: Int,
                       val valueRes: Int) {
    DIRECT(1, R.string.direct),
    ONE(2, R.string.one_trasit),
    TWO(3, R.string.two_transit)
}