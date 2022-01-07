package com.tokopedia.autocompletecomponent.suggestion.chips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.chipwidget.AutocompleteChipDataView
import com.tokopedia.autocompletecomponent.chipwidget.AutocompleteChipWidgetViewListener
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteChipWidgetBinding
import com.tokopedia.autocompletecomponent.suggestion.BaseSuggestionDataView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class SuggestionChipWidgetViewHolder(
        itemView: View,
        private val chipListener: SuggestionChipListener
): AbstractViewHolder<SuggestionChipWidgetDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_chip_widget
    }

    private var binding: LayoutAutocompleteChipWidgetBinding? by viewBinding()

    override fun bind(element: SuggestionChipWidgetDataView) {
        val chipWidget = binding?.autocompleteChipWidgetView ?: return
        chipWidget.addOnImpressionListener(element.data) {
            chipListener.onChipImpressed(element.data)
        }
        chipWidget.bindChipWidgetView(
            data = element.data.toListAutocompleteChipDataView(),
            listener = object : AutocompleteChipWidgetViewListener {
                override fun onChipClicked(item: AutocompleteChipDataView, position: Int) {
                    val baseSuggestionDataView = element.data
                    val childItem = baseSuggestionDataView.childItems.getOrNull(position) ?: return
                    chipListener.onChipClicked(baseSuggestionDataView, childItem)
                }
            }
        )
    }

    private fun BaseSuggestionDataView.toListAutocompleteChipDataView(): List<AutocompleteChipDataView> {
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