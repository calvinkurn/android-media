package com.tokopedia.settingnotif.usersetting.view.dataview

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory

typealias Data = Visitable<SettingFieldTypeFactory>

data class UserSettingViewModel(var data: List<Data>)