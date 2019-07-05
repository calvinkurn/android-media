package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure.getInt
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.logisticaddaddress.R

/**
 * Created by fwidjaja on 2019-06-22.
 */
object AddNewAddressUtils {

    @JvmStatic
    fun generateLatLng(latitude: Double?, longitude: Double?): LatLng {
        return latitude?.let { longitude?.let { it1 -> LatLng(it, it1) } }!!
    }

    @JvmStatic
    fun showToastError(message: String, view: View, activity: Activity) {
        var msg = message
        if (message.isEmpty()) {
            msg = activity.getString(R.string.default_request_error_unknown)
        }
        val snackbar = view.let { Snackbar.make(it, msg, BaseToaster.LENGTH_SHORT) }
        val snackbarTextView = snackbar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        val snackbarActionButton = snackbar.view.findViewById<Button>(android.support.design.R.id.snackbar_action)
        snackbar.view.background = ContextCompat.getDrawable(activity, com.tokopedia.design.R.drawable.bg_snackbar_error)
        snackbarTextView?.setTextColor(ContextCompat.getColor(activity, R.color.font_black_secondary_54))
        snackbarActionButton?.setTextColor(ContextCompat.getColor(activity, R.color.font_black_primary_70))
        snackbarTextView?.maxLines = 5
        snackbar.setAction(activity.getString(R.string.label_action_snackbar_close)) { }.show()
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.KITKAT)
    fun isLocationEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            val mode = getInt(context.contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF)
            mode != Settings.Secure.LOCATION_MODE_OFF

        }
    }

    @JvmStatic
    fun toDp(number: Int): Int {
        return (number * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
    }

    @JvmStatic
    fun onGlobalLayoutListener(activity: Activity?, view: View?): ViewTreeObserver.OnGlobalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val r = Rect()
        //r will be populated with the coordinates of your view that area still visible.
        activity?.window?.decorView?.getWindowVisibleDisplayFrame(r)

        //get screen height and calculate the difference with the useable area from the r
        val height = activity?.window?.decorView?.context?.resources?.displayMetrics?.heightPixels
        val diff = height?.minus(r.bottom)

        //if it could be a keyboard add the padding to the view
        if (diff != 0) {
            // if the use-able screen height differs from the total screen height we assume that it shows a keyboard now
            //check if the padding is 0 (if yes set the padding for the keyboard)
            if (view?.paddingBottom !== diff) {
                //set the padding of the contentView for the keyboard
                if (diff != null) {
                    view?.setPadding(0, 0, 0, diff)
                }
            }
        } else {
            //check if the padding is != 0 (if yes reset the padding)
            if (view?.paddingBottom !== 0) {
                //reset the padding of the contentView
                view?.setPadding(0, 0, 0, 0)
            }
        }
    }

    @JvmStatic
    fun isGpsEnabled(context: Context?): Boolean {
        var isGpsOn = false
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val mSettingsClient = LocationServices.getSettingsClient(it)

            val locationRequest = LocationRequest.create()
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 10 * 1000
            locationRequest.fastestInterval = 2 * 1000
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            val mLocationSettingsRequest = builder.build()
            builder.setAlwaysShow(true)

            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                isGpsOn = true
            } else {
                mSettingsClient
                        .checkLocationSettings(mLocationSettingsRequest)
                        .addOnSuccessListener(context as Activity) {
                            isGpsOn = true
                        }
            }

            isGpsOn = isLocationEnabled(it) && isGpsOn
        }
        return isGpsOn
    }
}