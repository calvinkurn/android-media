package com.tokopedia.logisticaddaddress.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import rx.Emitter
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

const val REVERSE_GEOCODE_DELAY = 1000L
import com.tokopedia.logisticaddaddress.features.dropoff_picker.model.DropoffNearbyModel

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

internal fun rxPinPoint(maps: GoogleMap): Observable<Boolean> =
        Observable.create({ emitter: Emitter<Boolean> ->
            maps.setOnCameraMoveListener {
                emitter.onNext(true)
            }
        }, Emitter.BackpressureMode.LATEST)
                .debounce(REVERSE_GEOCODE_DELAY, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())

internal fun DropoffNearbyModel.getDescription(): String =
        "${this.districtName}, ${this.cityName}, ${this.provinceName}"