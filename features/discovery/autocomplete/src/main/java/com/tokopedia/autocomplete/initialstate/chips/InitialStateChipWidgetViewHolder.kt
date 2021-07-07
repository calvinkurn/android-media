package com.tokopedia.autocomplete.initialstate.chips

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.chipwidget.AutocompleteChipDataView
import com.tokopedia.autocomplete.chipwidget.AutocompleteChipWidgetViewListener
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import kotlinx.android.synthetic.main.layout_autocomplete_chip_widget.view.*

class InitialStateChipWidgetViewHolder(
        itemView: View,
        private val clickListener: InitialStateItemClickListener
): AbstractViewHolder<InitialStateChipWidgetDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_chip_widget
    }

    override fun bind(element: InitialStateChipWidgetDataView) {
        itemView.autocompleteChipWidgetView?.bindChipWidgetView(
                data = element.toListAutocompleteChipDataView(),
                listener = object : AutocompleteChipWidgetViewListener {
                    override fun onChipClicked(item: AutocompleteChipDataView) {
                        clickListener.onChipClicked(item.toBaseItemInitialStateSearch())
                    }

                }
        )
    }

    private fun InitialStateChipWidgetDataView.toListAutocompleteChipDataView(): List<AutocompleteChipDataView> {
        return this.list.map {
            AutocompleteChipDataView(
                    template = it.template,
                    type = it.type,
                    applink = it.applink,
                    url = it.url,
                    title = it.title,
                    dimension90 = it.dimension90,
                    position = it.position
            )
        }
    }

    private fun AutocompleteChipDataView.toBaseItemInitialStateSearch(): BaseItemInitialStateSearch {
        return BaseItemInitialStateSearch(
                template = template,
                type = type,
                applink = applink,
                url = url,
                title = title,
                dimension90 = dimension90,
                position = position
        )
    }
}