package com.tokopedia.autocomplete.initialstate.recentview

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import kotlinx.android.synthetic.main.layout_title_auto_complete.view.*

class RecentViewTitleViewHolder(itemView: View) : AbstractViewHolder<RecentViewTitleViewModel>(itemView) {

    override fun bind(element: RecentViewTitleViewModel) {
        itemView.titleTextView?.text = element.title
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_recent_view
    }
}