package com.tokopedia.flight.common.view.enum

/**
 * @author by furqan on 30/10/2020
 */
enum class FlightPassengerType(val id: Int,
                               val type: String) {
    ADULT(0, "Dewasa"),
    CHILDREN(1, "Anak"),
    INFANT(2, "Bayi")
}