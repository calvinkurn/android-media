package com.tokopedia.hotel.search_map.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.hotel.R
import com.tokopedia.hotel.databinding.LayoutHotelFilterSelectionRangeBinding
import com.tokopedia.hotel.search_map.data.model.FilterRatingEnum
import com.tokopedia.hotel.search_map.data.model.FilterV2
import com.tokopedia.hotel.search_map.presentation.adapter.HotelSearchResultFilterV2Adapter
import com.tokopedia.unifycomponents.RangeSliderUnify
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by jessica on 12/08/20
 */

class FilterSelectionRangeViewHolder(view: View, val onSelectedFilterChangedListener: OnSelectedFilterChangedListener)
    : HotelSearchResultFilterV2Adapter.FilterBaseViewHolder(view) {

    private val binding = LayoutHotelFilterSelectionRangeBinding.bind(view)

    override var filterName: String = ""

    override fun bind(filter: FilterV2) {
        filterName  = filter.name

        with(binding) {
            baseRatingStep.removeAllViews()
            hotelFilterSelectionRangeTitle.text = filter.displayName


            hotelFilterSelectionRangeSeekbar.activeBackgroundRailColor = ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_GN300)
            hotelFilterSelectionRangeSeekbar.activeRailColor = ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_NN50)
            hotelFilterSelectionRangeSeekbar.activeKnobColor = ContextCompat.getColor(root.context, unifyprinciplesR.color.Unify_GN500)

            val selectedValue =  filter.optionSelected.firstOrNull() ?: "0"

            if (filter.options.isEmpty()) {
                filter.options = listOf(
                        FilterRatingEnum.ALL_RATING.value,
                        FilterRatingEnum.ABOVE_6.value,
                        FilterRatingEnum.ABOVE_7.value,
                        FilterRatingEnum.ABOVE_8.value,
                        FilterRatingEnum.ABOVE_9.value)
            }
            hotelFilterSelectionRangeSeekbar.updateEndValue(filter.options.lastIndex)

            filter.options.forEachIndexed { index, item ->
                if (item == selectedValue) {
                    hotelFilterSelectionRangeSeekbar.setInitialValue(index)
                    hotelFilterSelectionRangeSeekbar.updateValue(index)
                }

                val stepView = LayoutInflater.from(root.context).inflate(R.layout.item_hotel_filter_rating_step, null)

                stepView.findViewById<TextView>(R.id.title_step).text  = item

                baseRatingStep.addView(stepView)
                if (index < filter.options.size - 1){
                    val separator = View(root.context)
                    val lp = LinearLayout.LayoutParams(root.resources.getDimensionPixelSize(unifyprinciplesR.dimen.layout_lvl0),
                            ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                    separator.layoutParams = lp
                    baseRatingStep.addView(separator)
                }
            }

            if (filter.optionSelected.isEmpty()){
                val maxValue = hotelFilterSelectionRangeSeekbar.getValue().first
                hotelFilterSelectionRangeSeekbar.setInitialValue(maxValue)
                hotelFilterSelectionRangeSeekbar.updateValue(maxValue)
            }

            hotelFilterSelectionRangeSeekbar.onSliderMoveListener = object : RangeSliderUnify.OnSliderMoveListener{
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
