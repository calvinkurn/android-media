package com.tokopedia.reviewseller.feature.reviewlist.view.viewholder

import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewlist.view.model.FilterAndSortModel
import com.tokopedia.unifycomponents.ChipsUnify

class FilterAndSortViewHolder(itemView: View): AbstractViewHolder<FilterAndSortModel>(itemView) {

    private val chipsFilter = itemView.findViewById<ChipsUnify>(R.id.review_period_filter_button)
    private val chipsSort = itemView.findViewById<ChipsUnify>(R.id.review_sort_button)

    override fun bind(element: FilterAndSortModel?) {
        setupChips()
//        itemView.review_period_filter_button.chip_text.text = element?.filter
    }

    private fun setupChips() {
        val chipsPaddingSize = itemView.resources.getDimension(R.dimen.chips_padding_size).toInt()
        val chipsTextSize = itemView.resources.getDimension(R.dimen.chips_font_size)
        //chipsFilter.chip_container.background = ResourcesCompat.getDrawable(itemView.resources, R.drawable.order_filter_selector_chip, null)

        chipsFilter.chip_container.setPadding(chipsPaddingSize, chipsPaddingSize, chipsPaddingSize, chipsPaddingSize)
        chipsFilter.chip_text.textSize = chipsTextSize

        chipsSort.chip_container.setPadding(chipsPaddingSize, chipsPaddingSize, chipsPaddingSize, chipsPaddingSize)
        chipsSort.chip_text.textSize = chipsTextSize

        chipsFilter.setChevronClickListener {
            Log.d("Test", "rafli")
        }

        chipsSort.setChevronClickListener {
            Log.d("lalala", "tes")
        }

    }

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_review_list_filter_and_sort
    }
}