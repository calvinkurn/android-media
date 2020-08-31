package com.tokopedia.settingnotif.usersetting.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.data.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView

object UserSettingMapper {

    fun map(response: UserNotificationResponse): UserSettingDataView {
        val outputData = arrayListOf<Visitable<SettingFieldTypeFactory>>()
        val data = response.userSetting

        for (settingSection in data.settingSections) {
            if (settingSection == null) continue
            settingSection.apply { this.isEnabled = true }
            outputData.add(settingSection)
            for (setting in settingSection.listSettings) {
                if (setting == null) continue
                setting.apply { this.isEnabled = true }
                outputData.add(setting)
                for (childSetting in setting.childSettings) {
                    if (childSetting == null) continue
                    outputData.add(childSetting)
                }
            }
        }

        return UserSettingDataView(outputData)
    }

}