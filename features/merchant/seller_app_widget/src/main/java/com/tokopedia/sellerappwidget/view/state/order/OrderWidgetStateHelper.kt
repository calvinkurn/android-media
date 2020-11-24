package com.tokopedia.sellerappwidget.view.state.order

import android.appwidget.AppWidgetManager
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.AppWidgetHelper
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.view.model.OrderUiModel

/**
 * Created By @ilhamsuaib on 18/11/20
 */

object OrderWidgetStateHelper {

    fun setLastUpdated(context: Context, remoteView: RemoteViews) {
        val updatedFmt = AppWidgetHelper.getOrderWidgetLastUpdatedFmt(context)
        val updated = context.getString(R.string.saw_updated)
        remoteView.setTextViewText(R.id.tvSawOrderUpdated, "$updated $updatedFmt")
        remoteView.setTextViewText(R.id.tvSawSmallOrderUpdated, "$updated $updatedFmt")
    }

    fun showEmptyState(context: Context, appWidgetManager: AppWidgetManager, widgetIds: Array<Int>, widgetItems: List<OrderUiModel>) {

    }

    fun updateViewOnSuccess(remoteViews: RemoteViews) {
        with(remoteViews) {
            setInt(R.id.containerSawOrderListEmpty, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListLoading, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListError, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListNoLogin, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListSuccess, Const.Method.SET_VISIBILITY, View.VISIBLE)
        }
    }

    fun updateViewOnLoading(remoteViews: RemoteViews) {
        with(remoteViews) {
            setInt(R.id.containerSawOrderListEmpty, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListError, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListSuccess, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListNoLogin, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListLoading, Const.Method.SET_VISIBILITY, View.VISIBLE)
        }
    }

    fun updateViewOnError(remoteViews: RemoteViews) {
        with(remoteViews) {
            setInt(R.id.containerSawOrderListEmpty, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListSuccess, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListLoading, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListNoLogin, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListError, Const.Method.SET_VISIBILITY, View.VISIBLE)
        }
    }

    fun updateViewNoLogin(remoteViews: RemoteViews) {
        with(remoteViews) {
            setInt(R.id.containerSawOrderListEmpty, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListSuccess, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListLoading, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListError, Const.Method.SET_VISIBILITY, View.GONE)
            setInt(R.id.containerSawOrderListNoLogin, Const.Method.SET_VISIBILITY, View.VISIBLE)
        }
    }
}