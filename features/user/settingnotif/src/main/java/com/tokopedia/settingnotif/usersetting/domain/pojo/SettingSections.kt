package com.tokopedia.settingnotif.usersetting.domain.pojo


import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory

data class SettingSections(
        @SerializedName("list_settings")
        var listSettings: List<ParentSetting?> = emptyList(),
        @SerializedName("section")
        var title: String = ""
) : Visitable<SettingFieldTypeFactory> {

        override fun type(typeFactory: SettingFieldTypeFactory): Int {
                return typeFactory.type(this)
        }

}