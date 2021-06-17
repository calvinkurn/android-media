package com.tokopedia.flight.search.presentation.model.filter

import com.tokopedia.flight.R

/**
 * @author by furqan on 25/06/2020
 */
enum class DepartureTimeEnum(val id: Int, val valueRes: Int) {
    _00(1, R.string.departure_0000_to_0600),
    _06(2, R.string.departure_0600_to_1200),
    _12(3, R.string.departure_1200_to_1800),
    _18(4, R.string.departure_1800_to_2400)
}