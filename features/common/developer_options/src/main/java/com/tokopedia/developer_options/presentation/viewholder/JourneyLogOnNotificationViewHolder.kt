package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.journeydebugger.JourneyLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.JourneyLogOnNotificationUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class JourneyLogOnNotificationViewHolder(
    itemView: View
): AbstractViewHolder<JourneyLogOnNotificationUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_journey_log_on_notification
    }

    override fun bind(element: JourneyLogOnNotificationUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.journey_log_on_notification_cb)
        cb.isChecked = JourneyLogger.getInstance(itemView.context).isNotificationEnabled
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            JourneyLogger.getInstance(itemView.context).enableNotification(state)
        }
    }
}
