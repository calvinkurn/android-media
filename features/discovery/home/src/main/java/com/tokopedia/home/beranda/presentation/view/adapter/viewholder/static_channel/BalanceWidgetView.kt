package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_BALANCE_WIDGET_CUSTOMVIEW
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceShimmerModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceWidgetFailedModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.factory.balancewidget.BalanceWidgetTypeFactoryImpl
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceWidgetAdapter

/**
 * Created by yfsx on 3/1/21.
 */
class BalanceWidgetView : FrameLayout {

    private var itemView: View
    private val itemContext: Context
    private var listener: HomeCategoryListener? = null
    private var rvBalance: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var balanceWidgetAdapter: BalanceWidgetAdapter? = null
    private var subscriptionPosition = HomeBalanceModel.DEFAULT_BALANCE_POSITION
    private var previousElement: HomeBalanceModel? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {
        private const val DEFAULT_POSITION_BALANCE_WIDGET = 0
    }

    init {
        val view =
            LayoutInflater.from(context).inflate(R.layout.layout_item_widget_balance_widget, this)
        rvBalance = view.findViewById(R.id.rv_balance_widget)
        this.itemView = view
        this.itemContext = view.context
    }

    fun bind(element: HomeBalanceModel, listener: HomeCategoryListener?) {
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_BALANCE_WIDGET_CUSTOMVIEW)
        this.listener = listener
        renderWidget(element)
        BenchmarkHelper.endSystraceSection()
    }

    private fun renderWidget(element: HomeBalanceModel) {
        layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.VERTICAL, false)
        if (balanceWidgetAdapter == null || rvBalance?.adapter == null) {
            balanceWidgetAdapter = BalanceWidgetAdapter(BalanceWidgetTypeFactoryImpl(listener))
            rvBalance?.layoutManager = layoutManager
            rvBalance?.adapter = balanceWidgetAdapter
        }
        when (element.status) {
            HomeBalanceModel.STATUS_LOADING -> {
                if (previousElement?.status == HomeBalanceModel.STATUS_LOADING || previousElement == null) {
                    balanceWidgetAdapter?.setVisitables(listOf(BalanceShimmerModel()))
                }
            }
            HomeBalanceModel.STATUS_ERROR -> {
                balanceWidgetAdapter?.setVisitables(listOf(BalanceWidgetFailedModel()))
            }
            else -> {
                loadDataSuccess(element)
            }
        }
        previousElement = element
    }

    private fun loadDataSuccess(element: HomeBalanceModel) {
        if (element.balanceDrawerItemModels.isNotEmpty()) {
            subscriptionPosition = element.balancePositionSubscriptions
            balanceWidgetAdapter?.setVisitables(listOf(element))
            listener?.showBalanceWidgetCoachMark(element)
            rvBalance?.post {
                listener?.showBalanceWidgetCoachMark(element)
            }
        } else {
            balanceWidgetAdapter?.setVisitables(listOf(BalanceWidgetFailedModel()))
        }
    }

    fun getSubscriptionView(): View? {
        val firstViewHolder = rvBalance?.findViewHolderForAdapterPosition(DEFAULT_POSITION_BALANCE_WIDGET)
        firstViewHolder?.let {
            if (it is BalanceWidgetViewHolder) {
                return it.getSubscriptionView(subscriptionPosition)
            }
        }
        return null
    }
}
