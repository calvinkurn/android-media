package com.tokopedia.sellerappwidget.view.appwidget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.tokopedia.kotlin.extensions.view.pxToDp
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.Utils
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.remoteview.OrderWidgetRemoteViewService
import com.tokopedia.sellerappwidget.view.service.GetOrderService
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created By @ilhamsuaib on 16/11/20
 */

class OrderAppWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        GetOrderService.startService(context)
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val awm = AppWidgetManager.getInstance(context)
                val ids = awm.getAppWidgetIds(ComponentName(context, OrderAppWidget::class.java))
                if (ids != null && ids.isNotEmpty()) {
                    onUpdate(context, awm, ids)
                }
            }
            Const.Action.REFRESH -> refreshWidget(context, intent)
            Const.Action.ITEM_CLICK -> onOrderItemClick(context, intent)
        }
        super.onReceive(context, intent)
    }

    override fun onAppWidgetOptionsChanged(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle?) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
    }

    private fun refreshWidget(context: Context, intent: Intent) {
        val awm = AppWidgetManager.getInstance(context)
        val appWidgetIds = awm.getAppWidgetIds(ComponentName(context, OrderAppWidget::class.java))
        appWidgetIds.forEach {
            val remoteViews = RemoteViews(context.packageName, R.layout.saw_app_widget_order)
            updateViewOnLoading(remoteViews)
            awm.updateAppWidget(it, remoteViews)
        }
        GetOrderService.startService(context)
    }

    private fun onOrderItemClick(context: Context, intent: Intent) {
        val bundle = intent.getBundleExtra(Const.Extra.BUNDLE)
        val orderItem: OrderUiModel? = bundle?.getParcelable(Const.Extra.ORDER_ITEM)
        println("AppWidget : onOrderItemClick -> ${orderItem?.product?.productName}")
    }

    companion object {

        fun showSuccessState(context: Context, appWidgetManager: AppWidgetManager, widgetIds: Array<Int>, widgetItems: List<OrderUiModel>) {
            widgetIds.forEach { widgetId ->
                val userSession: UserSessionInterface = UserSession(context)
                val remoteViews = RemoteViews(context.packageName, R.layout.saw_app_widget_order)

                with(remoteViews) {
                    updateViewOnSuccess(this)

                    val randomNumber = Math.random().toInt() * 1000
                    val items = if (!widgetItems.isNullOrEmpty()) ArrayList(widgetItems) else null

                    //setup widget list view
                    val intent = Intent(context, OrderWidgetRemoteViewService::class.java).apply {
                        //need unique widget id to notify data set changed
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId + randomNumber)
                        data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                        putExtra(Const.Extra.BUNDLE, Bundle().also {
                            it.putParcelableArrayList(Const.Extra.ORDER_ITEMS, items)
                        })
                    }
                    setRemoteAdapter(R.id.lvSawOrderList, intent)
                    setEmptyView(R.id.containerSawOrderListSuccess, R.id.containerSawOrderListEmpty)

                    //setup list view item click event
                    val itemIntent = Intent(context, OrderWidgetRemoteViewService::class.java).apply {
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
                    remoteViews.setOnClickPendingIntent(R.id.btnSawOrderRefresh, reloadPendingIntent)


                    val totalOrderFmt = "${widgetItems.size} Pesanan Baru"
                    val updatedFmt = Utils.formatDate(Date(), "hh:mm")
                    setTextViewText(R.id.tvSawOrderTotalOrder, totalOrderFmt)
                    setTextViewText(R.id.tvSawOrderShopName, userSession.shopName)
                    setTextViewText(R.id.tvSawOrderUpdated, "Diperbarui $updatedFmt")
                    setInt(R.id.icSawOrderMoreOrder, "setImageResource", R.drawable.ic_saw_chevron_right)
                    setInt(R.id.btnSawOrderRefresh, "setImageResource", R.drawable.ic_saw_refresh)


                    Utils.getAppIcon(context)?.let {
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
                    }
                }

                appWidgetManager.updateAppWidget(widgetId, remoteViews)
            }
        }

        fun showEmptyState(context: Context, appWidgetManager: AppWidgetManager, widgetIds: Array<Int>, widgetItems: List<OrderUiModel>) {

        }

        fun updateViewOnSuccess(remoteViews: RemoteViews) {
            with(remoteViews) {
                setInt(R.id.containerSawOrderListLoading, "setVisibility", View.GONE)
                setInt(R.id.containerSawOrderListError, "setVisibility", View.GONE)
                setInt(R.id.containerSawOrderListSuccess, "setVisibility", View.VISIBLE)
            }
        }

        fun updateViewOnLoading(remoteViews: RemoteViews) {
            with(remoteViews) {
                setInt(R.id.containerSawOrderListEmpty, "setVisibility", View.GONE)
                setInt(R.id.containerSawOrderListError, "setVisibility", View.GONE)
                setInt(R.id.containerSawOrderListSuccess, "setVisibility", View.GONE)
                setInt(R.id.containerSawOrderListLoading, "setVisibility", View.VISIBLE)
            }
        }

        fun updateViewOnError(remoteViews: RemoteViews) {
            with(remoteViews) {
                setInt(R.id.containerSawOrderListEmpty, "setVisibility", View.GONE)
                setInt(R.id.containerSawOrderListSuccess, "setVisibility", View.GONE)
                setInt(R.id.containerSawOrderListLoading, "setVisibility", View.GONE)
                setInt(R.id.containerSawOrderListError, "setVisibility", View.VISIBLE)
            }
        }
    }
}