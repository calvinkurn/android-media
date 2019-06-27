package com.tokopedia.settingnotif.usersetting.domain.pojo

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory

abstract class BaseSetting (
    @SerializedName("name")
    var name: String = "",
    @SerializedName("icon")
    var icon: String = "",
    @SerializedName("key")
    var key: String = "",
    @SerializedName("status")
    var status: Boolean = false

//    var name: String,
//    var icon: String,
//    var key: String,
//    var status: Boolean
) : Visitable<SettingFieldTypeFactory>