package com.tokopedia.sellerhomedrawer.presentation.view.drawer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.MenuItem
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.tokopedia.sellerhomedrawer.domain.service.SellerDrawerGetNotificationService

abstract class BaseSellerReceiverDrawerActivity: SellerDrawerPresenterActivity() {

    private val drawerGetNotificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val intentSuccessGetNotification =
                    intent?.action == SellerDrawerGetNotificationService.BROADCAST_GET_NOTIFICATION && intent.getBooleanExtra(SellerDrawerGetNotificationService.GET_NOTIFICATION_SUCCESS, false)
            val intentUpdateNotificationData = intent?.action == SellerDrawerGetNotificationService.UPDATE_NOTIFICATON_DATA
            when {
                context == null || intent?.action == null ->
                    return
                intentSuccessGetNotification ->
                    updateDrawerData()
                intentUpdateNotificationData ->
                    startDrawerGetNotificationService()
            }
        }
    }

    override fun setDrawerPosition(): Int = 0

    override fun onResume() {
        super.onResume()
        registerBroadcastReceiver()
        startDrawerGetNotificationService()
    }

    override fun onPause() {
        super.onPause()
        unregisterBroadcastReceiver()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home)
            finish()
        return super.onOptionsItemSelected(item)
    }

    protected open fun startDrawerGetNotificationService() {
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