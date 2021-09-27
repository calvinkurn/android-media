package com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import kotlinx.android.synthetic.main.item_tokopedianow_repurchase_empty_state_no_history.view.*

class RepurchaseEmptyStateNoHistoryViewHolder (
    itemView: View,
    private val listener: RepurchaseEmptyStateNoHistoryListener? = null
): AbstractViewHolder<RepurchaseEmptyStateNoHistoryUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_repurchase_empty_state_no_history
    }

    override fun bind(element: RepurchaseEmptyStateNoHistoryUiModel) {
        itemView.tp_desc.text = getString(element.description)
        itemView.btn_open_tokopedianow.setOnClickListener {
            listener?.onClickEmptyStateNoHistoryBtn()
        }
    }

    interface RepurchaseEmptyStateNoHistoryListener {
        fun onClickEmptyStateNoHistoryBtn()
    }
}
