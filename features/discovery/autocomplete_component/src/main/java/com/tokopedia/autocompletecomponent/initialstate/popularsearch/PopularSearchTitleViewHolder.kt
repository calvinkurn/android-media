package com.tokopedia.autocompletecomponent.initialstate.popularsearch

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutTitlePopularSearchBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.contentdescription.TextAndContentDescriptionUtil
import com.tokopedia.utils.view.binding.viewBinding

class PopularSearchTitleViewHolder(
    itemView: View,
    private val listener: PopularSearchListener,
) : AbstractViewHolder<PopularSearchTitleDataView>(itemView) {

    private var binding: LayoutTitlePopularSearchBinding? by viewBinding()

    override fun bind(element: PopularSearchTitleDataView) {
        bindTitle(element)
        bindActionRefreshButton(element)
    }

    private fun bindTitle(item: PopularSearchTitleDataView) {
        binding?.titleTextView?.let {
            TextAndContentDescriptionUtil.setTextAndContentDescription(
                it,
                item.title,
                getString(R.string.content_desc_titleTextView)
            )
        }
    }

    private fun bindActionRefreshButton(item: PopularSearchTitleDataView) {
        val actionRefreshButton = binding?.actionRefreshButton ?: return

        actionRefreshButton.shouldShowWithAction(item.labelAction.isNotEmpty()) {
            actionRefreshButton.text = item.labelAction
            actionRefreshButton.isEnabled = true
            actionRefreshButton.setOnClickListener {
                actionRefreshButton.isEnabled = false
                listener.onRefreshPopularSearch(item.featureId)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_popular_search
    }
}