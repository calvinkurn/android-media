package com.tokopedia.tokopedianow.search.presentation.viewholder

import android.text.Html
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowSearchSuggestionBinding
import com.tokopedia.tokopedianow.search.presentation.listener.SuggestionListener
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.utils.view.binding.viewBinding

class SuggestionViewHolder(
        itemView: View,
        private val suggestionListener: SuggestionListener
): AbstractViewHolder<SuggestionDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_search_suggestion
    }

    private var binding: ItemTokopedianowSearchSuggestionBinding? by viewBinding()

    override fun bind(element: SuggestionDataView) {
        bindSuggestionView(element)
    }

    private fun bindSuggestionView(element: SuggestionDataView) {
        itemView.shouldShowWithAction(element.text.isNotEmpty()) {
            binding?.apply {
                tokoNowSearchSuggestionText.text = Html.fromHtml(element.text)
                tokoNowSearchSuggestionText.setOnClickListener {
                    if (element.query.isNotEmpty()) {
                        suggestionListener.onSuggestionClicked(element)
                    }
                }
            }
        }
    }
}