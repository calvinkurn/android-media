package com.tokopedia.autocompletecomponent.initialstate.dynamic

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutTitleDynamicInitialStateBinding
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

class DynamicInitialStateTitleViewHolder(
    itemView: View,
    private val listener: DynamicInitialStateListener
) : AbstractViewHolder<DynamicInitialStateTitleDataView>(itemView) {

    private var binding: LayoutTitleDynamicInitialStateBinding? by viewBinding()

    override fun bind(element: DynamicInitialStateTitleDataView) {
        bindTitle(element)
        bindActionButton(element)
    }

    private fun bindTitle(item: DynamicInitialStateTitleDataView) {
        binding?.initialStateDynamicTitle?.text = item.title
    }

    private fun bindActionButton(item: DynamicInitialStateTitleDataView) {
        val actionButton = binding?.initialStateDynamicButton ?: return

        actionButton.shouldShowWithAction(item.labelAction.isNotEmpty()) {
            actionButton.text = item.labelAction
            actionButton.isEnabled = true
            actionButton.setOnClickListener {
                actionButton.isEnabled = false
                listener.onRefreshDynamicSection(item.featureId)
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_title_dynamic_initial_state
    }
}