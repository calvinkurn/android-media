package com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseEmptyStateNoHistoryUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class RepurchaseEmptyStateNoHistoryViewHolder (
    itemView: View,
    private val listener: RepurchaseEmptyStateNoHistoryListener? = null
): AbstractViewHolder<RepurchaseEmptyStateNoHistoryUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_repurchase_empty_state_no_history
    }

    override fun bind(element: RepurchaseEmptyStateNoHistoryUiModel) {
        val tpTitle = itemView.findViewById<Typography>(R.id.tp_title)
        val tpDesc = itemView.findViewById<Typography>(R.id.tp_desc)
        val btnOpen = itemView.findViewById<UnifyButton>(R.id.btn_open_tokopedianow)
        tpTitle.text = getString(element.title)
        tpDesc.text = getString(element.description)
        btnOpen.setOnClickListener {
            listener?.onClickEmptyStateNoHistoryBtn()
        }
    }

    interface RepurchaseEmptyStateNoHistoryListener {
        fun onClickEmptyStateNoHistoryBtn()
    }
}
