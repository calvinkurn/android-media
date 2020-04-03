package com.tokopedia.autocomplete.initialstate.popularsearch

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import kotlinx.android.synthetic.main.layout_title_auto_complete.view.*

class PopularSearchTitleViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : AbstractViewHolder<PopularSearchTitleViewModel>(itemView) {

    override fun bind(element: PopularSearchTitleViewModel) {
        itemView.titleTextView?.text = element.title
        itemView.actionRefreshButton?.visibility = if (element.isVisible) View.VISIBLE else View.GONE
        itemView.actionRefreshButton?.setOnClickListener { clickListener.onRefreshPopularSearch() }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_popular_search
    }
}