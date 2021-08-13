package com.tokopedia.flight.orderdetail.presentation.model

/**
 * @author by furqan on 13/11/2020
 */
enum class FlightOrderDetailAmenityEnum(val type: Int,
                                        val text: String) {
    LUGGAGE(1, "Bagasi"),
    MEAL(2, "Makanan")
}