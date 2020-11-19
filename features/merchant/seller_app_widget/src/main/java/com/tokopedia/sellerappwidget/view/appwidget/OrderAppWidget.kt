package com.tokopedia.sellerappwidget.view.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.service.GetOrderService
import com.tokopedia.sellerappwidget.view.state.order.OrderWidgetEmptyState
import com.tokopedia.sellerappwidget.view.state.order.OrderWidgetNoLoginState
import com.tokopedia.sellerappwidget.view.state.order.OrderWidgetStateHelper
import com.tokopedia.sellerappwidget.view.state.order.OrderWidgetSuccessState
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 16/11/20
 */

class OrderAppWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        GetOrderService.startService(context, DEFAULT_ORDER_STATUS_ID)
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val awm = AppWidgetManager.getInstance(context)
                val ids = AppWidgetHelper.getAppWidgetIds<OrderAppWidget>(context, awm)
                if (ids.isNotEmpty()) {
                    onUpdate(context, awm, ids)
                }
            }
            Const.Action.REFRESH -> refreshWidget(context, intent)
            Const.Action.ITEM_CLICK -> onOrderItemClick(context, intent)
            Const.Action.SWITCH_ORDER -> switchOrder(context, intent)
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

    private fun switchOrder(context: Context, intent: Intent) {
        val orderStatusId = intent.getIntExtra(Const.Extra.ORDER_STATUS_ID, DEFAULT_ORDER_STATUS_ID)
        val widgetItems: List<OrderUiModel> = intent.getParcelableArrayListExtra<OrderUiModel>(Const.Extra.ORDER_ITEMS).orEmpty()
        showSuccessState(context, ArrayList(widgetItems), orderStatusId)
    }

    private fun refreshWidget(context: Context, intent: Intent) {
        val awm = AppWidgetManager.getInstance(context)
        val appWidgetIds = awm.getAppWidgetIds(ComponentName(context, OrderAppWidget::class.java))
        appWidgetIds.forEach {
            val remoteViews = RemoteViews(context.packageName, R.layout.saw_app_widget_order)
            OrderWidgetStateHelper.updateViewOnLoading(remoteViews)
            awm.updateAppWidget(it, remoteViews)
        }
        GetOrderService.startService(context, DEFAULT_ORDER_STATUS_ID)
    }

    private fun onOrderItemClick(context: Context, intent: Intent) {
        val bundle = intent.getBundleExtra(Const.Extra.BUNDLE)
        val orderItem: OrderUiModel? = bundle?.getParcelable(Const.Extra.ORDER_ITEM)
        println("AppWidget : onOrderItemClick -> ${orderItem?.product?.productName}")
    }

    companion object {

        const val DEFAULT_ORDER_STATUS_ID = Const.OrderStatusId.NEW_ORDER

        fun showSuccessState(context: Context, widgetItems: List<OrderUiModel>, orderStatusId: Int) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = AppWidgetHelper.getAppWidgetIds<OrderAppWidget>(context, appWidgetManager)

            val userSession: UserSessionInterface = UserSession(context)

            widgetIds.forEach { widgetId ->
                val remoteViews = RemoteViews(context.packageName, R.layout.saw_app_widget_order)

                when {
                    !userSession.isLoggedIn -> OrderWidgetNoLoginState.setupNoLoginState()
                    widgetItems.isEmpty() -> OrderWidgetEmptyState.setupEmptyState()
                    else -> {
                        OrderWidgetSuccessState.setupSuccessState(context, remoteViews, userSession, widgetItems, orderStatusId, widgetId)
                    }
                }

                appWidgetManager.updateAppWidget(widgetId, remoteViews)
            }
        }
    }
}