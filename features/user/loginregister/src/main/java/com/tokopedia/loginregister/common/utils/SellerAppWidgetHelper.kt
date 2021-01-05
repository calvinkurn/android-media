package com.tokopedia.loginregister.common.utils

import android.content.Context
import android.content.Intent
import com.tokopedia.config.GlobalConfig
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 23/12/20
 */

object SellerAppWidgetHelper {

    private const val GET_ALL_APP_WIDGET_DATA = "com.tokopedia.sellerappwidget.GET_ALL_APP_WIDGET_DATA"

    fun fetchSellerAppWidgetData(context: Context?) {
        if (!GlobalConfig.isSellerApp()) return

        try {
            context?.let {
                val intent = Intent()
                intent.action = GET_ALL_APP_WIDGET_DATA
                intent.setPackage(it.packageName)
                it.sendBroadcast(intent)
            }
        } catch (e: Exception) {
            Timber.i(e)
        }
    }
}