package com.tokopedia.settingnotif.usersetting.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.domain.pojo.UserSettingPojo
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel
import rx.functions.Func1

class UserSettingFieldMapper : Func1<UserSettingPojo, UserSettingViewModel> {

    override fun call(data: UserSettingPojo): UserSettingViewModel {
        val outputData = arrayListOf<Visitable<SettingFieldTypeFactory>>()

        for (settingSection in data.sections) {
            outputData.add(settingSection)
            for (setting in settingSection.settings) {
                outputData.add(setting)
                for (childSetting in setting.childSettings) {
                    outputData.add(childSetting)
                }
            }
        }

        return UserSettingViewModel(outputData)
    }

}