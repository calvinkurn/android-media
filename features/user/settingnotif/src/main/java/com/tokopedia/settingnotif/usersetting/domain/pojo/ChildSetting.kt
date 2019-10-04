package com.tokopedia.settingnotif.usersetting.domain.pojo


import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory

class ChildSetting(
        name: String,
        icon: String,
        key: String,
        status: Boolean
) : BaseSetting(name, icon, key, status) {
    override fun type(typeFactory: SettingFieldTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getIndex(currentSettingPosition: Int, parentSettingIndex: Int): Int {
        return currentSettingPosition - parentSettingIndex - 1
    }
}