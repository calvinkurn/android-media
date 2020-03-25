package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.util.openNotificationSetting
import com.tokopedia.unifycomponents.UnifyButton

class PushNotifActivationViewHolder(
        itemView: View?
): AbstractViewHolder<NotificationActivation>(itemView) {

    private val btnActivation = itemView?.findViewById<UnifyButton>(R.id.btn_activation)

    override fun bind(element: NotificationActivation?) {
        btnActivation?.setOnClickListener {
            val context = itemView.context
            context.openNotificationSetting()
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_push_notif_activation
    }

}