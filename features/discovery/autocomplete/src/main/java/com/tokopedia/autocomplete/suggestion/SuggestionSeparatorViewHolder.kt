package com.tokopedia.autocomplete.suggestion

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import kotlinx.android.synthetic.main.layout_autocomplete_suggestion_separator.view.*

class SuggestionSeparatorViewHolder(itemView: View) : AbstractViewHolder<SuggestionSeparatorDataView>(itemView) {

    override fun bind(element: SuggestionSeparatorDataView) {
        itemView.autocompleteSuggestionDoubleLineWithoutImageSeparator?.visibility = View.VISIBLE
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_suggestion_separator
    }
}