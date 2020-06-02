package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.viewholder.SearchSuggestionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.SearchSuggestionAdapterDelegate
import com.tokopedia.play.broadcaster.view.uimodel.SearchSuggestionUiModel

/**
 * Created by jegul on 29/05/20
 */
class SearchSuggestionsAdapter(
        suggestionListener: SearchSuggestionViewHolder.Listener
) : BaseDiffUtilAdapter<SearchSuggestionUiModel>() {

    init {
        delegatesManager
                .addDelegate(SearchSuggestionAdapterDelegate(suggestionListener))
    }

    override fun areItemsTheSame(oldItem: SearchSuggestionUiModel, newItem: SearchSuggestionUiModel): Boolean {
        return oldItem.suggestionText == newItem.suggestionText
    }

    override fun areContentsTheSame(oldItem: SearchSuggestionUiModel, newItem: SearchSuggestionUiModel): Boolean {
        return oldItem == newItem
    }
}