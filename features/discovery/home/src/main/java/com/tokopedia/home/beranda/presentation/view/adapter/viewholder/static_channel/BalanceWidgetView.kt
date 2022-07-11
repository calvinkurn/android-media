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
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel.Companion.TYPE_STATE_2
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
    private var viewBalanceCoachmark: LinearLayout? = null
    private var viewBalanceCoachmarkNew: LinearLayout? = null

    private var tokopointsView: View? = null
    private var tokopointsViewNew: View? = null
    private var gopayActivateNewView: View? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        var disableAnimation: Boolean = false
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_widget_balance_widget, this)
        rvBalance = view.findViewById(R.id.rv_balance_widget)
        viewBalanceCoachmark = view.findViewById(R.id.view_balance_widget_coachmark)
        viewBalanceCoachmarkNew = view.findViewById(R.id.view_balance_widget_coachmark_new)
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
        if (element.balanceType == TYPE_STATE_2) {
            viewBalanceCoachmark?.visibility = View.GONE
            viewBalanceCoachmarkNew?.visibility = View.INVISIBLE
        } else {
            viewBalanceCoachmark?.visibility = View.GONE
            viewBalanceCoachmarkNew?.visibility = View.GONE
        }
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

    fun getTokopointsView(): View? {
        tokopointsView = findViewById(R.id.home_coachmark_item_tokopoints)
        return tokopointsView
    }

    fun getGopayView(): View? {
//        if (balanceAdapter?.getItemMap()?.containsGopay() == true) {
//            val gopayView: View = findViewById(R.id.home_coachmark_item_gopay)
//            return gopayView
//        }
        return null
    }

    fun getGopayNewView(): View? {
//        if (balanceAdapter?.getItemMap()?.containsGopay() == true) {
//            val gopayViewNew: View = findViewById(R.id.home_coachmark_item_gopay_new)
//            return gopayViewNew
//        }
        return null
    }

    fun getGopayActivateNewView(): View? {
        gopayActivateNewView = findViewById(R.id.home_coachmark_item_gopay_activate_new)
        return gopayActivateNewView
    }

    fun getTokopointsNewView(): View? {
        tokopointsViewNew = findViewById(R.id.home_coachmark_item_tokopoint_new)
        return tokopointsViewNew
    }
}