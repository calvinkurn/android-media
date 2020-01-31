package com.tokopedia.sellerhomedrawer.presentation.view.drawer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tokopedia.sellerhomedrawer.domain.service.SellerDrawerGetNotificationService

abstract class BaseSellerReceiverDrawerActivity: SellerDrawerPresenterActivity() {

    val drawerGetNotificationReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == SellerDrawerGetNotificationService.BROADCAST_GET_NOTIFICATION
                    && intent.getBooleanExtra(SellerDrawerGetNotificationService.GET_NOTIFICATION_SUCCESS, false))
                updateDrawerData()
        }
    }

    override fun setDrawerPosition(): Int = 0

}