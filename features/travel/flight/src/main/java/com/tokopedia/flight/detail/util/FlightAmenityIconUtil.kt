package com.tokopedia.flight.detail.util

import androidx.annotation.DrawableRes
import com.tokopedia.flight.R

/**
 * Created by zulfikarrahman on 11/6/17.
 */
object FlightAmenityIconUtil {
    @JvmStatic
    @DrawableRes
    fun getImageResource(icon: String?): Int {
        return when (icon) {
            "baggage" -> R.drawable.flight_ic_baggage
            "meal" -> R.drawable.flight_ic_meals
            "usb" -> R.drawable.flight_ic_power
            "wifi" -> R.drawable.flight_ic_wifi_amenity
            else -> R.drawable.flight_ic_baggage
        }
    }
}