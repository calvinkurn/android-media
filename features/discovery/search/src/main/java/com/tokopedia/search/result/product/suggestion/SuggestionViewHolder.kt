package com.tokopedia.search.result.product.suggestion

import android.text.Html
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductSuggestionLayoutBinding
import com.tokopedia.utils.view.binding.viewBinding

class SuggestionViewHolder(
    itemView: View,
    private val suggestionListener: SuggestionListener,
    private val isReimagine: Boolean = false
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
        setPadding()
        val suggestionTextView = binding?.root ?: return

        suggestionTextView.addOnImpressionListener(element) {
            suggestionListener.onSuggestionImpressed(element)
        }

        suggestionTextView.shouldShowWithAction(element.suggestionText.isNotEmpty()) {
            suggestionTextView.text = Html.fromHtml(element.suggestionText)
            suggestionTextView.setOnClickListener {
                if (element.suggestedQuery.isNotEmpty()) {
                    suggestionListener.onSuggestionClicked(element)
                }
            }
        }
    }

    private fun setPadding() {
        if (isReimagine) {
            paddingReimagineVersion()
        } else {
            paddingControlVersion()
        }
    }

    private fun paddingReimagineVersion() {
        val suggestionTextView = binding?.suggestionTextView ?: return
        val contextResource = suggestionTextView.context.resources
        val paddingTop = contextResource.getDimensionPixelSize(R.dimen.search_suggestion_inspiration_carousel_padding_top_reimagine)
        val paddingLeftRight = contextResource.getDimensionPixelSize(R.dimen.search_suggestion_inspiration_carousel_padding_left_right)
        suggestionTextView.setPadding(paddingLeftRight, paddingTop, paddingLeftRight, 0)
    }

    private fun paddingControlVersion() {
        val suggestionTextView = binding?.suggestionTextView ?: return
        val contextResource = suggestionTextView.context.resources
        val paddingTop = contextResource.getDimensionPixelSize(R.dimen.search_suggestion_inspiration_carousel_padding_top)
        val paddingLeftRight = contextResource.getDimensionPixelSize(R.dimen.search_suggestion_inspiration_carousel_padding_left_right)
        val paddingBottom = contextResource.getDimensionPixelSize(R.dimen.search_suggestion_inspiration_carousel_padding_bottom)
        suggestionTextView.setPadding(paddingLeftRight, paddingTop, paddingLeftRight, paddingBottom)
    }
}
