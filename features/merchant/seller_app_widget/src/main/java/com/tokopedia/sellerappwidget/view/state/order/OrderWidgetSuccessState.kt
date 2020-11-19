package com.tokopedia.sellerappwidget.view.state.order

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.Utils
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.remoteview.OrderWidgetRemoteViewService
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created By @ilhamsuaib on 18/11/20
 */

object OrderWidgetSuccessState {

    fun setupSuccessState(context: Context, remoteViews: RemoteViews, userSession: UserSessionInterface, widgetItems: List<OrderUiModel>, orderStatusId: Int, widgetId: Int) {
        with(remoteViews) {
            OrderWidgetStateHelper.updateViewOnSuccess(this)

            //setup widget list view
            val orderItemsByType = ArrayList(widgetItems.filter { it.statusId == orderStatusId })
            val randomNumber = (Math.random() * 10000).toInt()

            val intent = Intent(context, OrderWidgetRemoteViewService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId + randomNumber)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                putExtra(Const.Extra.BUNDLE, Bundle().also {
                    it.putParcelableArrayList(Const.Extra.ORDER_ITEMS, orderItemsByType)
                })
            }
            setRemoteAdapter(R.id.lvSawOrderList, intent)
            setEmptyView(R.id.lvSawOrderList, R.id.containerSawOrderListEmpty)

            //setup list view item click event
            val itemIntent = Intent(context, OrderAppWidget::class.java).apply {
                action = Const.Action.ITEM_CLICK
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            val itemPendingIntent = PendingIntent.getBroadcast(context, 0, itemIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            setPendingIntentTemplate(R.id.lvSawOrderList, itemPendingIntent)

            //setup refresh button click event
            val reloadIntent = Intent(context, OrderAppWidget::class.java).apply {
                action = Const.Action.REFRESH
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
            }
            val reloadPendingIntent = PendingIntent.getBroadcast(context, 0, reloadIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            setOnClickPendingIntent(R.id.btnSawOrderRefresh, reloadPendingIntent)

            //setup switch button click event
            val mOrderStatusId = if (orderStatusId == Const.OrderStatusId.NEW_ORDER) {
                Const.OrderStatusId.READY_TO_SHIP
            } else {
                Const.OrderStatusId.NEW_ORDER
            }
            val switchIntent = Intent(context, OrderAppWidget::class.java).apply {
                action = Const.Action.SWITCH_ORDER
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                putExtra(Const.Extra.ORDER_STATUS_ID, mOrderStatusId)
                putParcelableArrayListExtra(Const.Extra.ORDER_ITEMS, ArrayList(widgetItems))
            }
            val switchPendingIntent = PendingIntent.getBroadcast(context, 0, switchIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            setOnClickPendingIntent(R.id.containerSawOrderSwitch, switchPendingIntent)

            val orderTypeStringRes = if (orderStatusId == Const.OrderStatusId.NEW_ORDER) {
                R.string.saw_new_order
            } else {
                R.string.saw_ready_to_ship
            }
            val totalOrderFmt = "${orderItemsByType.size} ${context.getString(orderTypeStringRes)}"
            setTextViewText(R.id.tvSawOrderTotalOrder, totalOrderFmt)
            setTextViewText(R.id.tvSawOrderShopName, userSession.shopName)
            setInt(R.id.icSawOrderMoreOrder, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_chevron_right)
            setInt(R.id.btnSawOrderRefresh, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_refresh)

            when (orderStatusId) {
                Const.OrderStatusId.NEW_ORDER -> {
                    setInt(R.id.imgSawOrderSwitchLeft, Const.Method.SET_VISIBILITY, View.GONE)
                    setInt(R.id.imgSawOrderSwitchRight, Const.Method.SET_VISIBILITY, View.VISIBLE)
                    setInt(R.id.imgSawOrderSwitchRight, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_arrow_right)
                    setTextViewText(R.id.tvSawOrderSwitch, context.getString(R.string.saw_ready_to_ship))
                }
                else -> {
                    setInt(R.id.imgSawOrderSwitchRight, Const.Method.SET_VISIBILITY, View.GONE)
                    setInt(R.id.imgSawOrderSwitchLeft, Const.Method.SET_VISIBILITY, View.VISIBLE)
                    setInt(R.id.imgSawOrderSwitchLeft, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_arrow_left)
                    setTextViewText(R.id.tvSawOrderSwitch, context.getString(R.string.saw_new_order))
                }
            }

            /*Utils.getAppIcon(context)?.let {
                val width = context.pxToDp(40).toInt()
                val radius = context.pxToDp(8).toInt()
                val builder = Glide.with(context)
                        .asBitmap()
                        .load(it)
                        .transform(CenterCrop(), RoundedCorners(radius))
                val futureTarget = builder.submit(width, width)

                try {
                    setImageViewBitmap(R.id.imgSawOrderItemProduct, futureTarget.get())
                } catch (e: Exception) {
                    Timber.i(e)
                }
            }*/
        }
    }


    fun setLastUpdated(context: Context) {
        val updatedFmt = Utils.formatDate(Date(), "hh:mm:ss")
        val updated = context.getString(R.string.saw_updated)

        val rv = AppWidgetHelper.getRemoteView(context, R.layout.saw_app_widget_order)
        val awm = AppWidgetManager.getInstance(context)
        val ids = AppWidgetHelper.getAppWidgetIds<OrderAppWidget>(context, awm)
        ids.forEach {
            rv.setTextViewText(R.id.tvSawOrderUpdated, "$updated $updatedFmt")
            awm.updateAppWidget(it, rv)
        }
    }
}