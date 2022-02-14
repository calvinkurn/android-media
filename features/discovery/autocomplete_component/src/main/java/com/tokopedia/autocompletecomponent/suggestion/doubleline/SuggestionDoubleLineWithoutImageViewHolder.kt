package com.tokopedia.autocompletecomponent.suggestion.doubleline

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.SuggestionListener
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.layout_autocomplete_double_line_without_image_item.view.*

class SuggestionDoubleLineWithoutImageViewHolder(
        itemView: View,
        private val listener: SuggestionListener
) : AbstractViewHolder<SuggestionDoubleLineWithoutImageDataDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_double_line_without_image_item
    }

    override fun bind(item: SuggestionDoubleLineWithoutImageDataDataView) {
        bindTitle(item.data)
        bindSubtitle(item.data)
        bindListener(item.data)
    }

    private fun bindTitle(item: BaseSuggestionDataView) {
        itemView.autocompleteSuggestionDoubleLineWithoutImageTitle?.setTextAndCheckShow(item.title)
    }

    private fun bindSubtitle(item: BaseSuggestionDataView) {
        if (item.applink.isNotEmpty()) {
            itemView.autocompleteSuggestionDoubleLineWithoutImageSubtitle?.setTypography(itemView.context, Typography.BOLD, com.tokopedia.unifyprinciples.R.color.Unify_G500)
        } else {
            itemView.autocompleteSuggestionDoubleLineWithoutImageSubtitle?.setTypography(itemView.context, Typography.REGULAR, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        }
        itemView.autocompleteSuggestionDoubleLineWithoutImageSubtitle?.setTextAndCheckShow(item.subtitle)
    }

    private fun Typography.setTypography(context: Context, weightType: Int, color: Int) {
        this.setWeight(weightType)
        setTextColor(ContextCompat.getColor(context, color))
    }

    private fun bindListener(item: BaseSuggestionDataView) {
        if (item.applink.isNotEmpty()) {
            itemView.autocompleteDoubleLineWithoutImageItem?.setOnClickListener {
                listener.onItemClicked(item)
            }
        }
    }
}