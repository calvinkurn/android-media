package com.tokopedia.tokopedianow.repurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowRepurchaseEmptyStateNoHistoryBinding
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.utils.view.binding.viewBinding

class RepurchaseEmptyStateNoHistoryViewHolder (
    itemView: View,
    private val listener: RepurchaseEmptyStateNoHistoryListener? = null
): AbstractViewHolder<RepurchaseEmptyStateNoHistoryUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_repurchase_empty_state_no_history
    }

    private var binding: ItemTokopedianowRepurchaseEmptyStateNoHistoryBinding? by viewBinding()

    override fun bind(element: RepurchaseEmptyStateNoHistoryUiModel) {
        binding?.apply {
            listener?.onImpressEmptyStateNoHistory()
            tpTitle.text = getString(element.title)
            tpDesc.text = getString(element.description)
            btnOpenTokopedianow.setOnClickListener {
                listener?.onClickEmptyStateNoHistoryBtn()
            }
        }
    }

    interface RepurchaseEmptyStateNoHistoryListener {
        fun onClickEmptyStateNoHistoryBtn()
        fun onImpressEmptyStateNoHistory()
    }
}
