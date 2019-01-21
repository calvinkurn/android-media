package com.tokopedia.expresscheckout.router

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.tokopedia.logisticdata.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequestParam

interface ExpressCheckoutRouter {

    fun getExpressCheckoutIntent(activity: Activity, atcRequestParam: AtcRequestParam): Intent

    fun getGeolocationIntent(context: Context, locationPass: LocationPass): Intent

}