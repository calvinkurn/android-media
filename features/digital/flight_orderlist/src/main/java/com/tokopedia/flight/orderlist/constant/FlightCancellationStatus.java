package com.tokopedia.flight.orderlist.constant;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author by furqan on 04/05/18.
 */

@IntDef({FlightCancellationStatus.PENDING, FlightCancellationStatus.REFUNDED, FlightCancellationStatus.ABORTED, FlightCancellationStatus.REQUESTED})
@Retention(RetentionPolicy.SOURCE)
public @interface FlightCancellationStatus {
    int PENDING = 1;
    int REFUNDED = 2;
    int ABORTED = 3;
    int REQUESTED = 4;
}
