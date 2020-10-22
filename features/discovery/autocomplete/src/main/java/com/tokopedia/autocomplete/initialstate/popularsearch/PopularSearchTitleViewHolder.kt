package com.tokopedia.autocomplete.initialstate.popularsearch

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.layout_title_popular_search.view.*
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil;

class PopularSearchTitleViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : AbstractViewHolder<PopularSearchTitleViewModel>(itemView) {

    override fun bind(element: PopularSearchTitleViewModel) {
        bindTitle(element)
        bindActionRefreshButton(element)
    }

    private fun bindTitle(item: PopularSearchTitleViewModel) {
        itemView.titleTextView?.let { TextAndContentDescriptionUtil.setTextAndContentDescription(it, item.title, getString(R.string.content_desc_titleTextView)) }
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