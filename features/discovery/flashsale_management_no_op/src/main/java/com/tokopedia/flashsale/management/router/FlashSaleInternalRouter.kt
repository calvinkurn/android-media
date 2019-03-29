package com.tokopedia.flashsale.management.router

import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * Created by hendry on 23/11/18.
 */
class FlashSaleInternalRouter {
    companion object {
        @JvmStatic
        fun getFlashSaleDashboardActivity(context: Context): Intent {
            Toast.makeText(context, "Go To Flash Sale", Toast.LENGTH_SHORT).show()
            val startMain = Intent(Intent.ACTION_MAIN)
            startMain.addCategory(Intent.CATEGORY_HOME)
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return startMain
        }
    }
}