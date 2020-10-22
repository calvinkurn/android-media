package com.tokopedia.flight.dummy

import com.tokopedia.flight.orderdetail.presentation.model.*

/**
 * @author by furqan on 16/10/2020
 */
val DUMMY_ORDER_DETAIL_DATA = OrderDetailDataModel(
        1,
        "0001-01-01T00:00:00Z",
        1,
        "Berhasil",
        1,
        "1234567890",
        "Muhammad Furqan",
        "email@email.com",
        "123456789012",
        "ID",
        "Rp1.000.000",
        1000000,
        "Rp0",
        0,
        "Rp0",
        0,
        "Rp1.000.000",
        1000000,
        "Rp",
        "dummy pdf",
        true,
        false,
        "Ekonomi",
        "dummy contact us URL",
        OrderDetailPaymentModel(
                1,
                1,
                "Paid",
                "BCA Virtual Account",
                "dummy icon",
                "0001-01-01T00:00:00Z",
                "",
                "ididid",
                "",
                0,
                "Rp0",
                0,
                "rp0",
                0,
                "Rp0",
                0,
                "Rp0",
                0,
                "Rp0",
                0,
                "BCA",
                "Jakarta",
                "1234567890",
                "Furqan",
                "Rp1.000.000"
        ),
        arrayListOf(
                OrderDetailJourneyModel(
                        1,
                        1,
                        "123",
                        "0001-01-01T00:00:00Z",
                        "departure airport",
                        "aceh",
                        "321",
                        "0001-01-01T00:00:00Z",
                        "arrival airport",
                        "jakarta",
                        0,
                        0,
                        0,
                        "2j",
                        120,
                        OrderDetailFareModel(
                                1000000,
                                0,
                                0
                        ),
                        arrayListOf(
                                OrderDetailRouteModel(
                                        "123",
                                        "departure time",
                                        "departure airport",
                                        "departure city",
                                        "321",
                                        "arrival time",
                                        "arrival airport",
                                        "arrival city",
                                        "123ASD",
                                        "890",
                                        "Seulawah Air",
                                        "",
                                        "",
                                        "AN-12345",
                                        "2j",
                                        120,
                                        "",
                                        0,
                                        false,
                                        "2",
                                        "",
                                        0,
                                        "",
                                        arrayListOf(),
                                        arrayListOf(),
                                        OrderDetailFreeAmenityModel(
                                                OrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                OrderDetailFreeAmenityModel.OrderDetailBaggageModel(true, "kg", 1),
                                                false,
                                                false,
                                                false,
                                                arrayListOf()
                                        )
                                )
                        ),
                        OrderDetailWebCheckInModel(
                                "Check In",
                                "",
                                "",
                                "",
                                "",
                                "",
                                ""
                        )
                )
        ),
        arrayListOf(
                OrderDetailPassengerModel(
                        1,
                        1,
                        1,
                        "Muhammad",
                        "Furqan",
                        "",
                        "",
                        "",
                        "",
                        "",
                        arrayListOf(),
                        arrayListOf()
                )
        ),
        arrayListOf(),
        arrayListOf(),
        arrayListOf(),
        arrayListOf()
)
