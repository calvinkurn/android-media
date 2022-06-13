package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analyticsdebugger.debugger.FpmLogger
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.presentation.model.FpmLogOnNotificationUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class FpmLogOnNotificationViewHolder(
    itemView: View
): AbstractViewHolder<FpmLogOnNotificationUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_fpm_log_on_notification
    }

    private var cb: CheckboxUnify? = null

    override fun bind(element: FpmLogOnNotificationUiModel) {
        cb = itemView.findViewById(R.id.fpm_log_on_notification_cb)
        cb?.isChecked = FpmLogger.getInstance()?.isNotificationEnabled ?: false
        cb?.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            FpmLogger.getInstance()?.enableNotification(state)
        }
    }
}