package com.tokopedia.settingnotif.usersetting.view.fragment

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.config.GlobalConfig
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.data.pojo.ParentSetting
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationEmail
import com.tokopedia.settingnotif.usersetting.view.dataview.SettingStateDataView.removeBuyerNotificationSetting
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment
import com.tokopedia.settingnotif.usersetting.view.viewmodel.SettingStateViewModel

class EmailFieldFragment: SettingFieldFragment() {

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
        return R.raw.query_email_setting
    }

    override fun onResume() {
        super.onResume()
        if (settingStates.isNotEmpty()) {
            emailValidation()
        }
    }

    override fun onSuccessGetUserSetting(data: UserSettingDataView) {
        if (GlobalConfig.isSellerApp()) {
            data.data = removeBuyerNotificationSetting(data.data)
        }
        viewModel.addPinnedEmailItems(data)
        data.data = viewModel.getPinnedItems().toList()
        super.onSuccessGetUserSetting(data)
    }

    override fun renderList(list: MutableList<Visitable<*>>) {
        super.renderList(list)

        // save state of settings
        viewModel.saveLastStateAll(list)

        // view validation
        emailValidation()
    }

    private fun emailValidation() {
        permissionValidation(
                userSession.email.isNotEmpty(),
                activationEmail(),
                settingStates
        )
    }

    override fun updateSettingState(setting: ParentSetting?) {
        viewModel.updateSettingState(setting)
    }

    override fun getScreenName() = getString(R.string.settingnotif_email)
    override fun getNotificationType() = TYPE_EMAIL
}