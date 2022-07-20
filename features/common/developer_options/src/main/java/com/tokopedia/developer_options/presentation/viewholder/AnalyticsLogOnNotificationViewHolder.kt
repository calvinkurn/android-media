package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.cassava.GtmLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.AnalyticsLogOnNotificationUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class AnalyticsLogOnNotificationViewHolder(
    itemView: View
): AbstractViewHolder<AnalyticsLogOnNotificationUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_analytics_log_on_notification
    }

    override fun bind(element: AnalyticsLogOnNotificationUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.analytics_log_on_notification_cb)
        cb.isChecked = GtmLogger.getInstance(itemView.context).isNotificationEnabled()
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            GtmLogger.getInstance(itemView.context).enableNotification(state)
        }
    }
}