package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocomplete.adapter.ItemClickListener
import com.tokopedia.autocomplete.analytics.AutocompleteTracking
import com.tokopedia.autocomplete.initialstate.newfiles.BaseItemInitialStateSearch
import kotlinx.android.synthetic.main.layout_recent_item_autocomplete_two_line.view.*

class ItemTwoLineViewHolder(itemView: View, private val clickListener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: BaseItemInitialStateSearch) {
        bindIconImage(item.imageUrl)
        bindIconTitle(item.iconTitle)
        bindIconSubtitle(item.iconSubtitle)
        bindTitle(item.title)
        bindSubtitle(item.subtitle)
        bindLabel(item.label, 0)
        bindRemoveButton(item.shortcutUrl)
        bindListener(item)
    }

    private fun bindIconImage(imgUrl: String) {
        itemView.iconImage?.shouldShowWithAction(imgUrl.isNotEmpty()) {
            ImageHandler.loadImageCircle2(itemView.context, it, imgUrl)
        }
    }

    private fun bindIconTitle(imgUrl: String) {
        itemView.iconTitle?.shouldShowWithAction(imgUrl.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholder(it, imgUrl)
        }
    }

    private fun bindIconSubtitle(imgUrl: String) {
        itemView.iconTitle?.shouldShowWithAction(imgUrl.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholder(it, imgUrl)
        }
    }

    private fun bindTitle(title: String) {
        itemView.recentSearchTitle?.shouldShowWithAction(title.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(title)
        }
        itemView.recentSearchTitle?.text
    }

    private fun bindSubtitle(subtitle: String) {
        itemView.recentSearchSubtitle?.shouldShowWithAction(subtitle.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(subtitle)
        }
    }

    private fun bindLabel(label: String, labelType: Int) {
        itemView.recentSearchLabel?.shouldShowWithAction(label.isNotEmpty()) {
            it.text = MethodChecker.fromHtml(label)
            it.setLabelType(labelType)
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