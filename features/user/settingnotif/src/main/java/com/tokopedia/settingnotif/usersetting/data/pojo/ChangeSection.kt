package com.tokopedia.settingnotif.usersetting.data.pojo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.state.NotificationItemState
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory

data class ChangeSection(
        val icon: Int = 0,
        val description: Int = 0,
        val changeItem: String = "",
        val state: NotificationItemState
) : Visitable<SettingFieldTypeFactory> {

    override fun type(typeFactory: SettingFieldTypeFactory): Int {
        return typeFactory.type(this)
    }

}