package com.tokopedia.flight.common.util;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zulfikarrahman on 12/14/17.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({FlightPassengerTitle.TUAN, FlightPassengerTitle.NYONYA, FlightPassengerTitle.NONA})
public @interface FlightPassengerTitle {
    String TUAN = "tuan";
    String NYONYA = "nyonya";
    String NONA = "nona";
}
