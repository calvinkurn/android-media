package com.tokopedia.flight.dummy

import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney

/**
 * @author by furqan on 23/07/2020
 */
val DUMMY_EMPTY_PASSENGER_SELECTED_CANCELLATION = arrayListOf<FlightCancellationModel>(
        FlightCancellationModel(
                "1234567890",
                FlightCancellationJourney(
                        "54321",
                        "Banda Aceh",
                        "",
                        "BTJ",
                        "2020-11-11T10:10:10Z",
                        "Jakarta",
                        "JKTA",
                        "",
                        "2021-11-11T10:10:10Z",
                        "Seulawah Air",
                        true,
                        arrayListOf()
                ),
                arrayListOf()
        )
)

val DUMMY_WITH_PASSENGER_PASSENGER_SELECTED_CANCELLATION = arrayListOf<FlightCancellationModel>(
        FlightCancellationModel(
                "1234567890",
                FlightCancellationJourney(
                        "54321",
                        "Banda Aceh",
                        "",
                        "BTJ",
                        "2020-11-11T10:10:10Z",
                        "Jakarta",
                        "JKTA",
                        "",
                        "2020-11-12T10:10:10Z",
                        "Seulawah Air",
                        true,
                        arrayListOf()
                ),
                arrayListOf(
                        FlightCancellationPassengerModel(
                                "12345",
                                1,
                                1,
                                "Tuan",
                                "Penumpang",
                                "Testing",
                                "12345-54321",
                                arrayListOf("67890-54321"),
                                0,
                                ""
                        ),
                        FlightCancellationPassengerModel(
                                "67890",
                                1,
                                1,
                                "Tuan",
                                "Penumpang",
                                "Testing",
                                "67890-54321",
                                arrayListOf("12345-54321"),
                                0,
                                ""
                        )
                )
        ),
        FlightCancellationModel(
                "0987654321",
                FlightCancellationJourney(
                        "09876",
                        "Jakarta",
                        "JKTA",
                        "",
                        "2020-10-11T10:10:10Z",
                        "Banda Aceh",
                        "",
                        "BTJ",
                        "2020-11-11T10:10:10Z",
                        "Seulawah Air",
                        true,
                        arrayListOf()
                ),
                arrayListOf(
                        FlightCancellationPassengerModel(
                                "12345",
                                1,
                                1,
                                "Tuan",
                                "Penumpang",
                                "Testing",
                                "",
                                arrayListOf(),
                                0,
                                ""
                        )
                )
        )
)

val DUMMY_CANCELLATION_JOURNEY = arrayListOf(
        FlightCancellationJourney(
                "54321",
                "Banda Aceh",
                "",
                "BTJ",
                "2020-11-11T10:10:10Z",
                "Jakarta",
                "JKTA",
                "",
                "2020-11-12T10:10:10Z",
                "Seulawah Air",
                true,
                arrayListOf()
        ),
        FlightCancellationJourney(
                "09876",
                "Jakarta",
                "JKTA",
                "",
                "2020-10-11T10:10:10Z",
                "Banda Aceh",
                "",
                "BTJ",
                "2020-11-11T10:10:10Z",
                "Seulawah Air",
                true,
                arrayListOf()
        )
)

val DUMMY_CANCELLED_PASSENGER =
        FlightCancellationPassengerModel(
                "10101",
                1,
                1,
                "Tuan",
                "HHHHHH",
                "IIIII",
                "10101-54321",
                arrayListOf(),
                0,
                "Penumpang ini sudah dibatalkan"
        )