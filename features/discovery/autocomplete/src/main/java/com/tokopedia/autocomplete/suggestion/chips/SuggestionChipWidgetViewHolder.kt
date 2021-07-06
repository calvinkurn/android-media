package com.tokopedia.autocomplete.suggestion.chips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.chipwidget.AutocompleteChipDataView
import com.tokopedia.autocomplete.chipwidget.AutocompleteChipWidgetViewListener
import com.tokopedia.autocomplete.suggestion.BaseSuggestionDataView
import com.tokopedia.autocomplete.suggestion.SuggestionClickListener
import kotlinx.android.synthetic.main.layout_autocomplete_chip_widget.view.*

class SuggestionChipWidgetViewHolder(
        itemView: View,
        private val clickListener: SuggestionClickListener
): AbstractViewHolder<SuggestionChipWidgetDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_chip_widget
    }

    override fun bind(element: SuggestionChipWidgetDataView) {
        itemView.autocompleteChipWidgetView?.bindChipWidgetView(
                data = element.toListAutocompleteChipDataView(),
                listener = object : AutocompleteChipWidgetViewListener {
                    override fun onChipClicked(item: AutocompleteChipDataView) {
                        clickListener.onChipClicked(item.toBaseSuggestionDataViewChildItem())
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
                    title = it.title
            )
        }
    }

    private fun AutocompleteChipDataView.toBaseSuggestionDataViewChildItem(): BaseSuggestionDataView.ChildItem {
        return BaseSuggestionDataView.ChildItem(
                template = template,
                type = type,
                applink = applink,
                url = url,
                title = title
        )
    }
}