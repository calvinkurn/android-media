package com.tokopedia.hotel.search.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.FilterRatingEnum
import com.tokopedia.hotel.search.data.model.FilterV2
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultFilterV2Adapter
import kotlinx.android.synthetic.main.layout_hotel_filter_selection_range.view.*
import kotlinx.android.synthetic.main.layout_hotel_filter_selection_range.view.base_rating_step

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
            hotel_filter_selection_range_seekbar.max = filter.options.size - 1

            val selectedValue =  filter.optionSelected.firstOrNull() ?: "0"

            if (filter.options.isEmpty()) {
                filter.options = listOf(
                        FilterRatingEnum.ALL_RATING.value,
                        FilterRatingEnum.ABOVE_6.value,
                        FilterRatingEnum.ABOVE_7.value,
                        FilterRatingEnum.ABOVE_8.value,
                        FilterRatingEnum.ABOVE_9.value)
            }

            filter.options.forEachIndexed { index, item ->
                if (item == selectedValue) hotel_filter_selection_range_seekbar.progress = filter.options.size - index - 1

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

            if (filter.optionSelected.isEmpty())
                hotel_filter_selection_range_seekbar.progress = hotel_filter_selection_range_seekbar.max

            hotel_filter_selection_range_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    if (filter.options.getOrNull(p1) != null) {
                        if (p1 == filter.options.lastIndex) onSelectedFilterChangedListener.onSelectedFilterChanged(filterName)
                        else {
                            onSelectedFilterChangedListener.onSelectedFilterChanged(filterName,
                                    mutableListOf(filter.options[filter.options.size - p1 - 1]))
                        }
                    }
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}

                override fun onStopTrackingTouch(p0: SeekBar?) {}

            })
        }
    }

    companion object {
        val LAYOUT = R.layout.layout_hotel_filter_selection_range
    }
}