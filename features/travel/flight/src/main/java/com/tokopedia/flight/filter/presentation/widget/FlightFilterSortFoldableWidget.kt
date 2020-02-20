package com.tokopedia.flight.filter.presentation.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.adapter.FlightFilterSortWidgetAdapter
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterSortWidgetViewHolder
import com.tokopedia.flight.filter.presentation.model.BaseFilterSortModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_flight_filter_sort_foldable.view.*

/**
 * @author by jessica on 2020-02-19
 */

class FlightFilterSortFoldableWidget @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr), FlightFilterSortWidgetAdapter.ActionListener {

    var titleText: String = ""
    var isFoldAble: Boolean = true
    var hasShowMore: Boolean = true
    var listener: ActionListener? = null
    var maxItemCount: Int = 5
    var isFlowLayout: Boolean = true
    var isSelectOnlyOneChip: Boolean = false

    private lateinit var widgetAdapter: FlightFilterSortWidgetAdapter

    init {
        View.inflate(context, R.layout.widget_flight_filter_sort_foldable, this)
    }

    /**
     *  the buildView() function is expected to be called after all params set.
     */


    fun buildView(items: List<BaseFilterSortModel>) {
        tv_title.text = titleText

        if (isFoldAble) {
            ic_arrow_up.show()
            ic_arrow_up.setOnClickListener {
                if (child_view.isVisible) {
                    child_view.hide()
                    ic_arrow_up.setImageDrawable(ContextCompat.getDrawable(context, com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_down_gray_24))
                } else {
                    child_view.show()
                    ic_arrow_up.setImageDrawable(ContextCompat.getDrawable(context, com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_up_gray_24))
                }
            }
        } else ic_arrow_up.hide()

        if (hasShowMore) {
            tv_show_more.show()
            tv_show_more.setOnClickListener {
                listener?.onClickShowMore()
            }
        } else tv_show_more.hide()

        if (!::widgetAdapter.isInitialized) {
            widgetAdapter = FlightFilterSortWidgetAdapter(items.toMutableList(), this)
            widgetAdapter.isSelectOnlyOneChip = isSelectOnlyOneChip
            widgetAdapter.maxItemCount = maxItemCount

            if (isFlowLayout) {
                rv_flight_sort.layoutManager = ChipsLayoutManager.newBuilder(context)
                        .setOrientation(ChipsLayoutManager.HORIZONTAL)
                        .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                        .build()
            } else {
                rv_flight_sort.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            rv_flight_sort.adapter = widgetAdapter
        }
    }

    override fun onResetChip() {
        for (i in 0 until widgetAdapter.itemCount) {
            with(rv_flight_sort.findViewHolderForAdapterPosition(i) as FlightFilterSortWidgetViewHolder) {
                this.unselectChip()
            }
        }
    }

    override fun onChipStateChanged(items: List<BaseFilterSortModel>) {
        listener?.onChipStateChanged(items)
    }

    interface ActionListener {
        fun onChipStateChanged(items: List<BaseFilterSortModel>)
        fun onClickShowMore()
    }

}