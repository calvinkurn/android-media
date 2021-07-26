package com.tokopedia.settingnotif.usersetting.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.usersetting.data.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.data.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.data.pojo.SellerSection.Companion.createSellerItem
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.VisitableSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.ChangeItemDataView.changeEmail
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationEmail
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationPushNotif
import com.tokopedia.settingnotif.usersetting.view.dataview.SettingStateDataView.mapCloneSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.settingnotif.usersetting.view.listener.SettingFieldContract
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SettingStateViewModel @Inject constructor(
        private val userSession: UserSessionInterface
): ViewModel(), SettingFieldContract.SettingState {

    private val pinnedItems = arrayListOf<VisitableSettings>()
    private val temporaryList = arrayListOf<ParentSetting>()

    override fun getPinnedItems() = pinnedItems

    override fun getSettingStates() = temporaryList

    private fun addPinnedItems(
            data: UserSettingDataView,
            pinned: () -> Unit
    ) {
        // reset first
        pinnedItems.clear()

        // add pinned
        pinned()

        // store pinned data
        pinnedItems.addAll(data.data)
    }

    override fun addPinnedPushNotificationItems(
            isNotificationEnabled: Boolean,
            data: UserSettingDataView
    ) {
        addPinnedItems(data) {
            // pinned permission
            addPinnedPermission(!isNotificationEnabled, activationPushNotif())

            // pinned seller
            addPinnedSellerSection()
        }
    }

    override fun addPinnedEmailItems(data: UserSettingDataView) {
        addPinnedItems(data) {
            val email = userSession.email

            // pinned recom tag to adding a new email if user doesn't have
            addPinnedPermission(email.isEmpty(), activationEmail())

            // pinned change email
            if (email.isNotEmpty()) pinnedItems.add(changeEmail(email))
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

    override fun addPinnedSellerSection() {
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

}