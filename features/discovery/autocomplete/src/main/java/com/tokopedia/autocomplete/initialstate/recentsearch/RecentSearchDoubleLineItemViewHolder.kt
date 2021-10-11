package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.BaseItemInitialStateSearch
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.layout_autocomplete_double_line_item.view.*

class RecentSearchDoubleLineItemViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : RecyclerView.ViewHolder(itemView) {

    fun bind(item: BaseItemInitialStateSearch) {
        bindIconImage(item)
        bindIconTitle(item)
        bindIconSubtitle(item)
        bindSubtitle(item)
        bindTitle(item)
        bindLabel(item)
        bindRemoveButton(item)
        bindListener(item)
    }

    private fun bindIconImage(item: BaseItemInitialStateSearch) {
        itemView.iconImage?.showWithAction(item.imageUrl.isNotEmpty()) {
            ImageHandler.loadImageCircle2(itemView.context, it, item.imageUrl)
        }
    }

    private fun bindIconTitle(item: BaseItemInitialStateSearch) {
        itemView.iconTitle?.shouldShowOrHideWithAction(item.iconTitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(it, item.iconTitle)
        }
    }

    private fun bindIconSubtitle(item: BaseItemInitialStateSearch) {
        itemView.iconSubtitle?.shouldShowOrHideWithAction(item.iconSubtitle.isNotEmpty()) {
            ImageHandler.loadImageWithoutPlaceholderAndError(it, item.iconSubtitle)
        }
    }

    private fun bindSubtitle(item: BaseItemInitialStateSearch) {
        itemView.doubleLineSubtitle?.setTextAndCheckShow(MethodChecker.fromHtml(item.subtitle).toString())
    }

    private fun bindTitle(item: BaseItemInitialStateSearch) {
        itemView.doubleLineTitle?.setTextAndCheckShow(MethodChecker.fromHtml(item.title).toString())

        setTitleMaxLine(item)
    }

    private fun setTitleMaxLine(item: BaseItemInitialStateSearch) {
        if (item.subtitle.isEmpty()) {
            itemView.doubleLineTitle?.maxLines = 2
        }
    }

    private fun bindLabel(item: BaseItemInitialStateSearch) {
        itemView.doubleLineLabel?.setTextAndCheckShow(item.label)
        if(itemView.doubleLineLabel.text.isNotEmpty()) {
            itemView.doubleLineLabel?.setLabelType(0)
        }
    }

    private fun bindRemoveButton(item: BaseItemInitialStateSearch) {
        itemView.actionShortcutButton?.shouldShowWithAction(item.shortcutImage.isNotEmpty()) {
        ImageHandler.loadImage2(itemView.actionShortcutButton, item.shortcutImage, R.drawable.autocomplete_ic_remove)
        }
    }

    private fun bindListener(item: BaseItemInitialStateSearch) {
        itemView.actionShortcutButton?.setOnClickListener { _ -> clickListener.onDeleteRecentSearchItem(item) }
        itemView.autocompleteDoubleLineItem?.setOnClickListener { _ -> clickListener.onRecentSearchItemClicked(item) }
    }

    private fun <T : View> T?.showWithAction(shouldShow: Boolean, action: (T) -> Unit) {
        if (this == null) return

        if (shouldShow) {
            action(this)
        }
    }

    private fun <T : View> T?.shouldShowOrHideWithAction(shouldShow: Boolean, action: (T) -> Unit) {
        if (this == null) return

        if (shouldShow) {
            visibility = View.VISIBLE
            action(this)
        }else {
            visibility = View.GONE
        }
    }
}