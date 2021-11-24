package com.tokopedia.autocompletecomponent.initialstate.recentview

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutTitleRecentViewBinding
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.utils.view.binding.viewBinding

class RecentViewTitleViewHolder(itemView: View) : AbstractViewHolder<RecentViewTitleDataView>(itemView) {

    private var binding: LayoutTitleRecentViewBinding? by viewBinding()

    override fun bind(element: RecentViewTitleDataView) {
        binding?.recentViewTitleLayout?.titleTextView?.let {
            TextAndContentDescriptionUtil.setTextAndContentDescription(
                it,
                element.title,
                getString(R.string.content_desc_titleTextView)
            )
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_recent_view
    }
}