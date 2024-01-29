package com.tokopedia.search.result.product.separator

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.search.R
import com.tokopedia.unifycomponents.toPx

class VerticalSeparatorViewHolder(
    itemView: View,
    private val isReimagineProductCard: Boolean
): AbstractViewHolder<VerticalSeparatorDataView>(itemView) {

    init {
        initHeight()
    }

    private fun initHeight() {
        val height = HEIGHT_DP +
            if (!isReimagineProductCard) MARGIN_TOP_DP + MARGIN_BOTTOM_DP else 0
        itemView.layoutParams?.height = height.toPx()
        itemView.requestLayout()
    }

    override fun bind(element: VerticalSeparatorDataView?) { }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_separator_view_holder

        private val HEIGHT_DP = 8
        private val MARGIN_TOP_DP = 16
        private val MARGIN_BOTTOM_DP = 4
    }
}
