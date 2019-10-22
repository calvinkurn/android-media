package com.tokopedia.settingnotif.usersetting.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.domain.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel
import rx.functions.Func1

class UserSettingFieldMapper : Func1<UserNotificationResponse, UserSettingViewModel> {

    override fun call(response: UserNotificationResponse): UserSettingViewModel {
        val outputData = arrayListOf<Visitable<SettingFieldTypeFactory>>()
        val data = response.userSetting

        for (settingSection in data.settingSections) {
            if (settingSection == null) continue
            outputData.add(settingSection)
            for (setting in settingSection.listSettings) {
                if (setting == null) continue
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