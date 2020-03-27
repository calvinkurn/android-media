package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_EMAIL
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.ADD_PHONE
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.state.Email
import com.tokopedia.settingnotif.usersetting.state.Phone
import com.tokopedia.settingnotif.usersetting.state.PushNotif
import com.tokopedia.settingnotif.usersetting.util.getActivationIntent
import com.tokopedia.settingnotif.usersetting.util.openNotificationSetting
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ActivationItemViewHolder(
        itemView: View?
): AbstractViewHolder<NotificationActivation>(itemView) {

    private val txtTitle = itemView?.findViewById<Typography>(R.id.txtTitle)
    private val txtDescription = itemView?.findViewById<Typography>(R.id.txtDescription)
    private val btnActivation = itemView?.findViewById<UnifyButton>(R.id.btnActivation)

    private val context by lazy { itemView?.context }

    override fun bind(element: NotificationActivation?) {
        element?.let { data ->
            txtTitle?.text = context?.getString(data.title)
            txtDescription?.text = context?.getString(data.description)
            btnActivation?.text = context?.getString(data.action)
            btnActivation?.setOnClickListener {
                context?.let {
                    it.startActivity(when(data.type) {
                        is PushNotif -> it.openNotificationSetting()
                        is Email -> it.getActivationIntent(ADD_EMAIL)
                        is Phone -> it.getActivationIntent(ADD_PHONE)
                    })
                }
            }
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_push_notif_activation
    }

}