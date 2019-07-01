package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import android.provider.Settings.Secure.getInt
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
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
    fun scrollUpLayout(scrollViewLayout: ScrollView) {
        scrollViewLayout.postDelayed(Runnable {
            val lastChild = scrollViewLayout.getChildAt(scrollViewLayout.childCount - 1)
            val bottom = lastChild.bottom + scrollViewLayout.paddingBottom
            val sy = scrollViewLayout.scrollY
            val sh = scrollViewLayout.height
            val delta = bottom - (sy + sh)
            scrollViewLayout.smoothScrollBy(0, delta)
        }, 200)
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
}