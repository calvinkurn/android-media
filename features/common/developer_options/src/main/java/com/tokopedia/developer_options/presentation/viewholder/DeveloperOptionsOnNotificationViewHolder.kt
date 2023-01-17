package com.tokopedia.developer_options.presentation.viewholder

import android.app.Application
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.config.DevOptConfig
import com.tokopedia.developer_options.notification.DevOptNotificationManager
import com.tokopedia.developer_options.presentation.model.DeveloperOptionsOnNotificationUiModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

/**
 * Created By : Jonathan Darwin on September 29, 2022
 */
class DeveloperOptionsOnNotificationViewHolder(
    itemView: View
): AbstractViewHolder<DeveloperOptionsOnNotificationUiModel>(itemView)
{
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_developer_options_on_notification
    }

    private val context = itemView.context

    private val notificationManager by lazy(LazyThreadSafetyMode.NONE) {
        val application = context.applicationContext as? Application

        if(application == null) null
        else DevOptNotificationManager(application)
    }

    override fun bind(element: DeveloperOptionsOnNotificationUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.cbx_developer_options_on_notificaion)

        cb.isChecked = DevOptConfig.isDevOptOnNotifEnabled(context)
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            DevOptConfig.setDevOptOnNotifEnabled(context, state)

            if(state) notificationManager?.showNotificationIfEnabled()
            else notificationManager?.dismissNotification()
        }
    }
}
