package com.tokopedia.settingnotif.usersetting.domain.pojo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.state.NotificationActivationState
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory

data class NotificationActivation(
        val title: Int = 0,
        val description: Int = 0,
        val action: Int = 0,
        val type: NotificationActivationState
): Visitable<SettingFieldTypeFactory> {

    override fun type(typeFactory: SettingFieldTypeFactory): Int {
        return typeFactory.type(this)
    }

}