package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.SearchSuggestionUiModel

/**
 * Created by jegul on 29/05/20
 */
class SearchSuggestionViewHolder(
        itemView: View,
        private val listener: Listener
) : BaseViewHolder(itemView) {

    private val tvSuggestion: TextView = itemView.findViewById(R.id.tv_suggestion)

    fun bind(item: SearchSuggestionUiModel) {
        tvSuggestion.text = item.spannedSuggestion
        itemView.setOnClickListener { listener.onSuggestionClicked(item.spannedSuggestion.toString(), item.suggestedId) }
    }

    companion object {

        val LAYOUT = R.layout.item_play_search_suggestion
    }

    interface Listener {
        fun onSuggestionClicked(suggestionText: String, suggestionId: String)
    }
}