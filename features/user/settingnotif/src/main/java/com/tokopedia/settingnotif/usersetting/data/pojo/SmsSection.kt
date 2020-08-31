package com.tokopedia.settingnotif.usersetting.data.pojo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory

class SmsSection(
        val icon: Int = 0,
        val title: String = ""
): Visitable<SettingFieldTypeFactory> {

    override fun type(typeFactory: SettingFieldTypeFactory): Int {
        return typeFactory.type(this)
    }

}