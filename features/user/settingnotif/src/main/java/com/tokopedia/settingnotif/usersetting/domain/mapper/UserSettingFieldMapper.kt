package com.tokopedia.settingnotif.usersetting.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.domain.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingViewModel
import rx.functions.Func1

class UserSettingFieldMapper : Func1<UserNotificationResponse, UserSettingViewModel> {

    override fun call(response: UserNotificationResponse): UserSettingViewModel {
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

        return UserSettingViewModel(outputData)
    }

}