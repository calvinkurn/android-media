package com.tokopedia.normalcheckout.router

import android.content.Context
import android.content.Intent
import com.tokopedia.transaction.common.sharedata.ShipmentFormRequest

/**
 * Created by Irfan Khoirul on 12/12/18.
 */

interface NormalCheckoutRouter {

    fun getCheckoutIntent(context: Context, shipmentFormRequest: ShipmentFormRequest): Intent
    fun getCheckoutIntent(context: Context, deviceid: String): Intent
    fun getCartIntent(context: Context): Intent

}