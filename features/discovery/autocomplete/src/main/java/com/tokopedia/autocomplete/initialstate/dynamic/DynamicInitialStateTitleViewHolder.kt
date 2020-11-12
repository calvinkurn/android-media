package com.tokopedia.autocomplete.initialstate.dynamic

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.layout_title_dynamic_initial_state.view.*

class DynamicInitialStateTitleViewHolder(itemView: View, private val clickListener: InitialStateItemClickListener) : AbstractViewHolder<DynamicInitialStateTitleViewModel>(itemView) {

    override fun bind(element: DynamicInitialStateTitleViewModel) {
        bindTitle(element)
        bindActionButton(element)
    }

    private fun bindTitle(item: DynamicInitialStateTitleViewModel) {
        itemView.initialStateDynamicTitle?.text = item.title
    }

    private fun bindActionButton(item: DynamicInitialStateTitleViewModel) {
        itemView.initialStateDynamicButton?.shouldShowWithAction(item.labelAction.isNotEmpty()) {
            itemView.initialStateDynamicButton?.text = item.labelAction
            itemView.initialStateDynamicButton?.setOnClickListener {
                itemView.initialStateDynamicButton?.isClickable = false
                clickListener.onRefreshDynamicSection(item.featureId)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_dynamic_initial_state
    }
}