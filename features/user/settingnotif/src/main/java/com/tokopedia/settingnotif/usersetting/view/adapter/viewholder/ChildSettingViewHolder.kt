package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Switch
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.ChildSettingPojo

class ChildSettingViewHolder(itemView: View?) : SettingViewHolder<ChildSettingPojo>(itemView) {

    override fun getSwitchView(itemView: View?): Switch? {
        return itemView?.findViewById(R.id.sw_setting)
    }

    override fun bind(element: ChildSettingPojo?) {
        super.bind(element)

        settingSwitch?.text = element?.name
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_child_setting
    }

}