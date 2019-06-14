package com.tokopedia.settingnotif.usersetting.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.usersetting.domain.pojo.ChildSettingPojo
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSettingPojo
import com.tokopedia.settingnotif.usersetting.domain.pojo.SettingSectionsPojo
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.ChildSettingViewHolder
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.ParentSettingViewHolder
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingSectionViewHolder

class SettingFieldTypeFactory : BaseAdapterTypeFactory() {

    fun type(settingSectionsPojo: SettingSectionsPojo): Int {
        return SettingSectionViewHolder.LAYOUT
    }

    fun type(parentSettingPojo: ParentSettingPojo): Int {
        return ParentSettingViewHolder.LAYOUT
    }

    fun type(childSettingPojo: ChildSettingPojo): Int {
        return ChildSettingViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            SettingSectionViewHolder.LAYOUT -> SettingSectionViewHolder(parent)
            ParentSettingViewHolder.LAYOUT -> ParentSettingViewHolder(parent)
            ChildSettingViewHolder.LAYOUT -> ChildSettingViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

}