package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.ChangeSection
import com.tokopedia.settingnotif.usersetting.state.Email
import com.tokopedia.settingnotif.usersetting.state.Phone
import com.tokopedia.unifyprinciples.Typography

class ChangeItemViewHolder(
        itemView: View?
): AbstractViewHolder<ChangeSection>(itemView) {

    private val txtTitle = itemView?.findViewById<Typography>(R.id.txtTitle)
    private val btnChange = itemView?.findViewById<Typography>(R.id.btnChange)
    private val txtChangeType = itemView?.findViewById<Typography>(R.id.txtChangeType)

    override fun bind(element: ChangeSection?) {
        val context = itemView.context
        element?.let { data ->
            txtTitle?.text = context.getString(data.description)
            txtChangeType?.text = data.changeItem
            btnChange?.setOnClickListener {
                when(data.state) {
                    is Email -> context.startActivity(RouteManager.getIntent(
                            context,
                            ApplinkConstInternalGlobal.ADD_EMAIL
                    ))
                    is Phone -> context.startActivity(RouteManager.getIntent(
                            context,
                            ApplinkConstInternalGlobal.ADD_PHONE
                    ))
                }
            }
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_change_section
    }

}