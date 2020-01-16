package com.tokopedia.sellerhomedrawer.view.dashboard

import android.app.Activity
import android.content.Context
import android.content.Intent

class SellerDashboardActivity: Activity() {

    companion object {
        @JvmStatic
        fun createInstance(context: Context) = Intent(context, SellerDashboardActivity::class.java)
    }
}