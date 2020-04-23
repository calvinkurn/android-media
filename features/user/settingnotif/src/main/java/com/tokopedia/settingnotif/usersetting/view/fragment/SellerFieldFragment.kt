package com.tokopedia.settingnotif.usersetting.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.VisitableSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationPushNotif
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment
import com.tokopedia.settingnotif.usersetting.view.viewmodel.SettingStateViewModel

class SellerFieldFragment: SettingFieldFragment() {

    private lateinit var viewModel: SettingStateViewModel

    private val settingStates by lazy(LazyThreadSafetyMode.NONE) {
        viewModel.getSettingStates()
    }

    override fun getGqlRawQuery(): Int {
        return R.raw.query_seller_notif_setting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
                .of(this, viewModelFactory)
                .get(SettingStateViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        if (settingStates.isNotEmpty()) {
            pushNotifValidation()
        }
    }

    override fun onSuccessGetUserSetting(data: UserSettingDataView) {
        val pinnedData = arrayListOf<VisitableSettings>()
        pinnedData.addAll(data.data)
        data.data = pinnedData.toList()
        super.onSuccessGetUserSetting(data)
    }

    override fun renderList(list: MutableList<Visitable<*>>) {
        super.renderList(list)

        // save state of settings
        viewModel.saveLastStateAll(list)

        // view validation
        pushNotifValidation()
    }

    /*
    * especially for seller push notif,
    * request update setting must be use `pushnotif`
    * as a notification key
    * */
    override fun requestUpdateUserSetting(
            notificationType: String,
            updatedSettingIds: List<Map<String, Any>>
    ) {
        settingViewModel.requestUpdateUserSetting(TYPE_PUSH_NOTIF, updatedSettingIds)
        settingViewModel.requestUpdateMoengageUserSetting(updatedSettingIds)
    }

    private fun pushNotifValidation() {
        permissionValidation(
                isNotificationEnabled(),
                activationPushNotif(),
                settingStates,
                false
        )
    }

    override fun getScreenName() = getString(R.string.settingnotif_settings_seller_title)
    override fun getNotificationType() = TYPE_SELLER_NOTIF

}