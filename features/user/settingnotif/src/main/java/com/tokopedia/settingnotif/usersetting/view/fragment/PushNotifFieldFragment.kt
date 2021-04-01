package com.tokopedia.settingnotif.usersetting.view.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.data.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationPushNotif
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationTroubleshooter
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment
import com.tokopedia.settingnotif.usersetting.view.fragment.dialog.InformationDialog.showInformationDialog
import com.tokopedia.settingnotif.usersetting.view.viewmodel.SettingStateViewModel

class PushNotifFieldFragment : SettingFieldFragment() {

    private val viewModel: SettingStateViewModel by lazy {
        ViewModelProvider(
                this,
                viewModelFactory
        ).get(SettingStateViewModel::class.java)
    }

    private val settingStates by lazy(LazyThreadSafetyMode.NONE) {
        viewModel.getSettingStates()
    }

    override fun getGqlRawQuery(): Int {
        return R.raw.query_push_notif_setting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_notifsetting_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.mn_information) {
            showInformationDialog(screenName)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (settingStates.isNotEmpty()) {
            pushNotifValidation()
        }
    }

    override fun onSuccessGetUserSetting(data: UserSettingDataView) {
        viewModel.addPinnedPushNotificationItems(isNotificationEnabled(), data)
        data.data = viewModel.getPinnedItems().toList()
        super.onSuccessGetUserSetting(data)
    }

    override fun renderList(list: MutableList<Visitable<*>>) {
        super.renderList(list)

        // save state of settings
        viewModel.saveLastStateAll(list)

        // view validation
        pushNotifValidation()
    }

    override fun updateSettingState(setting: ParentSetting?) {
        viewModel.updateSettingState(setting)
    }

    private fun pushNotifValidation() {
        val isNotificationEnabled = isNotificationEnabled()
        val notificationAction = if(isNotificationEnabled) {
            activationTroubleshooter()
        } else {
            activationPushNotif()
        }
        permissionValidationNotification(
                isNotificationEnabled,
                notificationAction,
                settingStates
        )
    }

    override fun getScreenName() = getString(R.string.settingnotif_dialog_info_title)
    override fun getNotificationType() = TYPE_PUSH_NOTIF

}