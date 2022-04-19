package com.tokopedia.logisticCommon.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import rx.Emitter
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

const val REVERSE_GEOCODE_DELAY = 1000L
const val AUTOCOMPLETE_DELAY = 500L

fun String.toKilometers(): String {
    var number = 0.0
    try {
        number = this.trim().toDouble()
    } catch (e: NumberFormatException) {
        e.printStackTrace()
    }
    return String.format("%.1f km", number)
}

fun getLatLng(lat: String, long: String): LatLng {
    return getLatLng(lat.toDoubleOrNull() ?: 0.0, long.toDoubleOrNull() ?: 0.0)
}

fun getLatLng(lat: Double, long: Double): LatLng = LatLng(lat, long)

fun bitmapDescriptorFromVector(context: Context, @DrawableRes vectorResId: Int)
        : BitmapDescriptor? {
    return ContextCompat.getDrawable(context, vectorResId)?.run {
        setBounds(0, 0, intrinsicWidth, intrinsicHeight)
        val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
        draw(Canvas(bitmap))
        BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}

fun rxPinPoint(maps: GoogleMap): Observable<Boolean> =
        Observable.create({ emitter: Emitter<Boolean> ->
            maps.setOnCameraMoveListener {
                emitter.onNext(true)
            }
        }, Emitter.BackpressureMode.LATEST)
                .debounce(REVERSE_GEOCODE_DELAY, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())

fun rxEditText(et: EditText): Observable<String> =
        Observable.create({ emitter: Emitter<String> ->
            et.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    emitter.onNext(s?.toString() ?: "")
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // no op
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // no op
                }
            })
        }, Emitter.BackpressureMode.LATEST)
                .debounce(AUTOCOMPLETE_DELAY, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

fun Subscription.toCompositeSubs(subs: CompositeSubscription) {
    subs.add(this)
}
