package com.tokopedia.similarsearch.emptyresult

import android.view.View
import com.tokopedia.similarsearch.R
import com.tokopedia.similarsearch.abstraction.BaseViewHolder
import com.tokopedia.similarsearch.databinding.SimilarSearchEmptyResultLayoutBinding
import com.tokopedia.utils.view.binding.viewBinding

internal class EmptyResultViewHolder(
        itemView: View,
        private val emptyResultListener: EmptyResultListener
): BaseViewHolder<EmptyResultViewModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.similar_search_empty_result_layout
    }
    private var binding: SimilarSearchEmptyResultLayoutBinding? by viewBinding()

    override fun bind(item: EmptyResultViewModel) {
        binding?.similarSearchEmptyButton?.setOnClickListener {
            emptyResultListener.onEmptyResultButtonClicked()
        }
    }
}
