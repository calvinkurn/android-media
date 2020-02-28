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
        tvFlightFilterSortWidgetTitle.text = titleText

        if (isFoldAble) {
            icFlightFilterSortWidgetFoldIcon.show()
            icFlightFilterSortWidgetFoldIcon.setOnClickListener {
                if (child_view.isVisible) {
                    child_view.hide()
                    icFlightFilterSortWidgetFoldIcon.setImageDrawable(ContextCompat.getDrawable(context, com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_down_gray_24))
                } else {
                    child_view.show()
                    icFlightFilterSortWidgetFoldIcon.setImageDrawable(ContextCompat.getDrawable(context, com.tokopedia.resources.common.R.drawable.ic_system_action_arrow_up_gray_24))
                }
            }
        } else icFlightFilterSortWidgetFoldIcon.hide()

        if (hasShowMore) {
            tvFlightFilterSortWidgetSeeAll.show()
            tvFlightFilterSortWidgetSeeAll.setOnClickListener {
                listener?.onClickShowMore()
            }
        } else tvFlightFilterSortWidgetSeeAll.hide()

        if (!::widgetAdapter.isInitialized) {
            widgetAdapter = FlightFilterSortWidgetAdapter(items.toMutableList(), this)
            widgetAdapter.isSelectOnlyOneChip = isSelectOnlyOneChip
            widgetAdapter.maxItemCount = maxItemCount

            if (isFlowLayout) {
                rvFlightFilterSortWidget.layoutManager = ChipsLayoutManager.newBuilder(context)
                        .setOrientation(ChipsLayoutManager.HORIZONTAL)
                        .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                        .build()
            } else {
                rvFlightFilterSortWidget.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }

            rvFlightFilterSortWidget.adapter = widgetAdapter
        }
    }

    override fun onResetChip() {
        for (i in 0 until widgetAdapter.itemCount) {
            if (!widgetAdapter.items[i].isSelected) {
                rvFlightFilterSortWidget.findViewHolderForAdapterPosition(i)?.let {
                    (it as FlightFilterSortWidgetViewHolder).unselectChip()
                }
            }
        }
    }

    fun performClickOnChipWithPosition(position: Int) {
        if (position < widgetAdapter.maxItemCount){
            rvFlightFilterSortWidget.findViewHolderForAdapterPosition(position)?.let {
                (it as FlightFilterSortWidgetViewHolder).itemView.performClick()
            }
        }
        else widgetAdapter.resetAllSelectedChip()
    }

    fun getItems(): List<BaseFilterSortModel> {
        return widgetAdapter.items
    }

    fun notifyDataSetChanged() {
        widgetAdapter.notifyDataSetChanged()
    }

    override fun onChipStateChanged(items: List<BaseFilterSortModel>) {
        listener?.onChipStateChanged(items)
    }

    interface ActionListener {
        fun onChipStateChanged(items: List<BaseFilterSortModel>)
        fun onClickShowMore()
    }

}