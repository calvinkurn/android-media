package com.tokopedia.flashsale.management.router

import android.content.Context
import android.content.Intent

/**
 * Created by hendry on 23/11/18.
 */
interface FlashSaleRouter {

    fun getFlashSaleDashboardIntent(context: Context): Intent
}