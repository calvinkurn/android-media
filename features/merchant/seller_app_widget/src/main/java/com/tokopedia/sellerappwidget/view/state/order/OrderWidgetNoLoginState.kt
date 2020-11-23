package com.tokopedia.sellerappwidget.view.state.order

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.Utils

/**
 * Created By @ilhamsuaib on 18/11/20
 */

object OrderWidgetNoLoginState {

    fun setupNoLoginState(context: Context, awm: AppWidgetManager, ids: IntArray) {
        val remoteView = AppWidgetHelper.getOrderWidgetRemoteView(context)
        with(remoteView) {
            ids.forEach {
                OrderWidgetStateHelper.updateViewNoLogin(this)
                Utils.loadImageIntoAppWidget(context, this, R.id.imgSawOrderNoLogin, Const.Images.ORDER_ON_EMPTY, it)

                //setup on login click event
                val loginIntent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
                val loginPendingIntent = PendingIntent.getActivity(context, 0, loginIntent, 0)
                setOnClickPendingIntent(R.id.tvSawOrderLoginNow, loginPendingIntent)

                awm.updateAppWidget(it, this)
            }
        }
    }
}