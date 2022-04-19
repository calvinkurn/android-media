package com.tokopedia.sellerappwidget.view.state.order

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.analytics.AppWidgetTracking
import com.tokopedia.sellerappwidget.common.*
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget
import com.tokopedia.sellerappwidget.view.model.OrderItemUiModel
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.remoteview.OrderWidgetRemoteViewService
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created By @ilhamsuaib on 18/11/20
 */

object OrderWidgetSuccessState {

    fun setupSuccessState(context: Context, remoteViews: RemoteViews, userSession: UserSessionInterface, order: OrderUiModel, orderStatusId: Int, widgetId: Int) {
        val awm = AppWidgetManager.getInstance(context)
        val option = awm.getAppWidgetOptions(widgetId)
        val widgetSize = AppWidgetHelper.getAppWidgetSize(option)
        when (widgetSize) {
            WidgetSize.SMALL -> showSmallWidgetSuccessState(context, remoteViews, userSession, order, widgetId)
            else -> showNormalWidgetSuccessState(context, remoteViews, userSession, order, orderStatusId, widgetId)
        }
        val localCacheHandler = AppWidgetHelper.getCacheHandler(context)
        localCacheHandler.putInt(Const.SharedPrefKey.LAST_SELECTED_ORDER_TYPE, orderStatusId)
        localCacheHandler.applyEditor()
    }

    private fun showSmallWidgetSuccessState(context: Context, remoteViews: RemoteViews, userSession: UserSessionInterface, order: OrderUiModel, widgetId: Int) {
        with(remoteViews) {
            OrderWidgetStateHelper.updateViewOnSuccess(this)
            setInt(R.id.orderSawSuccessNormal, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.orderSawSuccessSmall, Const.Method.SET_VISIBILITY, View.VISIBLE)

            val newOrderCount = order.sellerOrderStatus?.newOrder.orZero()
            val readyToShipCount = order.sellerOrderStatus?.readyToShip.orZero()
            val newOrderFmt = "$newOrderCount ${context.getString(R.string.saw_order)}"
            val readyToShipFmt = "$readyToShipCount ${context.getString(R.string.saw_order)}"
            setTextViewText(R.id.tvSawOrderNewOrder, newOrderFmt)
            setTextViewText(R.id.tvSawOrderReadyToShip, readyToShipFmt)
            setTextViewText(R.id.tvSawSmallOrderShopName, userSession.shopName)
            setInt(R.id.btnSawSmallOrderRefresh, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_refresh)

            OrderWidgetStateHelper.setupRefreshIntent<OrderAppWidget>(context, remoteViews, R.id.btnSawSmallOrderRefresh, widgetId)

            Utils.getAppIcon(context)?.let {
                val radius = context.dpToPx(6).toInt()
                Utils.loadImageIntoAppWidget(context, this, R.id.imgSawSmallOrderAppIcon, it, widgetId, radius)
            }

            val bundle = Bundle().apply {
                putString(Const.Extra.WIDGET_SIZE, WidgetSize.SMALL)
            }
            registerAppLinkIntent<OrderAppWidget>(context, R.id.containerSawSmallOrderNewOrder, ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER, widgetId, bundle)
            registerAppLinkIntent<OrderAppWidget>(context, R.id.containerSawSmallOrderReadyToShip, ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP, widgetId, bundle)
            registerAppLinkIntent<OrderAppWidget>(context, R.id.orderSawSmallHeader, ApplinkConst.SellerApp.SELLER_APP_HOME, widgetId)
            registerAppLinkIntent<OrderAppWidget>(context, R.id.imgSawSmallOrderAppIcon, ApplinkConst.SellerApp.SELLER_APP_HOME, widgetId)

            AppWidgetTracking.getInstance(context)
                    .sendEventImpressionSmallSuccessStateNewOrderWidget()
        }
    }

