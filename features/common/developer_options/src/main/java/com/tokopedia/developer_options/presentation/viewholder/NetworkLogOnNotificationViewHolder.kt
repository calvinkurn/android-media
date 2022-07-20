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
        val sharedPref = itemView.context.getSharedPreferences(
            DevOptConfig.CHUCK_ENABLED,
            BaseActivity.MODE_PRIVATE
        )
        val cb = itemView.findViewById<CheckboxUnify>(R.id.network_log_on_notification_cb)
        cb.isChecked = sharedPref.getBoolean(DevOptConfig.IS_CHUCK_ENABLED, false)
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            val editor = sharedPref.edit().putBoolean(
                DevOptConfig.IS_CHUCK_ENABLED,
                state
            )
            editor.apply()
        }
    }
}