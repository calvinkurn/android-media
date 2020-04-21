package com.tokopedia.settingnotif.usersetting.data.mapper

import com.tokopedia.settingnotif.usersetting.data.pojo.UserNotificationResponse
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import rx.functions.Func1

class UserSettingFieldMapper : Func1<UserNotificationResponse, UserSettingDataView> {

    override fun call(response: UserNotificationResponse): UserSettingDataView {
        return UserSettingMapper.map(response)
    }

}