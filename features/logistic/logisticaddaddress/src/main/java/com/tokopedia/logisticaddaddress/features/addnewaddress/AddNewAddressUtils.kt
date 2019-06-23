package com.tokopedia.logisticaddaddress.features.addnewaddress

import android.app.Activity
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.logisticaddaddress.R
import kotlinx.android.synthetic.main.fragment_add_edit_new_address.*

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
}