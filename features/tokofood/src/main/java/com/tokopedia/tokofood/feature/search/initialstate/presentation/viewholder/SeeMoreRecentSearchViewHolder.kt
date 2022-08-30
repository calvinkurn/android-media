package com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.databinding.SeeMoreRecentSearchItemInitialStateBinding
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.SeeMoreRecentSearchUiModel

class SeeMoreRecentSearchViewHolder(
    view: View,
    private val actionListener: ActionListener
) : CustomPayloadViewHolder<SeeMoreRecentSearchUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.see_more_recent_search_item_initial_state
    }

    private val binding = SeeMoreRecentSearchItemInitialStateBinding.bind(itemView)

    override fun bind(element: SeeMoreRecentSearchUiModel) {
        setSeeMoreRecentSearchBtn(element)
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        super.bindPayload(payloads)
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is SeeMoreRecentSearchUiModel && newItem is SeeMoreRecentSearchUiModel) {
                if (oldItem != newItem) {
                    setSeeMoreRecentSearchBtn(newItem)
                }
            }
        }
    }

    private fun setSeeMoreRecentSearchBtn(element: SeeMoreRecentSearchUiModel) {
        with(binding) {
            seeMoreRecentSearchBtn.setOnClickListener {
                actionListener.onSeeMoreRecentSearchBtnClicked(element)
            }
        }
    }

    interface ActionListener {
        fun onSeeMoreRecentSearchBtnClicked(element: SeeMoreRecentSearchUiModel)
    }
}