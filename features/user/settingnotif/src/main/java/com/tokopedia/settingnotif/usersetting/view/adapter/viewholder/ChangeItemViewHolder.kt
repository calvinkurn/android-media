package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.CHANGE_PHONE_NUMBER
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.data.pojo.ChangeSection
import com.tokopedia.settingnotif.usersetting.state.Email
import com.tokopedia.settingnotif.usersetting.state.Phone
import com.tokopedia.settingnotif.usersetting.util.ChangeEmailLink
import com.tokopedia.settingnotif.usersetting.util.changeUserInfoIntent
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface

class ChangeItemViewHolder(
        private val userSession: UserSessionInterface,
        itemView: View?
): AbstractViewHolder<ChangeSection>(itemView) {

    private val imgIcon = itemView?.findViewById<ImageView>(R.id.imgIcon)
    private val txtTitle = itemView?.findViewById<Typography>(R.id.txtTitle)
    private val btnChange = itemView?.findViewById<Typography>(R.id.btnChange)
    private val txtChangeType = itemView?.findViewById<Typography>(R.id.txtChangeType)

    private val context by lazy { itemView?.context }

    override fun bind(element: ChangeSection?) {
        element?.let { data ->
            txtTitle?.text = context?.getString(data.description)
            txtChangeType?.text = data.changeItem
            imgIcon?.setImageResource(element.icon)

            btnChange?.setOnClickListener {
                when(data.state) {
                    is Email -> ChangeEmailLink(context, userSession.email)
                    is Phone -> userSettingApplink(CHANGE_PHONE_NUMBER)
                }
            }
        }
    }

    private fun userSettingApplink(applink: String) {
        context?.let {
            val intent = it.changeUserInfoIntent(
                    applink,
                    userSession.email,
                    userSession.phoneNumber
            )
            it.startActivity(intent)
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_change_section
    }

}