package com.tokopedia.settingnotif.usersetting.view.dataview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory

data class UserSettingDataView(
        var data: List<Visitable<SettingFieldTypeFactory>>
)