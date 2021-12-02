package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.TopAdsLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.TopAdsLogOnNotificationUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class TopAdsLogOnNotificationViewHolder(
    itemView: View
): AbstractViewHolder<TopAdsLogOnNotificationUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_topads_log_on_notification
    }

    override fun bind(element: TopAdsLogOnNotificationUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.topads_log_on_notification_cb)
        cb.isChecked = TopAdsLogger.getInstance(itemView.context).isNotificationEnabled
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            TopAdsLogger.getInstance(itemView.context).enableNotification(state)
        }
    }
}