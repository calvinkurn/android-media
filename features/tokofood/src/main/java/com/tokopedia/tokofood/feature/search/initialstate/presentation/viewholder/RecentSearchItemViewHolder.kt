package com.tokopedia.tokofood.feature.search.initialstate.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.presentation.listener.TokofoodScrollChangedListener
import com.tokopedia.tokofood.common.presentation.viewholder.CustomPayloadViewHolder
import com.tokopedia.tokofood.common.util.TokofoodExt.addAndReturnImpressionListener
import com.tokopedia.tokofood.databinding.RecentSearchItemInitialStateBinding
import com.tokopedia.tokofood.feature.search.initialstate.presentation.uimodel.RecentSearchItemUiModel

class RecentSearchItemViewHolder(view: View,
                                 private val actionListener: ActionListener,
                                 private val tokofoodScrollChangedListener: TokofoodScrollChangedListener
): CustomPayloadViewHolder<RecentSearchItemUiModel>(view) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.recent_search_item_initial_state
    }

    private val binding = RecentSearchItemInitialStateBinding.bind(itemView)

    override fun bind(element: RecentSearchItemUiModel) {
        bindImpressionRecentSearchListener(element, bindingAdapterPosition)
        setRecentSearchItem(element.title)
        setRecentSearchLabel(element.title)
        setRecentSearchImageUrl(element.imageUrl)
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
                if (oldItem.imageUrl != newItem.imageUrl) {
                    setRecentSearchImageUrl(newItem.imageUrl)
                }
                if (oldItem != newItem) {
                    setRecentSearchAction(newItem)
                }
                bindImpressionRecentSearchListener(newItem, bindingAdapterPosition)
            }
        }
    }

    private fun setRecentSearchImageUrl(imageUrl: String) {
        with(binding) {
            ivInitialStateHistory.setImageUrl(imageUrl)
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
                actionListener.onDeleteRecentSearchClicked(element.itemId, bindingAdapterPosition)
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
        binding.root.addAndReturnImpressionListener(item, tokofoodScrollChangedListener) {
            actionListener.onImpressionRecentSearch(item, position)
        }
    }

    interface ActionListener {
        fun onDeleteRecentSearchClicked(itemId: String, position: Int)
        fun onRecentSearchItemClicked(title: String)
        fun onImpressionRecentSearch(item: RecentSearchItemUiModel, position: Int)
    }
}
