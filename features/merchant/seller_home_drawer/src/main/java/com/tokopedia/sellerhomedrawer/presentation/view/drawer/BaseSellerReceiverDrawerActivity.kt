package com.tokopedia.sellerhomedrawer.presentation.view.drawer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        registerBroadcastReceiver()
        startDrawerGetNotificationServiceOnResume()
    }

    override fun onPause() {
        super.onPause()
        unregisterBroadcastReceiver()
    }

    override fun updateDrawerData() {
        super.updateDrawerData()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    protected open fun startDrawerGetNotificationServiceOnResume() {
        SellerDrawerGetNotificationService.startService(this, true)
    }

    private fun registerBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(SellerDrawerGetNotificationService.BROADCAST_GET_NOTIFICATION)
        LocalBroadcastManager.getInstance(this).registerReceiver(drawerGetNotificationReceiver, intentFilter)
    }

    private fun unregisterBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(drawerGetNotificationReceiver)
    }

}