package com.tokopedia.autocompletecomponent.initialstate.recentview

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import kotlinx.android.synthetic.main.layout_title_auto_complete.view.*

class RecentViewTitleViewHolder(itemView: View) : AbstractViewHolder<RecentViewTitleDataView>(itemView) {

    override fun bind(element: RecentViewTitleDataView) {
        itemView.titleTextView?.let { TextAndContentDescriptionUtil.setTextAndContentDescription(it, element.title, getString(R.string.content_desc_titleTextView)) }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_recent_view
    }
}