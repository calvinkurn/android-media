package com.tokopedia.similarsearch.emptyresult

import android.view.View
import com.tokopedia.similarsearch.R
import com.tokopedia.similarsearch.abstraction.BaseViewHolder
import kotlinx.android.synthetic.main.similar_search_empty_result_layout.view.*

internal class EmptyResultViewHolder(
        itemView: View,
        private val emptyResultListener: EmptyResultListener
): BaseViewHolder<EmptyResultViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.similar_search_empty_result_layout
    }

    override fun bind(item: EmptyResultViewModel) {
        itemView.similarSearchEmptyButton?.setOnClickListener {
            emptyResultListener.onEmptyResultButtonClicked()
        }
    }
}
