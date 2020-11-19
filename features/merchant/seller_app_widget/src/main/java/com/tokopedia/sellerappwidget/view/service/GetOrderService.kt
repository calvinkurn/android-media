package com.tokopedia.sellerappwidget.view.service

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.JobIntentService
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.Utils
import com.tokopedia.sellerappwidget.di.DaggerAppWidgetComponent
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.state.order.OrderWidgetStateHelper
import com.tokopedia.sellerappwidget.view.state.order.OrderWidgetSuccessState
import com.tokopedia.sellerappwidget.view.viewmodel.OrderAppWidgetViewModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.OrderAppWidgetView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import java.util.*
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/11/20
 */

class GetOrderService : JobIntentService(), OrderAppWidgetView {

    companion object {
        private const val JOB_ID = 8043

        fun startService(context: Context, orderStatusId: Int) {
            try {
                val work = Intent(context, GetOrderService::class.java).apply {
                    putExtra(Const.Extra.ORDER_STATUS_ID, orderStatusId)
                }
                enqueueWork(context, GetOrderService::class.java, JOB_ID, work)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }

        fun scheduleService(context: Context) {
            try {

            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }

    @Inject
    lateinit var viewModel: OrderAppWidgetViewModel

    private var orderStatusId = OrderAppWidget.DEFAULT_ORDER_STATUS_ID

    override fun onCreate() {
        super.onCreate()

        initInjector()
        viewModel.bindView(this)
    }

    private fun initInjector() {
        DaggerAppWidgetComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun onHandleWork(intent: Intent) {
        orderStatusId = intent.getIntExtra(Const.Extra.ORDER_STATUS_ID, orderStatusId)

        val dateFormat = "dd/MM/yyyy"
        val now = Date()
        val today = Utils.formatDate(now, dateFormat)

        showLoadingState()
        viewModel.getOrderList(today)
    }

    private fun showLoadingState() {
        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.saw_app_widget_order)
        OrderWidgetStateHelper.updateViewOnLoading(remoteViews)
        val awm = AppWidgetManager.getInstance(applicationContext)
        val ids = awm.getAppWidgetIds(ComponentName(applicationContext, OrderAppWidget::class.java))
        ids.forEach {
            awm.updateAppWidget(it, remoteViews)
        }
        OrderWidgetSuccessState.setLastUpdated(applicationContext)
    }

    override fun onSuccessGetOrderList(result: Success<List<OrderUiModel>>) {
        val orderList = result.data
        OrderAppWidget.showSuccessState(applicationContext, orderList, orderStatusId)
    }

    override fun onFailedGetOrderList(fail: Fail) {

    }
}