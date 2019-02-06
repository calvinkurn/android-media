package com.tokopedia.topads.dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object TopAdsDashboardInternalRouter{

    @JvmStatic
    fun getTopAdsdashboardIntent(context: Context): Intent {
        Toast.makeText(context, "TopAds Dashboard", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.getTopAdsdashboardIntent.com"))
        return intent
    }

    @JvmStatic
    fun getTopAdsAddCreditIntent(context: Context): Intent {
        Toast.makeText(context, "Add TopAds Credit", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.getTopAdsAddCreditIntent.com"))
        return intent
    }
}
