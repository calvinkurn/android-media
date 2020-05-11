package com.tokopedia.flight.common.util;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zulfikarrahman on 12/14/17.
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({FlightPassengerTitleType.TUAN, FlightPassengerTitleType.NYONYA, FlightPassengerTitleType.NONA})
public @interface FlightPassengerTitleType {
    int TUAN = 1;
    int NYONYA = 2;
    int NONA = 3;
}
