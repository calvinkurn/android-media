package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.benchmark.BenchmarkHelper
import com.tokopedia.home.beranda.helper.benchmark.TRACE_ON_BIND_BALANCE_WIDGET_CUSTOMVIEW
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel.Companion.TYPE_STATE_2
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget.BalanceAdapter
import com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.layoutmanager.NpaGridLayoutManager
import com.tokopedia.home.util.ViewUtils
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show

/**
 * Created by yfsx on 3/1/21.
 */
class BalanceWidgetView: FrameLayout {

    private var itemView: View
    private val itemContext: Context
    private var listener: HomeCategoryListener? = null
    private var rvBalance: RecyclerView? = null
    private var layoutManager: NpaGridLayoutManager? = null
    private var balanceAdapter: BalanceAdapter? = null
    private var viewBalanceCoachmark: LinearLayout? = null
    private var viewBalanceCoachmarkNew: LinearLayout? = null
    private lateinit var containerWidget: FrameLayout

    private var tokopointsView: View? = null
    private var tokopointsViewNew: View? = null
    private var gopayActivateNewView: View? = null

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
        viewBalanceCoachmark = view.findViewById(R.id.view_balance_widget_coachmark)
        viewBalanceCoachmarkNew = view.findViewById(R.id.view_balance_widget_coachmark_new)
        rvBalance?.itemAnimator?.changeDuration = 0
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
        if (element.isGopayEligible == null) {
            viewBalanceCoachmark?.visibility = View.GONE
            viewBalanceCoachmarkNew?.visibility = View.GONE
        }
        if (element.balanceType == TYPE_STATE_2 && element.isGopayEligible == false) {
            viewBalanceCoachmark?.visibility = View.INVISIBLE
            viewBalanceCoachmarkNew?.visibility = View.GONE
        } else if (element.balanceType == TYPE_STATE_2 && element.isGopayEligible == true) {
            viewBalanceCoachmark?.visibility = View.GONE
            viewBalanceCoachmarkNew?.visibility = View.INVISIBLE
        } else {
            viewBalanceCoachmark?.visibility = View.GONE
            viewBalanceCoachmarkNew?.visibility = View.GONE
        }
        containerWidget.background = ViewUtils.generateBackgroundWithShadow(containerWidget, com.tokopedia.unifyprinciples.R.color.Unify_N0, com.tokopedia.home.R.dimen.ovo_corner_radius, com.tokopedia.unifyprinciples.R.color.Unify_N400_32, com.tokopedia.home.R.dimen.ovo_elevation, Gravity.CENTER)
        layoutManager = getLayoutManager(element)
        if (balanceAdapter == null || rvBalance?.adapter == null) {
            balanceAdapter = BalanceAdapter(listener, object: DiffUtil.ItemCallback<BalanceDrawerItemModel>() {
                override fun areItemsTheSame(
                    oldItem: BalanceDrawerItemModel,
                    newItem: BalanceDrawerItemModel
                ): Boolean {
                    return oldItem.state == newItem.state
                }

                override fun areContentsTheSame(
                    oldItem: BalanceDrawerItemModel,
                    newItem: BalanceDrawerItemModel
                ): Boolean {
                    return oldItem == newItem
                }

            })
            rvBalance?.layoutManager = layoutManager
            rvBalance?.adapter = balanceAdapter
        }
        if (element.balanceDrawerItemModels.isEmpty()) {
            rvBalance?.gone()
        } else {
            balanceAdapter?.setItemMap(element)
            rvBalance?.show()
        }
    }

    private fun getLayoutManager(element: HomeBalanceModel): NpaGridLayoutManager {
        val spanCount = when(element.balanceType) {
            HomeBalanceModel.TYPE_STATE_2 -> LAYOUT_SPAN_2
            else -> LAYOUT_SPAN_3
        }
        return NpaGridLayoutManager(itemView.context, spanCount)
    }

    fun startRotationForPosition(position: Int) {
        val viewholder = rvBalance?.findViewHolderForAdapterPosition(position)
        viewholder?.let {
            (it as? BalanceAdapter.Holder)?.let {
                it.setDrawerItemWithAnimation()
            }
        }
    }

    fun getBalanceWidgetRecyclerView(): RecyclerView? {
        return rvBalance
    }

    fun getTokopointsView(): View? {
        tokopointsView = findViewById(R.id.home_coachmark_item_tokopoints)
        return tokopointsView
    }

    fun getGopayView(): View? {
        if (balanceAdapter?.getItemMap()?.containsGopay() == true) {
            val gopayView: View = findViewById(R.id.home_coachmark_item_gopay)
            return gopayView
        }
        return null
    }

    fun getGopayNewView(): View? {
        if (balanceAdapter?.getItemMap()?.containsGopay() == true) {
            val gopayViewNew: View = findViewById(R.id.home_coachmark_item_gopay_new)
            return gopayViewNew
        }
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