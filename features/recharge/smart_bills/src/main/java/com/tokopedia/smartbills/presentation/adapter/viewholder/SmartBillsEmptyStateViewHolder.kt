package com.tokopedia.smartbills.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.databinding.ViewSmartBillsEmptyStateBinding
import com.tokopedia.utils.view.binding.viewBinding

class SmartBillsEmptyStateViewHolder(itemView: View, val listener: EmptyStateSBMListener) : AbstractViewHolder<EmptyModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_smart_bills_empty_state
    }

    private val binding: ViewSmartBillsEmptyStateBinding? by viewBinding()

    override fun bind(element: EmptyModel) {
        binding?.run {
            smartBillsEmptyStateButton.setOnClickListener {
                listener.clickEmptyButton()
            }
        }
    }

    interface EmptyStateSBMListener {
        fun clickEmptyButton()
    }
}
