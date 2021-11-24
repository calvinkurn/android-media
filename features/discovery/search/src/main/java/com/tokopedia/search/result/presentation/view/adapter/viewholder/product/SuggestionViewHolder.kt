package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.text.Html
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductSuggestionLayoutBinding
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener
import com.tokopedia.utils.view.binding.viewBinding

class SuggestionViewHolder(
        itemView: View,
        private val suggestionListener: SuggestionListener,
) : AbstractViewHolder<SuggestionDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_suggestion_layout
    }

    private var binding: SearchResultProductSuggestionLayoutBinding? by viewBinding()

    override fun bind(element: SuggestionDataView) {
        bindSuggestionView(element)
    }

    private fun bindSuggestionView(element: SuggestionDataView) {
        binding?.root?.let {
            it.shouldShowWithAction(element.suggestionText.isNotEmpty()) {
                it.text = Html.fromHtml(element.suggestionText)
                it.setOnClickListener {
                    if (element.suggestedQuery.isNotEmpty()) {
                        suggestionListener.onSuggestionClicked(element)
                    }
                }
            }
        }
    }
}