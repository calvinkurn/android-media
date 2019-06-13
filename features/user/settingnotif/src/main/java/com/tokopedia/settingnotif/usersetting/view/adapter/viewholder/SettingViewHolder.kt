package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Switch
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.SettingPojo

class SettingViewHolder(itemView: View?) : AbstractViewHolder<SettingPojo>(itemView) {

    private val settingSwitch = itemView?.findViewById<Switch>(R.id.sw_setting)

    override fun bind(element: SettingPojo?) {
        settingSwitch?.text = element?.name
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_setting
    }

}