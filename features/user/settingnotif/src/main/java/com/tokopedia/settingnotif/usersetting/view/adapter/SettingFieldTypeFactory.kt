package com.tokopedia.settingnotif.usersetting.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.usersetting.domain.pojo.ChildSettingPojo
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSettingPojo
import com.tokopedia.settingnotif.usersetting.domain.pojo.SettingSectionsPojo
import com.tokopedia.settingnotif.usersetting.view.adapter.viewholder.SettingViewHolder

interface SettingFieldTypeFactory : AdapterTypeFactory {

    fun type(settingSectionsPojo: SettingSectionsPojo): Int

    fun type(parentSettingPojo: ParentSettingPojo): Int

    fun type(childSettingPojo: ChildSettingPojo): Int

    fun createViewHolder(
            parent: View,
            type: Int,
            settingListener: SettingViewHolder.SettingListener
    ): AbstractViewHolder<*>
}