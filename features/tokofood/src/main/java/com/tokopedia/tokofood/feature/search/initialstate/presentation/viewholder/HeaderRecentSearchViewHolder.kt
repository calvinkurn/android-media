package com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.databinding.HeaderRecentSearchInitialStateBinding
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.HeaderRecentSearchUiModel

class HeaderRecentSearchViewHolder(
    view: View,
    private val actionListener: ActionListener
) : CustomPayloadViewHolder<HeaderRecentSearchUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.header_recent_search_initial_state
    }

    private val binding = HeaderRecentSearchInitialStateBinding.bind(itemView)

    override fun bind(element: HeaderRecentSearchUiModel) {
        setHeaderLabelRecentSearch(element.headerLabel)
        setHeaderActionRecentSearch(element)
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        super.bindPayload(payloads)
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is HeaderRecentSearchUiModel && newItem is HeaderRecentSearchUiModel) {
                if (oldItem.headerLabel != newItem.headerLabel) {
                    setHeaderLabelRecentSearch(newItem.headerLabel)
                }
                if (oldItem != newItem) {
                    setHeaderActionRecentSearch(newItem)
                }
            }
        }
    }

    private fun setHeaderLabelRecentSearch(headerLabel: String) {
        with(binding) {
            headerLabelInitialState.text = headerLabel
        }
    }

    private fun setHeaderActionRecentSearch(element: HeaderRecentSearchUiModel) {
        with(binding) {
            headerActionInitialState.text = element.labelText
            headerActionInitialState.setOnClickListener {
                actionListener.onHeaderAllRemovedClicked(element.labelAction)
            }
        }
    }

    interface ActionListener {
        fun onHeaderAllRemovedClicked(labelAction: String)
    }
}