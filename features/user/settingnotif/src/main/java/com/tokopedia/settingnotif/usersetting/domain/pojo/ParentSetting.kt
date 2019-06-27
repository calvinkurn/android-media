package com.tokopedia.settingnotif.usersetting.domain.pojo


import com.google.gson.annotations.SerializedName
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory

class ParentSetting(
        name: String,
        icon: String,
        key: String,
        status: Boolean,
        @SerializedName("description")
        var description: String = "",
        @SerializedName("list_settings")
        var childSettings: List<ChildSetting?> = emptyList()
) : BaseSetting(name, icon, key, status) {

    override fun type(typeFactory: SettingFieldTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun hasDescription(): Boolean = description.isNotEmpty()
    fun hasChild(): Boolean = childSettings.isNotEmpty()

}