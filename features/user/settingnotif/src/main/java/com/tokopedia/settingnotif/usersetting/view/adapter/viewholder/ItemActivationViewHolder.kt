package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.state.Email
import com.tokopedia.settingnotif.usersetting.state.Phone
import com.tokopedia.settingnotif.usersetting.state.PushNotif
import com.tokopedia.settingnotif.usersetting.util.openNotificationSetting
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ItemActivationViewHolder(
        itemView: View?
): AbstractViewHolder<NotificationActivation>(itemView) {

    private val txtTitle = itemView?.findViewById<Typography>(R.id.txtTitle)
    private val txtDescription = itemView?.findViewById<Typography>(R.id.txtDescription)
    private val btnActivation = itemView?.findViewById<UnifyButton>(R.id.btnActivation)

    override fun bind(element: NotificationActivation?) {
        val context = itemView.context
        element?.let { data ->
            txtTitle?.text = context.getString(data.title)
            txtDescription?.text = context.getString(data.description)
            btnActivation?.text = context.getString(data.action)
            btnActivation?.setOnClickListener {
                when(data.type) {
                    is PushNotif -> context.openNotificationSetting()
                    is Email -> {}
                    is Phone -> {}
                }
            }
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_push_notif_activation
    }

}