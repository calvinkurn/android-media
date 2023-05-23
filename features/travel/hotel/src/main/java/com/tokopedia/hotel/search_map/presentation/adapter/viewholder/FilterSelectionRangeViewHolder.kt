package com.tokopedia.hotel.search_map.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search_map.data.model.FilterRatingEnum
import com.tokopedia.hotel.search_map.data.model.FilterV2
import com.tokopedia.hotel.search_map.presentation.adapter.HotelSearchResultFilterV2Adapter
import com.tokopedia.unifycomponents.RangeSliderUnify
import kotlinx.android.synthetic.main.layout_hotel_filter_selection_range.view.*

/**
 * @author by jessica on 12/08/20
 */

class FilterSelectionRangeViewHolder(view: View, val onSelectedFilterChangedListener: OnSelectedFilterChangedListener)
    : HotelSearchResultFilterV2Adapter.FilterBaseViewHolder(view) {

    override var filterName: String = ""

    override fun bind(filter: FilterV2) {
        filterName  = filter.name

        with(itemView) {
            base_rating_step.removeAllViews()
            hotel_filter_selection_range_title.text = filter.displayName


            hotel_filter_selection_range_seekbar.activeBackgroundRailColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN300)
            hotel_filter_selection_range_seekbar.activeRailColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN50)
            hotel_filter_selection_range_seekbar.activeKnobColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)

            val selectedValue =  filter.optionSelected.firstOrNull() ?: "0"

            if (filter.options.isEmpty()) {
                filter.options = listOf(
                        FilterRatingEnum.ALL_RATING.value,
                        FilterRatingEnum.ABOVE_6.value,
                        FilterRatingEnum.ABOVE_7.value,
                        FilterRatingEnum.ABOVE_8.value,
                        FilterRatingEnum.ABOVE_9.value)
            }
            hotel_filter_selection_range_seekbar.updateEndValue(filter.options.lastIndex)

            filter.options.forEachIndexed { index, item ->
                if (item == selectedValue) {
                    hotel_filter_selection_range_seekbar.setInitialValue(index)
                    hotel_filter_selection_range_seekbar.updateValue(index)
                }

                val stepView = LayoutInflater.from(context).inflate(R.layout.item_hotel_filter_rating_step, null)

                stepView.findViewById<TextView>(R.id.title_step).text  = item

                base_rating_step.addView(stepView)
                if (index < filter.options.size - 1){
                    val separator = View(context)
                    val lp = LinearLayout.LayoutParams(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl0),
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                    separator.layoutParams = lp
                    base_rating_step.addView(separator)
                }
            }

            if (filter.optionSelected.isEmpty()){
                val maxValue = hotel_filter_selection_range_seekbar.getValue().first
                hotel_filter_selection_range_seekbar.setInitialValue(maxValue)
                hotel_filter_selection_range_seekbar.updateValue(maxValue)
            }

            hotel_filter_selection_range_seekbar.onSliderMoveListener = object : RangeSliderUnify.OnSliderMoveListener{
                override fun onSliderMove(p0: Pair<Int, Int>) {
                    if (filter.options.getOrNull(p0.first) != null) {
                        if (p0.first == 0) onSelectedFilterChangedListener.onSelectedFilterChanged(filterName)
                        else {
                            onSelectedFilterChangedListener.onSelectedFilterChanged(filterName,
                                mutableListOf(filter.options[p0.first]))
                        }
                    }
                }

            }
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_hotel_filter_selection_range
    }
}
