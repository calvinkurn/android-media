package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.FpmLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ViewFpmLogUiModel
import com.tokopedia.unifycomponents.UnifyButton

class ViewFpmLogViewHolder(
    itemView: View
): AbstractViewHolder<ViewFpmLogUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_fpm_log
    }

    override fun bind(element: ViewFpmLogUiModel) {
        val btn = itemView.findViewById<UnifyButton>(R.id.view_fpm_log_btn)
        btn.setOnClickListener {
            FpmLogger.getInstance()?.openActivity()
        }
    }
}