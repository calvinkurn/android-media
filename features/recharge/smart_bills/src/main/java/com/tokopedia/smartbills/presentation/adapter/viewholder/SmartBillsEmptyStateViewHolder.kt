package com.tokopedia.smartbills.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.smartbills.R
import com.tokopedia.smartbills.databinding.ViewSmartBillsEmptyStateBinding

class SmartBillsEmptyStateViewHolder(itemView: View, val listener: EmptyStateSBMListener) : AbstractViewHolder<EmptyModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.view_smart_bills_empty_state
    }

    override fun bind(element: EmptyModel) {
        val binding = ViewSmartBillsEmptyStateBinding.bind(itemView)
        with(binding) {
            smartBillsEmptyStateButton.setOnClickListener {
                listener.clickEmptyButton()
            }
        }
    }

    interface EmptyStateSBMListener {
        fun clickEmptyButton()
    }
}
