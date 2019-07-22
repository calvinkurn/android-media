package com.tokopedia.settingnotif.usersetting.view.listener

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter
import com.tokopedia.settingnotif.usersetting.domain.pojo.setusersetting.SetUserSettingResponse
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel

interface SettingFieldContract {

    interface View : CustomerView {
        fun onSuccessGetUserSetting(data: UserSettingViewModel)
        fun onSuccessSetUserSetting(data: SetUserSettingResponse)
        fun onErrorSetUserSetting()
    }

    interface Presenter : CustomerPresenter<View> {
        fun loadUserSettings()
        fun requestUpdateUserSetting(notificationType: String, updatedSettingIds: List<Map<String, Any>>)
    }

}