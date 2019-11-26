package com.tokopedia.similarsearch.emptyresult

import android.view.View
import com.tokopedia.similarsearch.R
import com.tokopedia.similarsearch.abstraction.BaseViewHolder

internal class EmptyResultViewHolder(itemView: View): BaseViewHolder<EmptyResultViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.similar_search_empty_result_layout
    }

    override fun bind(item: EmptyResultViewModel) {

    }
}
