package com.tokopedia.settingnotif.usersetting.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.usersetting.domain.pojo.SettingPojo
import com.tokopedia.settingnotif.usersetting.domain.pojo.SettingSectionsPojo
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingSectionViewHolder
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder

class SettingFieldTypeFactory: BaseAdapterTypeFactory() {

    fun type(settingSectionsPojo: SettingSectionsPojo): Int {
        return SettingSectionViewHolder.LAYOUT
    }

    fun type(settingPojo: SettingPojo): Int {
        return SettingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SettingSectionViewHolder.LAYOUT -> SettingSectionViewHolder(parent)
            SettingViewHolder.LAYOUT -> SettingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}