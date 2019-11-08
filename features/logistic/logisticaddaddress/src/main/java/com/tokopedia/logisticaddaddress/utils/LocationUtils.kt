package com.tokopedia.logisticaddaddress.utils

import com.google.android.gms.maps.model.LatLng

internal fun String.toKilometers(): String {
    var number = 0.0
    try {
        number = this.trim().toDouble()
    } catch (e: NumberFormatException) {
        e.printStackTrace()
    }
    return String.format("%.1f km", number)
}

internal fun getLatLng(lat: String, long: String): LatLng {
    return getLatLng(lat.toDoubleOrNull() ?: 0.0, long.toDoubleOrNull() ?: 0.0)
}

internal fun getLatLng(lat: Double, long: Double): LatLng = LatLng(lat, long)