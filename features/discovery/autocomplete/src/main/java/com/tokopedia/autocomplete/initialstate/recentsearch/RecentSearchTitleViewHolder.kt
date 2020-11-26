package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.layout_title_auto_complete.view.*

class RecentSearchTitleViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : AbstractViewHolder<RecentSearchTitleViewModel>(itemView) {

    override fun bind(element: RecentSearchTitleViewModel) {
        bindTitle(element)
        bindActionDeleteButton(element)
    }

    private fun bindTitle(item: RecentSearchTitleViewModel) {
        itemView.titleTextView?.text = item.title
    }

    private fun bindActionDeleteButton(item: RecentSearchTitleViewModel) {
        itemView.actionDeleteButton?.shouldShowWithAction(item.labelAction.isNotEmpty()) {
            itemView.actionDeleteButton?.text = item.labelAction
            itemView.actionDeleteButton?.setOnClickListener { clickListener.onDeleteAllRecentSearch() }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_recent_search
    }
}