package com.tokopedia.expresscheckout.router

import android.app.Activity
import android.content.Intent
import com.tokopedia.transaction.common.data.expresscheckout.AtcRequestParam

interface ExpressCheckoutRouter {
    fun getExpressCheckoutIntent(activity: Activity, atcRequestParam: AtcRequestParam): Intent
}