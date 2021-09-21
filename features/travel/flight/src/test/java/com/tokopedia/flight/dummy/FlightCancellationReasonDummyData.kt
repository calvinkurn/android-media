package com.tokopedia.flight.dummy

import com.tokopedia.flight.cancellation.data.FlightCancellationPassengerEntity
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationPassengerModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationReasonAndAttachmentModel
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationWrapperModel
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney

/**
 * @author by furqan on 22/07/2020
 */

val DUMMY_REASON = FlightCancellationPassengerEntity.Reason(
        "1",
        "Testing Flight Cancellation Reason",
        arrayListOf(),
        arrayListOf(
                FlightCancellationPassengerEntity.RequiredDoc(
                        "1",
                        "KTP"
                )
        )
)

val DUMMY_REASON_LIST = arrayListOf<FlightCancellationPassengerEntity.Reason>(
        FlightCancellationPassengerEntity.Reason(
                "1",
                "Testing Flight Cancellation Reason 1",
                arrayListOf(),
                arrayListOf(
                        FlightCancellationPassengerEntity.RequiredDoc(
                                "1",
                                "KTP"
                        )
                )
        ),
        FlightCancellationPassengerEntity.Reason(
                "2",
                "Testing Flight Cancellation Reason 2",
                arrayListOf(),
                arrayListOf()
        ),
        FlightCancellationPassengerEntity.Reason(
                "3",
                "Testing Flight Cancellation Reason 3",
                arrayListOf(),
                arrayListOf(
                        FlightCancellationPassengerEntity.RequiredDoc(
                                "2",
                                "KK"
                        )
                )
        )
)

val DUMMY_CANCELLATION_WRAPPER_ATTACHMENT =
        FlightCancellationWrapperModel(
                FlightCancellationReasonAndAttachmentModel(
                        arrayListOf(),
                        "",
                        "",
                        0,
                        "",
                        false
                ),
                arrayListOf(
                        FlightCancellationModel(
                                "1234567890",
                                FlightCancellationJourney(
                                        "54321",
                                        "Jakarta",
                                        "JKTA",
                                        "",
                                        "2020-11-11T10:10:10Z",
                                        "Banda Aceh",
                                        "",
                                        "BTJ",
                                        "2020-11-11T20:20:20Z",
                                        "Garuda Indonesia",
                                        true,
                                        arrayListOf()
                                ),
                                arrayListOf(
                                        FlightCancellationPassengerModel(
                                                "12345",
                                                0,
                                                1,
                                                "Tuan",
                                                "Test",
                                                "Penumpang",
                                                "54321-12345",
                                                arrayListOf(),
                                                0,
                                                ""
                                        ),
                                        FlightCancellationPassengerModel(
                                                "67890",
                                                0,
                                                1,
                                                "Tuan",
                                                "Penumpang",
                                                "Test",
                                                "54321-67890",
                                                arrayListOf(),
                                                0,
                                                ""
                                        )
                                )
                        ),
                        FlightCancellationModel(
                                "1234567890",
                                FlightCancellationJourney(
                                        "54321",
                                        "Banda Aceh",
                                        "",
                                        "BTJ",
                                        "2020-12-11T10:10:10Z",
                                        "Jakarta",
                                        "JKTA",
                                        "",
                                        "2020-12-11T20:20:20Z",
                                        "Garuda Indonesia",
                                        true,
                                        arrayListOf()
                                ),
                                arrayListOf(
                                        FlightCancellationPassengerModel(
                                                "12345",
                                                0,
                                                1,
                                                "Tuan",
                                                "Test",
                                                "Penumpang",
                                                "09876-12345",
                                                arrayListOf(),
                                                0,
                                                ""
                                        ))
                        )
                )
        )