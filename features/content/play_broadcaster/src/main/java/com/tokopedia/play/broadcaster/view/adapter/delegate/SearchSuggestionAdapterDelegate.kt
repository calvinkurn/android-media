package com.tokopedia.play.broadcaster.view.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.broadcaster.ui.model.SearchSuggestionUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.SearchSuggestionViewHolder

/**
 * Created by jegul on 29/05/20
 */
class SearchSuggestionAdapterDelegate(
        private val listener: SearchSuggestionViewHolder.Listener
) : TypedAdapterDelegate<SearchSuggestionUiModel, SearchSuggestionUiModel, SearchSuggestionViewHolder>(SearchSuggestionViewHolder.LAYOUT) {

    override fun onBindViewHolder(item: SearchSuggestionUiModel, holder: SearchSuggestionViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): SearchSuggestionViewHolder {
        return SearchSuggestionViewHolder(basicView, listener)
    }
}