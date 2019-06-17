package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import android.widget.Switch
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.usersetting.domain.pojo.Setting

abstract class SettingViewHolder<T : Setting>(itemView: View?) : AbstractViewHolder<T>(itemView) {

    protected val settingSwitch: Switch? = getSwitchView(itemView)

    abstract fun getSwitchView(itemView: View?): Switch?

    override fun bind(element: T?) {

    }

}