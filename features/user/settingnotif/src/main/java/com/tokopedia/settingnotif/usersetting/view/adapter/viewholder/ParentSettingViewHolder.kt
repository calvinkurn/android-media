package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Switch
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSettingPojo

class ParentSettingViewHolder(itemView: View?) : SettingViewHolder<ParentSettingPojo>(itemView) {

    override fun getSwitchView(itemView: View?): Switch? {
        return itemView?.findViewById(R.id.sw_setting)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_parent_setting
    }
}