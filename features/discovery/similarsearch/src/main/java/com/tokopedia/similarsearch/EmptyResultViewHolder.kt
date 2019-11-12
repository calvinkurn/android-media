package com.tokopedia.similarsearch

import android.view.View

internal class EmptyResultViewHolder(itemView: View): BaseViewHolder<EmptyResultViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.similar_search_empty_result_layout
    }

    override fun bind(item: EmptyResultViewModel) {

    }
}
