package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_BALANCE_WIDGET_CUSTOMVIEW
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceAdapter
import com.tokopedia.home.util.ViewUtils
import kotlinx.android.synthetic.main.layout_item_widget_balance_widget.view.*

/**
 * Created by yfsx on 3/1/21.
 */
class BalanceWidgetView: FrameLayout {

    private var itemView: View
    private val itemContext: Context
    private var listener: HomeCategoryListener? = null
    private var rvBalance: RecyclerView? = null
    private var layoutManager: GridLayoutManager? = null
    private var balanceAdapter: BalanceAdapter? = null
    private lateinit var containerWidget: LinearLayout

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        const val LAYOUT_SPAN_2 = 2
        const val LAYOUT_SPAN_3 = 3
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_widget_balance_widget, this)
        rvBalance = view.findViewById(R.id.rv_balance_widget)
        containerWidget = view.findViewById(R.id.container_balance_widget)
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
        containerWidget.background = ViewUtils.generateBackgroundWithShadow(containerWidget, R.color.Unify_N0, R.dimen.dp_8, com.tokopedia.unifyprinciples.R.color.Unify_N400_32, R.dimen.dp_2, Gravity.CENTER)
        layoutManager = getLayoutManager(element)
        balanceAdapter = BalanceAdapter(listener)
        rvBalance?.layoutManager = layoutManager
        rvBalance?.adapter = balanceAdapter
        balanceAdapter?.setItemMap(element)
    }

    private fun getLayoutManager(element: HomeBalanceModel): GridLayoutManager {
        val spanCount = when(element.balanceType) {
            HomeBalanceModel.TYPE_STATE_2 -> LAYOUT_SPAN_2
            else -> LAYOUT_SPAN_3
        }
        return GridLayoutManager(itemView.context, spanCount)
    }

    private fun getBalanceWidgetRecyclerView(): RecyclerView? {
        return rvBalance
    }

    fun getTokopointsView(): View? {
        val tokopointsPos = balanceAdapter?.getTokopointsDataPosition() ?: -1
        if (tokopointsPos != -1) {
            layoutManager?.let {
                return it.getChildAt(tokopointsPos)
            }
        }
        return null
    }
}