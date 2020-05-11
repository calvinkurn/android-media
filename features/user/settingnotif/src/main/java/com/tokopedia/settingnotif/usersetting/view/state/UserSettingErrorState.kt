package com.tokopedia.settingnotif.usersetting.view.state

sealed class UserSettingErrorState {
    object GetSettingError: UserSettingErrorState()
    object SetSettingError: UserSettingErrorState()
}