package com.tokopedia.autocompletecomponent.initialstate.recentsearch

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutTitleRecentSearchBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.utils.view.binding.viewBinding

class RecentSearchTitleViewHolder(
    itemView: View,
    private val listener: RecentSearchListener,
) : AbstractViewHolder<RecentSearchTitleDataView>(itemView) {

    private var binding: LayoutTitleRecentSearchBinding? by viewBinding()

    override fun bind(element: RecentSearchTitleDataView) {
        bindTitle(element)
        bindActionDeleteButton(element)
    }

    private fun bindTitle(item: RecentSearchTitleDataView) {
        binding?.recentSearchTitleLayout?.titleTextView?.let {
            TextAndContentDescriptionUtil.setTextAndContentDescription(
                it,
                item.title,
                getString(R.string.content_desc_titleTextView)
            )
        }
    }

    private fun bindActionDeleteButton(item: RecentSearchTitleDataView) {
        val actionDeleteButton = binding?.recentSearchTitleLayout?.actionDeleteButton ?: return

        actionDeleteButton.shouldShowWithAction(item.labelAction.isNotEmpty()) {
            actionDeleteButton.text = item.labelAction
            actionDeleteButton.setOnClickListener { listener.onDeleteAllRecentSearch() }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_recent_search
    }
}