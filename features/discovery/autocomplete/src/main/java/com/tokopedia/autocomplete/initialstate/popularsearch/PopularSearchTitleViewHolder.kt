package com.tokopedia.autocomplete.initialstate.popularsearch

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.layout_title_dynamic_initial_state.view.*

class PopularSearchTitleViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : AbstractViewHolder<PopularSearchTitleViewModel>(itemView) {

    override fun bind(element: PopularSearchTitleViewModel) {
        itemView.initialStateDynamicTitle?.text = element.title
        bindActionRefreshButton(element)
    }

    private fun bindActionRefreshButton(item: PopularSearchTitleViewModel) {
        itemView.initialStateDynamicButton?.shouldShowWithAction(item.labelAction.isNotEmpty()) {
            itemView.initialStateDynamicButton?.text = item.labelAction
            itemView.initialStateDynamicButton?.setOnClickListener { clickListener.onRefreshPopularSearch(item.id) }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_dynamic_initial_state
    }
}