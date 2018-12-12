package com.tokopedia.expresscheckout.router

import android.app.Activity
import android.content.Intent

interface ExpressCheckoutRouter {
    fun getExpressCheckoutIntent(activity: Activity): Intent
}