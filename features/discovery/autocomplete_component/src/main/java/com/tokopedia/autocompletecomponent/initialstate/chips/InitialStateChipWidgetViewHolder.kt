package com.tokopedia.autocompletecomponent.initialstate.chips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.chipwidget.AutocompleteChipDataView
import com.tokopedia.autocompletecomponent.chipwidget.AutocompleteChipWidgetViewListener
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteChipWidgetBinding
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.utils.view.binding.viewBinding

class InitialStateChipWidgetViewHolder(
    itemView: View,
    private val listener: InitialStateChipListener,
): AbstractViewHolder<InitialStateChipWidgetDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_chip_widget
    }

    private var binding: LayoutAutocompleteChipWidgetBinding? by viewBinding()

    override fun bind(element: InitialStateChipWidgetDataView) {
        binding?.autocompleteChipWidgetView?.bindChipWidgetView(
            data = element.list.map(::toAutocompleteChipDataView),
            listener = object : AutocompleteChipWidgetViewListener {
                override fun onChipClicked(item: AutocompleteChipDataView, position: Int) {
                    val baseItemInitialStateSearch = element.list.getOrNull(position) ?: return
                    listener.onChipClicked(baseItemInitialStateSearch)
                }
            }
        )
    }

    private fun toAutocompleteChipDataView(
        baseItemInitialStateSearch: BaseItemInitialStateSearch
    ): AutocompleteChipDataView = AutocompleteChipDataView(
        template = baseItemInitialStateSearch.template,
        type = baseItemInitialStateSearch.type,
        applink = baseItemInitialStateSearch.applink,
        url = baseItemInitialStateSearch.url,
        title = baseItemInitialStateSearch.title,
        dimension90 = baseItemInitialStateSearch.dimension90,
        position = baseItemInitialStateSearch.position,
        featureId = baseItemInitialStateSearch.featureId
    )
}