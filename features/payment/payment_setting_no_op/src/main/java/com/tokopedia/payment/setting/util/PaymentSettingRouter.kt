package com.tokopedia.payment.setting.util

import android.content.Context
import android.content.Intent

interface PaymentSettingRouter {
    fun getResourceUrlAssetPayment() : String
    fun getProfileSettingIntent(context: Context): Intent
}