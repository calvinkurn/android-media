package com.tokopedia.autocompletecomponent.suggestion.chips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.chipwidget.AutocompleteChipDataView
import com.tokopedia.autocompletecomponent.chipwidget.AutocompleteChipWidgetViewListener
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.autocompletecomponent.suggestion.SuggestionListener
import kotlinx.android.synthetic.main.layout_autocomplete_chip_widget.view.*

class SuggestionChipWidgetViewHolder(
        itemView: View,
        private val clickListener: SuggestionListener
): AbstractViewHolder<SuggestionChipWidgetDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_chip_widget
    }

    override fun bind(element: SuggestionChipWidgetDataView) {
        itemView.autocompleteChipWidgetView?.bindChipWidgetView(
            data = element.toListAutocompleteChipDataView(),
            listener = object : AutocompleteChipWidgetViewListener {
                override fun onChipClicked(item: AutocompleteChipDataView, position: Int) {
                    val baseSuggestionDataView = element.childItems.getOrNull(position) ?: return
                    clickListener.onChipClicked(baseSuggestionDataView)
                }
            }
        )
    }

    private fun SuggestionChipWidgetDataView.toListAutocompleteChipDataView(): List<AutocompleteChipDataView> {
        return this.childItems.map {
            AutocompleteChipDataView(
                    template = it.template,
                    type = it.type,
                    applink = it.applink,
                    url = it.url,
                    title = it.title,
                    searchTerm = it.searchTerm,
                    dimension90 = dimension90,
                    position = position
            )
        }
    }
}