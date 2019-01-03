package com.tokopedia.expresscheckout.router

import android.app.Activity
import android.content.Intent
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequest

interface ExpressCheckoutRouter {
    fun getExpressCheckoutIntent(activity: Activity, atcRequest: AtcRequest): Intent
}