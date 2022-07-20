package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.IrisLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.SystemNonSystemAppsUiModel
import com.tokopedia.developer_options.presentation.model.ViewIrisLogUiModel
import com.tokopedia.unifycomponents.UnifyButton

class ViewIrisLogViewHolder(
    itemView: View
): AbstractViewHolder<ViewIrisLogUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_view_iris_log
    }

    override fun bind(element: ViewIrisLogUiModel) {
        val btnViewIrisSaveLog = itemView.findViewById<UnifyButton>(R.id.view_iris_save_log_btn)
        val btnViewIrisSendLog = itemView.findViewById<UnifyButton>(R.id.view_iris_send_log_btn)
        itemView.context.apply {
            btnViewIrisSaveLog.setOnClickListener {
                IrisLogger.getInstance(this).openSaveActivity()
            }
            btnViewIrisSendLog.setOnClickListener {
                IrisLogger.getInstance(this).openSendActivity()
            }
        }
    }
}