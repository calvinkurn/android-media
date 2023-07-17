package com.tokopedia.autocompletecomponent.suggestion.doubleline

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteDoubleLineWithoutImageItemBinding
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.SuggestionListener
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class SuggestionDoubleLineWithoutImageViewHolder(
        itemView: View,
        private val listener: SuggestionListener
) : AbstractViewHolder<SuggestionDoubleLineWithoutImageDataDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_double_line_without_image_item
    }
    private var binding: LayoutAutocompleteDoubleLineWithoutImageItemBinding? by viewBinding()

    override fun bind(item: SuggestionDoubleLineWithoutImageDataDataView) {
        bindTitle(item.data)
        bindSubtitle(item.data)
        bindListener(item.data)
    }

    private fun bindTitle(item: BaseSuggestionDataView) {
        binding?.autocompleteSuggestionDoubleLineWithoutImageTitle?.setTextAndCheckShow(item.title)
    }

    private fun bindSubtitle(item: BaseSuggestionDataView) {
        val autocompleteSuggestionDoubleLineWithoutImageSubtitle = binding?.autocompleteSuggestionDoubleLineWithoutImageSubtitle ?: return
        if (item.applink.isNotEmpty()) {
            autocompleteSuggestionDoubleLineWithoutImageSubtitle.setTypography(itemView.context, Typography.BOLD, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
        } else {
            autocompleteSuggestionDoubleLineWithoutImageSubtitle.setTypography(itemView.context, Typography.REGULAR, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)
        }
        autocompleteSuggestionDoubleLineWithoutImageSubtitle.setTextAndCheckShow(item.subtitle)
    }

    private fun Typography.setTypography(context: Context, weightType: Int, color: Int) {
        this.setWeight(weightType)
        setTextColor(ContextCompat.getColor(context, color))
    }

    private fun bindListener(item: BaseSuggestionDataView) {
        if (item.applink.isNotEmpty()) {
            binding?.autocompleteDoubleLineWithoutImageItem?.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }
}