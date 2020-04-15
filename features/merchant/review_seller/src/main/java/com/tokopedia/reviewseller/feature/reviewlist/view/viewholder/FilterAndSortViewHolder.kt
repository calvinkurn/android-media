package com.tokopedia.reviewseller.feature.reviewlist.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewlist.view.model.FilterAndSortModel
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.toPx

class FilterAndSortViewHolder(val view: View,
                              private var listener: FilterAndSortListener): AbstractViewHolder<FilterAndSortModel>(view) {

    private var chipsFilter: ChipsUnify? = null
    private var chipsSort: ChipsUnify? = null

    init {
        chipsFilter = view.findViewById(R.id.review_period_filter_button)
        chipsSort = view.findViewById(R.id.review_sort_button)
    }

    override fun bind(element: FilterAndSortModel?) {
        setupChips(element)
    }

    private fun setupChips(element: FilterAndSortModel?) {
        val chipsPaddingRight = 8.toPx()
        val maxChipTextWidth = 116.toPx()
        val chipsTextSize = itemView.resources.getDimension(R.dimen.chips_font_size)

        chipsFilter?.chip_text?.text = element?.filter
        chipsSort?.chip_text?.text = element?.sort

        chipsFilter?.chip_right_icon?.setPadding( 0, 0, chipsPaddingRight, 0)

        chipsSort?.chip_right_icon?.setPadding( 0, 0, chipsPaddingRight, 0)

//        chipsSort?.chip_text?.width = maxChipTextWidth
        chipsSort?.chip_text?.textSize = chipsTextSize
        chipsFilter?.chip_text?.textSize = chipsTextSize

        chipsFilter?.apply {
            setOnClickListener {
                listener.onFilterActionClicked(view, chipsFilter?.chipText.toString())
            }
            setChevronClickListener {
                listener.onFilterActionClicked(view, chipsFilter?.chipText.toString())
            }
        }

        chipsSort?.apply {
            setOnClickListener {
                listener.onSortActionClicked(view, chipsSort?.chipText.toString())
            }
            setChevronClickListener {
                listener.onSortActionClicked(view, chipsSort?.chipText.toString())
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_review_list_filter_and_sort
    }

    interface FilterAndSortListener {
        fun onFilterActionClicked(view: View, filter: String)
        fun onSortActionClicked(view: View, sort: String)
    }
}