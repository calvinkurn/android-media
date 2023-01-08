package com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.databinding.HeaderItemInitialStateBinding
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.ChipsListUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.HeaderItemInitialStateUiModel

class HeaderItemInitStateViewHolder(view: View) :
    CustomPayloadViewHolder<HeaderItemInitialStateUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.header_item_initial_state
    }

    private val binding = HeaderItemInitialStateBinding.bind(itemView)

    override fun bind(element: HeaderItemInitialStateUiModel) {
        seHeaderInitialState(element)
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        super.bindPayload(payloads)
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is HeaderItemInitialStateUiModel && newItem is HeaderItemInitialStateUiModel) {
                if (oldItem != newItem) {
                    seHeaderInitialState(newItem)
                }
            }
        }
    }

    private fun seHeaderInitialState(element: HeaderItemInitialStateUiModel) {
        with(binding) {
            headerLabelInitialState.text = element.headerLabel
        }
    }
}