package com.tokopedia.logisticCommon.util

import android.content.Context
import android.view.View
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.tokopedia.unifycomponents.Toaster

object MapsAvailabilityHelper {

    fun isMapsAvailable(context: Context): Boolean {
        val availability = GoogleApiAvailability.getInstance()
        val resultCode = availability.isGooglePlayServicesAvailable(context)
        return resultCode == ConnectionResult.SUCCESS
    }

    fun onMapsAvailableState(
        view: View,
        onUnAvailable: (() -> Unit)? = null,
        onAvailable: () -> Unit
    ) {
        if (isMapsAvailable(view.context)) {
            onAvailable()
        } else {
            Toaster.build(
                view,
                view.context.getString(com.tokopedia.logisticCommon.R.string.gms_unavailable_error),
                Toaster.LENGTH_SHORT,
                Toaster.TYPE_ERROR
            ).show()
            onUnAvailable?.invoke()
        }
    }
}
