package com.tokopedia.flight.dummy

import com.tokopedia.flight.cancellationV2.data.FlightCancellationEstimateDetail
import com.tokopedia.flight.cancellationV2.data.FlightCancellationEstimateEntity
import com.tokopedia.flight.cancellationV2.data.FlightCancellationRequestEntity
import com.tokopedia.flight.cancellationV2.presentation.model.*
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney

/**
 * @author by furqan on 22/07/2020
 */

val DUMMY_CANCELLATION_WRAPPER =
        FlightCancellationWrapperModel(
                FlightCancellationReasonAndAttachmentModel(
                        arrayListOf(
                                FlightCancellationAttachmentModel(
                                        "dummy.jpg",
                                        "storage/images/dummy.jpg",
                                        "12345",
                                        "Dummy Passenger",
                                        "54321-12345",
                                        "54321",
                                        1,
                                        0,
                                        false
                                )
                        ),
                        "Test Cancellation Reason",
                        "1",
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
                                arrayListOf()
                        )
                ),
                "1234567890"
        )

val DUMMY_EMPTY_FILEPATH_CANCELLATION_WRAPPER =
        FlightCancellationWrapperModel(
                FlightCancellationReasonAndAttachmentModel(
                        arrayListOf(
                                FlightCancellationAttachmentModel(
                                        "dummy.jpg",
                                        "",
                                        "12345",
                                        "Dummy Passenger",
                                        "54321-12345",
                                        "54321",
                                        1,
                                        0,
                                        false
                                )
                        ),
                        "Test Cancellation Reason",
                        "1",
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
                                arrayListOf()
                        )
                )
        )

val DUMMY_NOT_REFUNDABLE_CANCELLATION_WRAPPER =
        FlightCancellationWrapperModel(
                FlightCancellationReasonAndAttachmentModel(
                        arrayListOf(),
                        "Test Cancellation Reason",
                        "1",
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
                                        false,
                                        arrayListOf()
                                ),
                                arrayListOf(
                                        FlightCancellationPassengerModel(
                                                "12345",
                                                1,
                                                1,
                                                "Tuan",
                                                "Test",
                                                "Penumpang",
                                                "54321-12345",
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
                                        false,
                                        arrayListOf()
                                ),
                                arrayListOf()
                        )
                )
        )

val DUMMY_ESTIMATE_REFUND = FlightCancellationEstimateEntity(
        arrayListOf(
                FlightCancellationEstimateDetail(
                        54321,
                        12345,
                        1000000
                )
        ),
        "Rp1.000.000",
        1000000,
        true,
        arrayListOf(
                "Dummy Refund Notes 1",
                "Dummy Refund Notes 2"
        ),
        arrayListOf(
                "Dummy no refund"
        ),
        "Penerbangan not refundable"
)

val DUMMY_CANCEL_REQUEST = FlightCancellationRequestEntity(
        "1",
        1,
        arrayListOf()
)