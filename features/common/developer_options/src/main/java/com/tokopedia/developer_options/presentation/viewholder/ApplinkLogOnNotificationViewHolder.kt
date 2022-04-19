package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.ApplinkLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.ApplinkLogOnNotificationUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class ApplinkLogOnNotificationViewHolder(
    itemView: View
): AbstractViewHolder<ApplinkLogOnNotificationUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_applink_log_on_notification
    }

    override fun bind(element: ApplinkLogOnNotificationUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.applink_log_on_notification_cb)
        cb.isChecked = ApplinkLogger.getInstance(itemView.context).isNotificationEnabled
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            ApplinkLogger.getInstance(itemView.context).enableNotification(state)
        }
    }
}