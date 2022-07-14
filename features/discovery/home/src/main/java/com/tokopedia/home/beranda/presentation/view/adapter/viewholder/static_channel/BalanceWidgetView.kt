package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
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
class BalanceWidgetView: FrameLayout {

    private var itemView: View
    private val itemContext: Context
    private var listener: HomeCategoryListener? = null
    private var rvBalance: RecyclerView? = null
    private var layoutManager: LinearLayoutManager? = null
    private var balanceWidgetAdapter: BalanceWidgetAdapter? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        var disableAnimation: Boolean = false
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_widget_balance_widget, this)
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
                balanceWidgetAdapter?.setVisitables(listOf(BalanceShimmerModel()))
            }
            HomeBalanceModel.STATUS_ERROR -> {
                balanceWidgetAdapter?.setVisitables(listOf(BalanceWidgetFailedModel()))
            }
            else -> {
                balanceWidgetAdapter?.setVisitables(listOf(element))
                rvBalance?.post {
                    listener?.showBalanceWidgetCoachMark(element)
                }
            }
        }
    }

    fun startRotationForPosition(position: Int) {
        if (!disableAnimation) {
            val viewholder = rvBalance?.findViewHolderForAdapterPosition(position)
            viewholder?.let {
                (it as? BalanceViewHolder)?.let {
                }
            }
        }
    }

    fun showLoading() {
        balanceWidgetAdapter?.setVisitables(listOf(BalanceShimmerModel()))
    }

    fun getRewardsView(): View? {
        val firstViewHolder = rvBalance?.findViewHolderForAdapterPosition(0)
        if (firstViewHolder is BalanceWidgetViewHolder) {
            return firstViewHolder.getRewardsView()
        }
        return null
    }

    fun getSubscriptionView(): View? {
        val firstViewHolder = rvBalance?.findViewHolderForAdapterPosition(0)
        if (firstViewHolder is BalanceWidgetViewHolder) {
            return firstViewHolder.getSubscriptionView()
        }
        return null
    }
}