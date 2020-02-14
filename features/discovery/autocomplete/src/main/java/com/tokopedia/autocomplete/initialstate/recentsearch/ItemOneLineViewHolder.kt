package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocomplete.adapter.ItemClickListener
import com.tokopedia.autocomplete.analytics.AutocompleteTracking
import com.tokopedia.autocomplete.initialstate.newfiles.BaseItemInitialStateSearch
import kotlinx.android.synthetic.main.layout_recent_item_autocomplete.view.*

class ItemOneLineViewHolder(itemView: View, private val clickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: BaseItemInitialStateSearch) {
        bindIconSearch(item.imageUrl)
        bindRecentSearchTextView(item.title)
        bindRemoveButton(item.shortcutUrl)
        bindListener(item)
    }

    private fun bindIconSearch(imgUrl: String) {
        itemView.iconSearch?.shouldShowWithAction(imgUrl.isNotEmpty()) {
            ImageHandler.loadImageCircle2(itemView.context, it, imgUrl)
        }
    }

    private fun bindRecentSearchTextView(title: String) {
        itemView.recentSearchTextView?.shouldShowWithAction(title.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(title)
        }
    }

    private fun bindRemoveButton(imgUrl: String) {
        itemView.actionRemoveButton?.shouldShowWithAction(imgUrl.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholder(it, imgUrl)
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

    private fun <T : View> T?.shouldShowWithAction(shouldShow: Boolean, action: (T) -> Unit) {
        if (this == null) return

        if (shouldShow) {
            this.visibility = View.VISIBLE
            action(this)
        } else {
            this.visibility = View.INVISIBLE
        }
    }
}