package com.tokopedia.settingnotif.usersetting.data.pojo

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory

abstract class BaseSetting (
    @SerializedName("name") var name: String = "",
    @SerializedName("icon") var icon: String = "",
    @SerializedName("key") var key: String = "",
    @SerializedName("status") var status: Boolean = false,
    var isEnabled: Boolean = true
) : Visitable<SettingFieldTypeFactory> {

    fun hasSameCheckedStatusWith(checked: Boolean) : Boolean {
        return status == checked
    }

}