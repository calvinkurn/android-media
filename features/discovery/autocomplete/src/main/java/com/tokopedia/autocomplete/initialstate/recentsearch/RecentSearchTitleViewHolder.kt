package com.tokopedia.autocomplete.initialstate.recentsearch

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.adapter.ItemClickListener
import kotlinx.android.synthetic.main.layout_title_auto_complete.view.*

class RecentSearchTitleViewHolder(itemView: View, private val clickListener: ItemClickListener) : AbstractViewHolder<RecentSearchTitleViewModel>(itemView) {

    override fun bind(element: RecentSearchTitleViewModel) {
        itemView.titleTextView?.text = element.title
        itemView.actionDeleteButton?.visibility = if (element.isVisible) View.VISIBLE else View.GONE
        itemView.actionDeleteButton?.setOnClickListener { clickListener.onDeleteAllRecentSearch() }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_recent_search
    }
}