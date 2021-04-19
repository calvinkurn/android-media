package com.tokopedia.autocomplete.suggestion.doubleline

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.suggestion.SuggestionClickListener
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.layout_autocomplete_double_line_without_image_item.view.*

class SuggestionDoubleLineWithoutImageViewHolder(
        itemView: View,
        private val clickListener: SuggestionClickListener
) : AbstractViewHolder<SuggestionDoubleLineWithoutImageDataDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_double_line_without_image_item
    }

    override fun bind(item: SuggestionDoubleLineWithoutImageDataDataView) {
        bindTitle(item)
        bindSubtitle(item)
        bindListener(item)
    }

    private fun bindTitle(item: SuggestionDoubleLineWithoutImageDataDataView) {
        itemView.autocompleteSuggestionDoubleLineWithoutImageTitle?.setTextAndCheckShow(item.title)
    }

    private fun bindSubtitle(item: SuggestionDoubleLineWithoutImageDataDataView) {
        if (item.hasApplinkUrl()) {
            itemView.autocompleteSuggestionDoubleLineWithoutImageSubtitle?.setTypography(itemView.context, Typography.BOLD, com.tokopedia.unifyprinciples.R.color.Unify_G500)
        } else {
            itemView.autocompleteSuggestionDoubleLineWithoutImageSubtitle?.setTypography(itemView.context, Typography.REGULAR, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
        }
        itemView.autocompleteSuggestionDoubleLineWithoutImageSubtitle?.setTextAndCheckShow(item.subtitle)
    }

    private fun SuggestionDoubleLineWithoutImageDataDataView.hasApplinkUrl(): Boolean {
        return applink.isNotEmpty()
    }

    private fun Typography.setTypography(context: Context, weightType: Int, color: Int) {
        this.setWeight(weightType)
        setTextColor(ContextCompat.getColor(context, color))
    }

    private fun bindListener(item: SuggestionDoubleLineWithoutImageDataDataView) {
        if (item.hasApplinkUrl()) {
            itemView.autocompleteDoubleLineWithoutImageItem?.setOnClickListener {
                clickListener.onItemClicked(item)
            }
        }
    }
}