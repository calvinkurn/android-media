package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_BALANCE_WIDGET_CUSTOMVIEW
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.BalanceWidgetDataModel
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceAdapter

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

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        const val LAYOUT_SPAN_2 = 2
        const val LAYOUT_SPAN_3 = 3
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_item_widget_balance_widget, this)
        this.itemView = view
        this.itemContext = view.context
    }

    fun bind(element: BalanceWidgetDataModel, listener: HomeCategoryListener?) {
        this.listener = listener
        renderWidget(element)
        BenchmarkHelper.beginSystraceSection(TRACE_ON_BIND_BALANCE_WIDGET_CUSTOMVIEW)
        BenchmarkHelper.endSystraceSection()
    }

    private fun renderWidget(element: BalanceWidgetDataModel) {
        layoutManager = getLayoutManager(element)
        balanceAdapter = BalanceAdapter(listener)
        balanceAdapter?.setItemMap(element.drawerMap)
    }

    private fun getLayoutManager(element: BalanceWidgetDataModel): GridLayoutManager {
        val spanCount = when(element.itemCount) {
            4 -> LAYOUT_SPAN_2
            else -> LAYOUT_SPAN_3
        }
        return GridLayoutManager(itemView.context, spanCount)
    }

}