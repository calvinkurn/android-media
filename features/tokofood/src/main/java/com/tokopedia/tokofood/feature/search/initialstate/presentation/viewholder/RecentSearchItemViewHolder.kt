package com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.databinding.RecentSearchItemInitialStateBinding
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.CuisineItemUiModel
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.RecentSearchItemUiModel

class RecentSearchItemViewHolder(view: View,
                                 private val actionListener: ActionListener
): CustomPayloadViewHolder<RecentSearchItemUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.recent_search_item_initial_state
    }

    private val binding = RecentSearchItemInitialStateBinding.bind(itemView)

    override fun bind(element: RecentSearchItemUiModel) {
        bindImpressionRecentSearchListener(element, adapterPosition)
        setRecentSearchItem(element.title)
        setRecentSearchLabel(element.title)
        setRecentSearchAction(element)
    }

    override fun bindPayload(payloads: Pair<*, *>?) {
        super.bindPayload(payloads)
        payloads?.let { (oldItem, newItem) ->
            if (oldItem is RecentSearchItemUiModel && newItem is RecentSearchItemUiModel) {
                if (oldItem.title != newItem.title) {
                    setRecentSearchLabel(newItem.title)
                    setRecentSearchItem(newItem.title)
                }
                if (oldItem != newItem) {
                    setRecentSearchAction(newItem)
                }
            }
        }
    }

    private fun setRecentSearchLabel(title: String) {
        with(binding) {
            tvLabelRecentSearch.text = title
        }
    }

    private fun setRecentSearchAction(element: RecentSearchItemUiModel) {
        with(binding) {
            ivDeleteRecentSearch.setImageUrl(element.imageActionUrl)
            ivDeleteRecentSearch.setOnClickListener {
                actionListener.onDeleteRecentSearchClicked(element.itemId, adapterPosition)
            }
        }
    }

    private fun setRecentSearchItem(title: String) {
        itemView.setOnClickListener {
            actionListener.onRecentSearchItemClicked(title)
        }
    }

    private fun bindImpressionRecentSearchListener(
        item: RecentSearchItemUiModel,
        position: Int
    ) {
        binding.root.addOnImpressionListener(
            item,
            createViewHintListener(item, position)
        )
    }

    private fun createViewHintListener(
        item: RecentSearchItemUiModel,
        position: Int
    ): ViewHintListener {
        return object : ViewHintListener {
            override fun onViewHint() {
                actionListener.onImpressionRecentSearch(item, position)
            }
        }
    }

    interface ActionListener {
        fun onDeleteRecentSearchClicked(itemId: String, position: Int)
        fun onRecentSearchItemClicked(title: String)
        fun onImpressionRecentSearch(item: RecentSearchItemUiModel, position: Int)
    }
}