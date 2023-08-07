package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure.getInt
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DENIED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_DONT_ASK_AGAIN
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_GRANTED
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant.PERMISSION_NOT_DEFINED
import kotlin.math.abs


/**
 * Created by fwidjaja on 2019-06-22.
 */
object AddNewAddressUtils {

    private const val MAX_LINES = 5
    private const val LOCATION_REQUEST_INTERVAL = 10000L
    private const val COORDINATE_THRESHOLD = 0.00001
    private const val LOCATION_REQUEST_FASTEST_INTERVAL = 2000L

    @JvmStatic
    fun showToastError(message: String, view: View, activity: Activity) {
        var msg = message
        if (message.isEmpty()) {
            msg = activity.getString(com.tokopedia.abstraction.R.string.default_request_error_unknown)
        }
        val snackbar = view.let { Snackbar.make(it, msg, Snackbar.LENGTH_SHORT) }
        val snackbarTextView = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        val snackbarActionButton = snackbar.view.findViewById<Button>(com.google.android.material.R.id.snackbar_action)
        snackbar.view.background = ContextCompat.getDrawable(activity, R.drawable.bg_snackbar_error)
        snackbarTextView?.setTextColor(ContextCompat.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_NN950_44))
        snackbarActionButton?.setTextColor(ContextCompat.getColor(activity, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68))
        snackbarTextView?.maxLines = MAX_LINES
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
    fun isGpsEnabled(context: Context?): Boolean {
        var isGpsOn = false
        context?.let {
            val locationManager = it.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val mSettingsClient = LocationServices.getSettingsClient(it)


            val mLocationSettingsRequest = requestLocationBuilder()?.build()
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

    private fun requestLocationBuilder(): LocationSettingsRequest.Builder? {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(getLocationRequest())
        builder.setAlwaysShow(true)
        return builder
    }

    @JvmStatic
    fun getLocationRequest() : LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = LOCATION_REQUEST_INTERVAL
        locationRequest.fastestInterval = LOCATION_REQUEST_FASTEST_INTERVAL
        return locationRequest
    }

    fun hideKeyboard(et: EditText, context: Context?) {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) ?: return
        (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(et.windowToken, 0)
    }

    @JvmStatic
    fun showKeyboard(context: Context?) {
        val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    fun hasDefaultCoordinate(lat: Double, long: Double, exact: Boolean = false): Boolean {
        val threshold = COORDINATE_THRESHOLD
        val diffLat = lat - AddressConstants.DEFAULT_LAT
        val diffLong = long - AddressConstants.DEFAULT_LONG

        return if (exact) (diffLat == 0.0 && diffLong == 0.0)
        else (abs(diffLat) < threshold && abs(diffLong) < threshold)
    }

    fun getPermissionStateFromResult(activity: Activity, context: Context, permissions: Array<out String>) : Int {
        var state = PERMISSION_NOT_DEFINED
        for (permission in permissions) {
            state = when {
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED -> {
                    PERMISSION_GRANTED
                }
                ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)  -> {
                    PERMISSION_DENIED
                }
                else -> {
                    PERMISSION_DONT_ASK_AGAIN
                }
            }
        }
        return state
    }
}