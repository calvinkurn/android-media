package com.tokopedia.settingnotif.usersetting.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.domain.pojo.SellerSection.Companion.createSellerItem
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.VisitableSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationPushNotif
import com.tokopedia.settingnotif.usersetting.view.dataview.SettingStateDataView.mapCloneSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

interface PushNotifContract {
    fun addPinnedItems(isNotificationEnabled: Boolean, data: UserSettingViewModel)
    fun saveLastStateAll(list: MutableList<Visitable<*>>)
    fun getPinnedItems(): List<VisitableSettings>
    fun getSettingStates(): List<ParentSetting>
    fun updateSettingState(setting: ParentSetting?)
}

class SettingStateViewModel @Inject constructor(
        private val userSession: UserSessionInterface
): ViewModel(), PushNotifContract {

    private val pinnedItems = arrayListOf<VisitableSettings>()
    private val temporaryList = arrayListOf<ParentSetting>()

    override fun addPinnedItems(
            isNotificationEnabled: Boolean,
            data: UserSettingViewModel
    ) {
        // reset first
        pinnedItems.clear()

        // showing pinned message if
        // notification permission turned off
        if (!isNotificationEnabled) {
            pinnedItems.add(activationPushNotif())
        }

        // showing seller sub menu card
        // is user has a shop
        if (userSession.hasShop()) {
            pinnedItems.add(createSellerItem())
        }

        // store pinned data
        pinnedItems.addAll(data.data)
    }

    override fun saveLastStateAll(list: MutableList<Visitable<*>>) {
        val listMapper = mapCloneSettings(list)
        temporaryList.clear()
        temporaryList.addAll(listMapper)
    }

    override fun updateSettingState(setting: ParentSetting?) {
        if (setting == null) return
        temporaryList.find { it.key == setting.key }.let {
            it.apply {
                it?.status = setting.status
                it?.isEnabled = setting.isEnabled
            }
        }
    }

    override fun getPinnedItems(): List<VisitableSettings> = pinnedItems

    override fun getSettingStates(): List<ParentSetting> = temporaryList

}