package com.tokopedia.sellerappwidget.view.executor

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.widget.RemoteViews
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchersProvider
import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.sellerappwidget.R
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.common.Utils
import com.tokopedia.sellerappwidget.data.local.SellerAppWidgetPreferences
import com.tokopedia.sellerappwidget.domain.mapper.OrderMapper
import com.tokopedia.sellerappwidget.domain.usecase.GetOrderUseCase
import com.tokopedia.sellerappwidget.view.appwidget.OrderAppWidget
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.state.order.OrderWidgetLoadingState
import com.tokopedia.sellerappwidget.view.state.order.OrderWidgetStateHelper
import com.tokopedia.sellerappwidget.view.viewmodel.OrderAppWidgetViewModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import com.tokopedia.sellerappwidget.view.work.GetOrderWorker
import timber.log.Timber
import java.util.*

/**
 * Created By @ilhamsuaib on 17/11/20
 */

class GetOrderExecutor(private val context: Context) : AppWidgetView<OrderUiModel> {

    companion object {
        private var INSTANCE: GetOrderExecutor? = null

        @JvmStatic
        fun run(context: Context, orderStatusId: Int) {
            if (INSTANCE == null) {
                INSTANCE = GetOrderExecutor(context)
            }
            INSTANCE?.run(orderStatusId)
        }
    }

    private val viewModel by lazy {
        val gqlRepository = GraphqlInteractor.getInstance().graphqlRepository
        val mapper = OrderMapper()
        val getNewOrderUseCase = GetOrderUseCase(gqlRepository, mapper)
        val getReadyToShipOrderUseCase = GetOrderUseCase(gqlRepository, mapper)
        return@lazy OrderAppWidgetViewModel(getNewOrderUseCase, getReadyToShipOrderUseCase, CoroutineDispatchersProvider)
    }
    private val sharedPref: SellerAppWidgetPreferences by lazy {
        SellerAppWidgetPreferences.getInstance(context)
    }
    private var orderStatusId = OrderAppWidget.DEFAULT_ORDER_STATUS_ID

    fun run(orderStatusId: Int) {
        this.orderStatusId = orderStatusId

        val dateFormat = "dd/MM/yyyy"
        val endDateFmt = Utils.formatDate(Date(Utils.getNowTimeStamp()), dateFormat)
        val startDateFmt = Utils.getNPastMonthTimeText(3)

        showLoadingState()
        viewModel.bindView(this)
        viewModel.getOrderList(startDateFmt, endDateFmt)
    }

    override fun onSuccessGetOrderList(result: OrderUiModel) {
        sharedPref.putLong(Const.SharedPrefKey.ORDER_LAST_UPDATED, System.currentTimeMillis())
        OrderAppWidget.setOnSuccess(context, result, orderStatusId)
        GetOrderWorker.runWorker(context)
        viewModel.unbind()
    }

    override fun onFailedGetOrderList(t: Throwable) {
        OrderAppWidget.setOnError(context)
        GetOrderWorker.runWorker(context)
        Timber.e(t)
        viewModel.unbind()
    }

    private fun showLoadingState() {
        val remoteViews = RemoteViews(context.packageName, R.layout.saw_app_widget_order)
        val awm = AppWidgetManager.getInstance(context)
        val ids = awm.getAppWidgetIds(ComponentName(context, OrderAppWidget::class.java))
        ids.forEach {
            OrderWidgetStateHelper.updateViewOnLoading(remoteViews)
            OrderWidgetLoadingState.setupLoadingState(context, awm, remoteViews, it)
            awm.updateAppWidget(it, remoteViews)
        }
    }
}