package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.analytics.AutocompleteTracking
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import kotlinx.android.synthetic.main.layout_recent_search_single_line_item_autocomplete.view.*

class ItemSingleLineViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: BaseItemInitialStateSearch) {
        bindIconSearch(item)
        bindRecentSearchTextView(item)
        bindRemoveButton(item)
        bindListener(item)
    }

    private fun bindIconSearch(item: BaseItemInitialStateSearch) {
        itemView.iconSearch?.let {
            ImageHandler.loadImage2(it, item.imageUrl, R.drawable.autocomplete_ic_time)
        }
    }

    private fun bindRecentSearchTextView(item: BaseItemInitialStateSearch) {
        itemView.recentSearchTextView?.let {
            it.text = MethodChecker.fromHtml(item.title)
        }
    }

    private fun bindRemoveButton(item: BaseItemInitialStateSearch) {
        itemView.actionRemoveButton?.let {
            ImageHandler.loadImage2(it, item.shortcutImage, R.drawable.autocomplete_ic_remove)
        }
    }

    private fun bindListener(item: BaseItemInitialStateSearch){
        itemView.actionRemoveButton?.setOnClickListener { _ -> clickListener.onDeleteRecentSearchItem(item.title) }
        itemView.autocompleteRecentSearchItem?.setOnClickListener { _ ->
            AutocompleteTracking.eventClickRecentSearch(
                    itemView.context,
                    String.format(
                            "value: %s - po: %s - applink: %s",
                            item.title,
                            (adapterPosition + 1).toString(),
                            item.applink
                    )
            )
            clickListener.onItemClicked(item.applink, item.url)
        }
    }
}