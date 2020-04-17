package com.tokopedia.settingnotif.usersetting.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.domain.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.domain.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.domain.pojo.SellerSection.Companion.createSellerItem
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.VisitableSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.ChangeItemDataView.changeEmail
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationEmail
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationPushNotif
import com.tokopedia.settingnotif.usersetting.view.dataview.SettingStateDataView.mapCloneSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingViewModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

interface PushNotifContract {
    fun addPinnedPushNotifItems(
            isNotificationEnabled: Boolean,
            data: UserSettingViewModel
    )
    fun addPinnedPermission(
            isNotificationEnabled: Boolean,
            activation: NotificationActivation
    )
    fun addPinnedEmailItems(data: UserSettingViewModel)
    fun addPinnedSellecSection()
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

    private fun addPinnedItems(
            data: UserSettingViewModel,
            pinned: () -> Unit
    ) {
        // reset first
        pinnedItems.clear()

        // add pinned
        pinned()

        // store pinned data
        pinnedItems.addAll(data.data)
    }

    override fun addPinnedPushNotifItems(
            isNotificationEnabled: Boolean,
            data: UserSettingViewModel
    ) {
        addPinnedItems(data) {
            // pinned permission
            addPinnedPermission(!isNotificationEnabled, activationPushNotif())

            // pinned seller
            addPinnedSellecSection()
        }
    }

    override fun addPinnedEmailItems(data: UserSettingViewModel) {
        addPinnedItems(data) {
            val email = userSession.email
            val hasEmail = email.isNotEmpty()

            // pinned add email
            addPinnedPermission(hasEmail, activationEmail())

            // pinned change email
            if (hasEmail) pinnedItems.add(changeEmail(email))
        }
    }

    override fun addPinnedPermission(
            isNotificationEnabled: Boolean,
            activation: NotificationActivation
    ) {
        // showing pinned message if
        // notification permission turned off
        if (isNotificationEnabled) {
            pinnedItems.add(activation)
        }
    }

    override fun addPinnedSellecSection() {
        // showing seller sub menu card
        // is user has a shop
        if (userSession.hasShop()) {
            pinnedItems.add(createSellerItem())
        }
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