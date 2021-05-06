package com.tokopedia.sellerappwidget.view.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.sellerappwidget.analytics.AppWidgetTracking
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.WidgetSize
import com.tokopedia.sellerappwidget.view.executor.GetOrderExecutor
import com.tokopedia.sellerappwidget.view.model.OrderItemUiModel
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.state.order.*
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 16/11/20
 */

class OrderAppWidget : AppWidgetProvider() {

    private var userSession: UserSessionInterface? = null

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        initUserSession(context)
        userSession?.let {
            if (it.isLoggedIn) {
                GetOrderExecutor.run(context, DEFAULT_ORDER_STATUS_ID, true)
            } else {
                OrderWidgetNoLoginState.setupNoLoginState(context, appWidgetManager, appWidgetIds)
            }
        }
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
            Const.Action.REFRESH -> refreshWidget(context)
            Const.Action.ITEM_CLICK -> onOrderItemClick(context, intent)
            Const.Action.SWITCH_ORDER -> switchOrder(context, intent)
            Const.Action.OPEN_APPLINK -> openAppLink(context, intent)
        }

        if (intent.action != AppWidgetManager.ACTION_APPWIDGET_UPDATE
                || intent.action != AppWidgetManager.ACTION_APPWIDGET_DISABLED) {
            AppWidgetHelper.setOrderAppWidgetEnabled(context, true)
        }

        super.onReceive(context, intent)
    }

    override fun onAppWidgetOptionsChanged(context: Context, appWidgetManager: AppWidgetManager?, appWidgetId: Int, newOptions: Bundle?) {
        initUserSession(context)
        userSession?.let {
            if (!it.isLoggedIn) {
                val awm = AppWidgetManager.getInstance(context)
                val ids = AppWidgetHelper.getAppWidgetIds<OrderAppWidget>(context, awm)
                OrderWidgetNoLoginState.setupNoLoginState(context, awm, ids)
                super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
                return
            }
        }
        GetOrderExecutor.run(context, DEFAULT_ORDER_STATUS_ID, true)
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onEnabled(context: Context) {
        val cacheHandler = AppWidgetHelper.getCacheHandler(context)
        cacheHandler.putBoolean(Const.SharedPrefKey.ORDER_WIDGET_ENABLED, true)
        cacheHandler.applyEditor()
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        val cacheHandler = AppWidgetHelper.getCacheHandler(context)
        cacheHandler.putBoolean(Const.SharedPrefKey.ORDER_WIDGET_ENABLED, false)
        cacheHandler.applyEditor()
        super.onDisabled(context)
    }

    private fun initUserSession(context: Context) {
        if (userSession == null) {
            userSession = UserSession(context)
        }
    }

    private fun openAppLink(context: Context, intent: Intent) {
        val appLink = intent.data?.toString().orEmpty()
        val appWidgetTracking = AppWidgetTracking.getInstance(context)
        val isSmallWidget = intent.getBundleExtra(Const.Extra.BUNDLE)
                ?.getString(Const.Extra.WIDGET_SIZE) == WidgetSize.SMALL
        when (appLink) {
            ApplinkConstInternalSellerapp.SELLER_HOME -> {
                appWidgetTracking.sendEventClickSellerIconOrderWidget()
            }
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP -> {
                if (isSmallWidget) {
                    appWidgetTracking.sendEventClickSmallReadyToShipOrderWidget()
                } else {
                    appWidgetTracking.sendEventClickShopNameOrderWidget()
                }
            }
            ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER -> {
                if (isSmallWidget) {
                    appWidgetTracking.sendEventClickSmallNewOrderWidget()
                } else {
                    appWidgetTracking.sendEventClickShopNameOrderWidget()
                }
            }
            ApplinkConst.LOGIN -> {
                appWidgetTracking.sendEventClickLoginNowOrderWidget()
            }
            ApplinkConst.SellerApp.CENTRALIZED_PROMO -> {
                appWidgetTracking.sendEventClickCheckNowOrderWidget()
            }
        }

        AppWidgetHelper.openAppLink(context, intent)
    }

    private fun switchOrder(context: Context, intent: Intent) {
        val orderStatusId = intent.getIntExtra(Const.Extra.ORDER_STATUS_ID, DEFAULT_ORDER_STATUS_ID)
        val order: OrderUiModel? = intent.getParcelableExtra(Const.Extra.SELLER_ORDER)
        order?.let {
            setOnSuccess(context, it, orderStatusId)
        }

        AppWidgetTracking.getInstance(context)
                .sendEventClickSwitchButtonOrderWidget()
    }

    private fun refreshWidget(context: Context) {
        val awm = AppWidgetManager.getInstance(context)
        val appWidgetIds = awm.getAppWidgetIds(ComponentName(context, OrderAppWidget::class.java))
        val remoteViews = AppWidgetHelper.getOrderWidgetRemoteView(context)
        initUserSession(context)
        userSession?.let {
            if (!it.isLoggedIn) {
                OrderWidgetNoLoginState.setupNoLoginState(context, awm, appWidgetIds)
                return
            }
        }
        appWidgetIds.forEach {
            OrderWidgetStateHelper.updateViewOnLoading(remoteViews)
            OrderWidgetLoadingState.setupLoadingState(context, awm, remoteViews, it)
            awm.updateAppWidget(it, remoteViews)
        }

        AppWidgetTracking.getInstance(context)
                .sendEventClickRefreshButtonOrderWidget()

        val cacheHandler = AppWidgetHelper.getCacheHandler(context)
        val lastSelectedOrderStatusId = cacheHandler.getInt(Const.SharedPrefKey.LAST_SELECTED_ORDER_TYPE, DEFAULT_ORDER_STATUS_ID)
        GetOrderExecutor.run(context, lastSelectedOrderStatusId, true)
    }

    private fun onOrderItemClick(context: Context, intent: Intent) {
        val bundle = intent.getBundleExtra(Const.Extra.BUNDLE)
        val orderItem: OrderItemUiModel? = bundle?.getParcelable(Const.Extra.ORDER_ITEM)
        val orderId = orderItem?.orderId ?: "0"
        val orderDetailIntent = RouteManager.getIntent(context, ApplinkConstInternalOrder.ORDER_DETAIL, orderId).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        AppWidgetTracking.getInstance(context)
                .sendEventClickItemOrderWidget()

        context.startActivity(orderDetailIntent)
    }

    companion object {

        @JvmField
        val DEFAULT_ORDER_STATUS_ID = Const.OrderStatusId.NEW_ORDER

        fun setOnSuccess(context: Context, order: OrderUiModel, orderStatusId: Int) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val widgetIds = AppWidgetHelper.getAppWidgetIds<OrderAppWidget>(context, appWidgetManager)

            val userSession: UserSessionInterface = UserSession(context)

            if (!getIsUserLoggedIn(context, appWidgetManager, userSession, widgetIds)) {
                return
            }

            val remoteViews = AppWidgetHelper.getOrderWidgetRemoteView(context)

            widgetIds.forEach { widgetId ->
                when {
                    order.orders.isNullOrEmpty() -> OrderWidgetEmptyState.setupEmptyState(context, remoteViews, widgetId)
                    else -> {
                        OrderWidgetSuccessState.setupSuccessState(context, remoteViews, userSession, order, orderStatusId, widgetId)
                    }
                }

                OrderWidgetStateHelper.setLastUpdated(context, remoteViews)
                appWidgetManager.updateAppWidget(widgetId, remoteViews)
            }
        }

        fun setOnError(context: Context) {
            val awm = AppWidgetManager.getInstance(context)
            val widgetIds = AppWidgetHelper.getAppWidgetIds<OrderAppWidget>(context, awm)

            val userSession: UserSessionInterface = UserSession(context)

            if (!getIsUserLoggedIn(context, awm, userSession, widgetIds)) {
                return
            }

            val remoteViews = AppWidgetHelper.getOrderWidgetRemoteView(context)

            widgetIds.forEach {
                OrderWidgetErrorState.setupErrorState(context, awm, remoteViews, it)
                awm.updateAppWidget(it, remoteViews)
            }
        }

        fun showNoLoginState(context: Context) {
            val awm = AppWidgetManager.getInstance(context)
            val widgetIds = AppWidgetHelper.getAppWidgetIds<OrderAppWidget>(context, awm)
            OrderWidgetNoLoginState.setupNoLoginState(context, awm, widgetIds)
        }

        private fun getIsUserLoggedIn(context: Context, awm: AppWidgetManager, userSession: UserSessionInterface, widgetIds: IntArray): Boolean {
            if (!userSession.isLoggedIn) {
                OrderWidgetNoLoginState.setupNoLoginState(context, awm, widgetIds)
            }
            return userSession.isLoggedIn
        }
    }
}