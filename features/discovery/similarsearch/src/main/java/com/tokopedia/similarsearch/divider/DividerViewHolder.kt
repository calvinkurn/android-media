package com.tokopedia.similarsearch.divider

import android.view.View
import com.tokopedia.similarsearch.R
import com.tokopedia.similarsearch.abstraction.BaseViewHolder

internal class DividerViewHolder(itemView: View): BaseViewHolder<DividerViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.similar_search_divider_layout
    }

    override fun bind(item: DividerViewModel) {

    }
}