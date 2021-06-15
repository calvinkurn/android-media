package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.SuggestionDataView
import com.tokopedia.search.result.presentation.view.listener.SuggestionListener

class SuggestionViewHolder(
        itemView: View,
        private val suggestionListener: SuggestionListener,
) : AbstractViewHolder<SuggestionDataView>(itemView) {

    companion object {
        @JvmField
        @LayoutRes
        val LAYOUT = R.layout.search_result_product_suggestion_layout
    }

    private val suggestionText: TextView? = itemView.findViewById(R.id.suggestion_text_view)

    override fun bind(element: SuggestionDataView) {
        bindSuggestionView(element)
    }

    private fun bindSuggestionView(element: SuggestionDataView) {
        itemView.shouldShowWithAction(element.suggestionText.isNotEmpty()) {
            suggestionText?.text = Html.fromHtml(element.suggestionText)
            suggestionText?.setOnClickListener {
                if (element.suggestedQuery.isNotEmpty()) {
                    suggestionListener.onSuggestionClicked(element)
                }
            }
        }
    }
}