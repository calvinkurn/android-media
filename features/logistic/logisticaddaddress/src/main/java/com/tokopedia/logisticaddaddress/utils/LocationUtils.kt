package com.tokopedia.logisticaddaddress.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
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

internal fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorResId: Int)
        : BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.run {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}