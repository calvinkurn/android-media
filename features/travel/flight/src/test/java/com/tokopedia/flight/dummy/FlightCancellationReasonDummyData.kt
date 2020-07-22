package com.tokopedia.flight.dummy

import com.tokopedia.flight.cancellationV2.data.FlightCancellationPassengerEntity

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