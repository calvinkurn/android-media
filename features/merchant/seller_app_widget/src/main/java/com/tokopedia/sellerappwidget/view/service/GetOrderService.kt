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
import com.tokopedia.sellerappwidget.data.local.SellerAppWidgetPreferences
import com.tokopedia.sellerappwidget.di.DaggerAppWidgetComponent
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.state.order.OrderWidgetLoadingState
import com.tokopedia.sellerappwidget.view.state.order.OrderWidgetStateHelper
import com.tokopedia.sellerappwidget.view.viewmodel.OrderAppWidgetViewModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import com.tokopedia.sellerappwidget.view.work.GetOrderWorker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 17/11/20
 */

class GetOrderService : JobIntentService(), AppWidgetView<List<OrderUiModel>> {

    companion object {
        private const val JOB_ID = 8043

        @JvmStatic
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
    }

    @Inject
    lateinit var viewModel: OrderAppWidgetViewModel

    @Inject
    lateinit var sharedPref: SellerAppWidgetPreferences

    private var orderStatusId = OrderAppWidget.DEFAULT_ORDER_STATUS_ID

    override fun onCreate() {
        super.onCreate()

        initInjector()
        viewModel.bindView(this)
    }

    override fun onHandleWork(intent: Intent) {
        orderStatusId = intent.getIntExtra(Const.Extra.ORDER_STATUS_ID, orderStatusId)

        val dateFormat = "dd/MM/yyyy"
        val nowMillis = Date()
        val yesterdayMillis = Date(nowMillis.time.minus(TimeUnit.DAYS.toMillis(1)))
        val today = Utils.formatDate(nowMillis, dateFormat)
        val yesterday = Utils.formatDate(yesterdayMillis, dateFormat)

        showLoadingState()
        viewModel.getOrderList(yesterday, today)
    }

    override fun onSuccessGetOrderList(result: Success<List<OrderUiModel>>) {
        val orderList = result.data
        sharedPref.putLong(Const.SharedPrefKey.ORDER_LAST_UPDATED, System.currentTimeMillis())
        OrderAppWidget.setOnSuccess(applicationContext, orderList, orderStatusId)
        GetOrderWorker.runWorker(applicationContext)
    }

    override fun onFailedGetOrderList(fail: Fail) {
        OrderAppWidget.setOnError(applicationContext)
        GetOrderWorker.runWorker(applicationContext)
        Timber.e(fail.throwable)
    }

    private fun initInjector() {
        DaggerAppWidgetComponent.builder()
                .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    private fun showLoadingState() {
        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.saw_app_widget_order)
        val awm = AppWidgetManager.getInstance(applicationContext)
        val ids = awm.getAppWidgetIds(ComponentName(applicationContext, OrderAppWidget::class.java))
        ids.forEach {
            OrderWidgetStateHelper.updateViewOnLoading(remoteViews)
            OrderWidgetLoadingState.setupLoadingState(awm, remoteViews, it)
            awm.updateAppWidget(it, remoteViews)
        }
    }
}