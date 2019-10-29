package com.tokopedia.settingnotif.usersetting.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.usersetting.domain.pojo.*
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder

interface SettingFieldTypeFactory : AdapterTypeFactory {

    fun type(settingSections: SettingSections): Int

    fun type(parentSetting: ParentSetting): Int

    fun type(childSetting: ChildSetting): Int

    fun createViewHolder(
            parent: View,
            type: Int,
            settingListener: SettingViewHolder.SettingListener
    ): AbstractViewHolder<*>
}