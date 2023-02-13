package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.config.DevOptConfig
import com.tokopedia.developer_options.presentation.model.NetworkLogOnNotificationUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

class NetworkLogOnNotificationViewHolder(
    itemView: View
): AbstractViewHolder<NetworkLogOnNotificationUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_network_log_on_notification
    }

    override fun bind(element: NetworkLogOnNotificationUiModel) {
        val context = itemView.context
        val cb = itemView.findViewById<CheckboxUnify>(R.id.network_log_on_notification_cb)

        cb.isChecked = DevOptConfig.isChuckNotifEnabled(context)
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            DevOptConfig.setChuckNotifEnabled(context, state)
        }
    }
}