    private fun showNormalWidgetSuccessState(context: Context, remoteViews: RemoteViews, userSession: UserSessionInterface, order: OrderUiModel, orderStatusId: Int, widgetId: Int) {
        with(remoteViews) {
            OrderWidgetStateHelper.updateViewOnSuccess(this)
            setInt(R.id.orderSawSuccessSmall, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.orderSawSuccessNormal, Const.Method.SET_VISIBILITY, View.VISIBLE)

            val orderItemsByType = order.orders?.filter { it.statusId == orderStatusId }.orEmpty()

            setupOrderList(context, this, orderItemsByType, widgetId, orderStatusId)

            OrderWidgetStateHelper.setupRefreshIntent<OrderAppWidget>(context, remoteViews, R.id.btnSawOrderRefresh, widgetId)

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
                putExtra(Const.Extra.SELLER_ORDER, order)
            }
            val switchPendingIntent = PendingIntent.getBroadcast(context, 0, switchIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            setOnClickPendingIntent(R.id.containerSawOrderSwitch, switchPendingIntent)

            val orderTypeStringRes = if (orderStatusId == Const.OrderStatusId.NEW_ORDER) {
                R.string.saw_new_order
            } else {
                R.string.saw_ready_to_ship
            }
            val totalOrder = if (orderStatusId == Const.OrderStatusId.NEW_ORDER) {
                order.sellerOrderStatus?.newOrder.orZero()
            } else {
                order.sellerOrderStatus?.readyToShip.orZero()
            }
            val totalOrderFmt = "$totalOrder ${context.getString(orderTypeStringRes)}"
            setTextViewText(R.id.tvSawOrderTotalOrder, totalOrderFmt)
            setTextViewText(R.id.tvSawOrderShopName, userSession.shopName)
            setInt(R.id.btnSawOrderRefresh, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_refresh)
            setInt(R.id.icSawOrderMoreOrder, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_chevron_right)

            when (orderStatusId) {
                Const.OrderStatusId.NEW_ORDER -> {
                    setInt(R.id.imgSawOrderSwitchLeft, Const.Method.SET_VISIBILITY, View.GONE)
                    setInt(R.id.imgSawOrderSwitchRight, Const.Method.SET_VISIBILITY, View.VISIBLE)
                    setInt(R.id.imgSawOrderSwitchRight, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_arrow_right)
                    setTextViewText(R.id.tvSawOrderSwitch, context.getString(R.string.saw_ready_to_ship))
                    registerAppLinkIntent<OrderAppWidget>(context, R.id.tvSawOrderShopName, ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER, widgetId)
                    registerAppLinkIntent<OrderAppWidget>(context, R.id.tvSawOrderTotalOrder, ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER, widgetId)

                    AppWidgetTracking.getInstance(context)
                            .sendEventImpressionNewOrderOrderWidget()
                }
                else -> {
                    setInt(R.id.imgSawOrderSwitchRight, Const.Method.SET_VISIBILITY, View.GONE)
                    setInt(R.id.imgSawOrderSwitchLeft, Const.Method.SET_VISIBILITY, View.VISIBLE)
                    setInt(R.id.imgSawOrderSwitchLeft, Const.Method.SET_IMAGE_RESOURCE, R.drawable.ic_saw_arrow_left)
                    setTextViewText(R.id.tvSawOrderSwitch, context.getString(R.string.saw_new_order))
                    registerAppLinkIntent<OrderAppWidget>(context, R.id.tvSawOrderShopName, ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP, widgetId)
                    registerAppLinkIntent<OrderAppWidget>(context, R.id.tvSawOrderTotalOrder, ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP, widgetId)

                    AppWidgetTracking.getInstance(context)
                            .sendEventImpressionReadyShippingOrderWidget()
                }
            }

            Utils.getAppIcon(context)?.let {
                val radius = context.dpToPx(6).toInt()
                Utils.loadImageIntoAppWidget(context, this, R.id.imgSawOrderAppIcon, it, widgetId, radius)
            }

            registerAppLinkIntent<OrderAppWidget>(context, R.id.imgSawOrderAppIcon, ApplinkConstInternalSellerapp.SELLER_HOME, widgetId)
        }
    }

    private fun setupOrderList(context: Context, remoteViews: RemoteViews, items: List<OrderItemUiModel>, widgetId: Int, orderStatusId: Int) {
        with(remoteViews) {
            //setup widget list view
            val randomNumber = (Math.random() * 10000).toInt()
            val intent = Intent(context, OrderWidgetRemoteViewService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId + randomNumber)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                putExtra(Const.Extra.BUNDLE, Bundle().also {
                    it.putParcelableArrayList(Const.Extra.ORDER_ITEMS, ArrayList(items))
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

            //setup empty state for new order / ready to ship
            if (items.isEmpty()) {
                setInt(R.id.containerSawOrderListEmpty, Const.Method.SET_VISIBILITY, View.VISIBLE)
                setInt(R.id.lvSawOrderList, Const.Method.SET_VISIBILITY, View.GONE)
                if (orderStatusId == Const.OrderStatusId.NEW_ORDER) {
                    setTextViewText(R.id.tvSawOrderMessageOnEmpty, context.getString(R.string.saw_order_new_order_on_empty_message))
                    setTextViewText(R.id.tvSawOrderSubMessageOnEmpty, context.getString(R.string.saw_order_new_order_on_empty_sub_message))
                } else {
                    setTextViewText(R.id.tvSawOrderMessageOnEmpty, context.getString(R.string.saw_order_ready_to_ship_on_empty_message))
                    setTextViewText(R.id.tvSawOrderSubMessageOnEmpty, context.getString(R.string.saw_order_ready_to_ship_on_empty_sub_message))
                }

                Utils.loadImageIntoAppWidget(context, this, R.id.imgSawOrderEmptyState, Const.Images.ORDER_ON_EMPTY, widgetId)
            } else {
                setInt(R.id.containerSawOrderListEmpty, Const.Method.SET_VISIBILITY, View.GONE)
                setInt(R.id.lvSawOrderList, Const.Method.SET_VISIBILITY, View.VISIBLE)
            }
        }
    }
}