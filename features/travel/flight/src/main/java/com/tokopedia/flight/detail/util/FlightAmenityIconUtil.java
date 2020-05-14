package com.tokopedia.flight.detail.util;

import androidx.annotation.DrawableRes;

/**
 * Created by zulfikarrahman on 11/6/17.
 */

public class FlightAmenityIconUtil {

    @DrawableRes
    public static int getImageResource(String icon) {
        switch (icon){
            case "baggage":
                return com.tokopedia.flight.R.drawable.flight_ic_baggage;
            case "meal":
                return com.tokopedia.flight.R.drawable.flight_ic_meals;
            case "usb":
                return com.tokopedia.flight.R.drawable.flight_ic_power;
            case "wifi":
                return com.tokopedia.flight.R.drawable.flight_ic_wifi_amenity;
            default:
                return com.tokopedia.flight.R.drawable.flight_ic_baggage;
        }
    }
}
