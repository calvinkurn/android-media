package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.layout_autocomplete_single_line_item.view.*

class RecentSearchSingleLineItemViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: BaseItemInitialStateSearch) {
        bindIconSearch(item)
        bindRecentSearchTextView(item)
        bindRemoveButton(item)
        bindListener(item)
    }

    private fun bindIconSearch(item: BaseItemInitialStateSearch) {
        itemView.iconImage?.let {
            ImageHandler.loadImage2(it, item.imageUrl, R.drawable.autocomplete_ic_time)
        }
    }

    private fun bindRecentSearchTextView(item: BaseItemInitialStateSearch) {
        itemView.singleLineTitle?.let {
            it.text = MethodChecker.fromHtml(item.title)
        }
    }

    private fun bindRemoveButton(item: BaseItemInitialStateSearch) {
        itemView.actionShortcutButton?.shouldShowWithAction(item.shortcutImage.isNotEmpty()) {
            ImageHandler.loadImage2(itemView.actionShortcutButton, item.shortcutImage, R.drawable.autocomplete_ic_remove)
        }
    }

    private fun bindListener(item: BaseItemInitialStateSearch){
        itemView.actionShortcutButton?.setOnClickListener { _ -> clickListener.onDeleteRecentSearchItem(item) }
        itemView.autocompleteSingleLineItem?.setOnClickListener { _ -> clickListener.onRecentSearchItemClicked(item) }
    }
}