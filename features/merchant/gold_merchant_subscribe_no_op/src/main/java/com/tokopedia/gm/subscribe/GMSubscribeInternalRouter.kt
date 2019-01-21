package com.tokopedia.gm.subscribe

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

object GMSubscribeInternalRouter {

    @JvmStatic
    fun getGMMembershipIntent(context: Context): Intent {
        Toast.makeText(context, "Gold Merchant Membership", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.getGMMembershipIntent.com"))
        return intent
    }

    @JvmStatic
    fun getGMSubscribeHomeIntent(context: Context): Intent {
        Toast.makeText(context, "Gold Merchant Home", Toast.LENGTH_SHORT).show()
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse("http://www.getGMSubscribeHomeIntent.com"))
        return intent
    }
}