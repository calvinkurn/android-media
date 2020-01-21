package com.tokopedia.sellerhomedrawer.drawer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.tokopedia.sellerhomedrawer.service.SellerDrawerGetNotificationService

open class BaseSellerReceiverDrawerActivity<T>: SellerDrawerPresenterActivity<T>() {

    val drawerGetNotificationReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == SellerDrawerGetNotificationService.BROADCAST_GET_NOTIFICATION
                    && intent.getBooleanExtra(SellerDrawerGetNotificationService.GET_NOTIFICATION_SUCCESS, false))
                updateDrawerData()
        }
    }

    override fun getLayoutId(): Int {
        return 0
    }

    override fun initVar() {

    }

    override fun initView() {

    }

    override fun initialPresenter() {

    }

    override fun setActionVar() {

    }

    override fun setViewListener() {

    }

    override fun setupBundlePass(extras: Bundle?) {

    }

    override fun setupURIPass(data: Uri?) {

    }

    override fun updateDrawerData() {
        super.updateDrawerData()
    }

    override fun setDrawerPosition(): Int = 0

}