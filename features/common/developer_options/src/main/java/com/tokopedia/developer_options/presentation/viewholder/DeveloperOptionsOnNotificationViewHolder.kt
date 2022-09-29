package com.tokopedia.developer_options.presentation.viewholder

import android.view.View
import android.widget.CompoundButton
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.config.DevOptConfig
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

    override fun bind(element: DeveloperOptionsOnNotificationUiModel) {
        val cb = itemView.findViewById<CheckboxUnify>(R.id.cbx_developer_options_on_notificaion)
        cb.isChecked = DevOptConfig.isDevOptOnNotifEnabled(itemView.context)
        cb.setOnCheckedChangeListener { _: CompoundButton, state: Boolean ->
            DevOptConfig.setDevOptOnNotifEnabled(itemView.context, state)
        }
    }
}
