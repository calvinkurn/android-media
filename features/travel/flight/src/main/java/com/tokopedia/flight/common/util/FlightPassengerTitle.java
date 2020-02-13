package com.tokopedia.flight.common.util;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({FlightPassengerTitle.TUAN, FlightPassengerTitle.NYONYA, FlightPassengerTitle.NONA})
public @interface FlightPassengerTitle {
    String TUAN = "tuan";
    String NYONYA = "nyonya";
    String NONA = "nona";
}
