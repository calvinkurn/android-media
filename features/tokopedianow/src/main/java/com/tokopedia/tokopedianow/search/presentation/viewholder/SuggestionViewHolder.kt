package com.tokopedia.tokopedianow.search.presentation.viewholder

import android.text.Html
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.search.presentation.listener.SuggestionListener
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.unifyprinciples.Typography

class SuggestionViewHolder(
        itemView: View,
        private val suggestionListener: SuggestionListener
): AbstractViewHolder<SuggestionDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.item_tokomart_search_suggestion
    }

    private val suggestionText: Typography? =
            itemView.findViewById(R.id.tokomartSearchSuggestionText)

    override fun bind(element: SuggestionDataView) {
        bindSuggestionView(element)
    }

    private fun bindSuggestionView(element: SuggestionDataView) {
        itemView.shouldShowWithAction(element.text.isNotEmpty()) {
            suggestionText?.text = Html.fromHtml(element.text)
            suggestionText?.setOnClickListener {
                if (element.query.isNotEmpty()) {
                    suggestionListener.onSuggestionClicked(element)
                }
            }
        }
    }
}