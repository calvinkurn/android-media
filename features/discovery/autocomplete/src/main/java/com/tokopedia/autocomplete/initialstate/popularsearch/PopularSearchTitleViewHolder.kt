package com.tokopedia.autocomplete.initialstate.popularsearch

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.layout_title_popular_search.view.*

class PopularSearchTitleViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : AbstractViewHolder<PopularSearchTitleViewModel>(itemView) {

    override fun bind(element: PopularSearchTitleViewModel) {
        itemView.titleTextView?.text = element.title
        bindActionRefreshButton(element)
    }

    private fun bindActionRefreshButton(item: PopularSearchTitleViewModel) {
        itemView.actionRefreshButton?.shouldShowWithAction(item.labelAction.isNotEmpty()) {
            itemView.actionRefreshButton?.text = item.labelAction
            itemView.actionRefreshButton?.setOnClickListener { clickListener.onRefreshPopularSearch(item.featureId) }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_popular_search
    }
}