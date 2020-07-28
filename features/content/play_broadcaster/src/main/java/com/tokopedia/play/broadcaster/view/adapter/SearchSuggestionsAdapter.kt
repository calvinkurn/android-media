package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.SearchSuggestionUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.SearchSuggestionViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.SearchSuggestionAdapterDelegate

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
        return oldItem.suggestedText == newItem.suggestedText
    }

    override fun areContentsTheSame(oldItem: SearchSuggestionUiModel, newItem: SearchSuggestionUiModel): Boolean {
        return oldItem == newItem
    }
}