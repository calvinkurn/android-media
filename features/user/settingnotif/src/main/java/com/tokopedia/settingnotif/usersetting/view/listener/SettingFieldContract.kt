package com.tokopedia.settingnotif.usersetting.view.listener

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.data.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.data.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.VisitableSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView

interface SettingFieldContract {

    interface UserSetting {
        fun requestUpdateUserSetting(
                notificationType: String,
                updatedSettingIds: List<Map<String, Any>>
        )
        fun loadUserSettings()
        fun requestUpdateMoengageUserSetting(name: String, value: Boolean)
    }

    interface SettingState {
        fun addPinnedPushNotificationItems(
                isNotificationEnabled: Boolean,
                data: UserSettingDataView
        )
        fun addPinnedPermission(
                isNotificationEnabled: Boolean,
                activation: NotificationActivation
        )
        fun addPinnedEmailItems(data: UserSettingDataView)
        fun addPinnedSellerSection()
        fun saveLastStateAll(list: MutableList<Visitable<*>>)
        fun getPinnedItems(): List<VisitableSettings>
        fun getSettingStates(): List<ParentSetting>
        fun updateSettingState(setting: ParentSetting?)
    }

}