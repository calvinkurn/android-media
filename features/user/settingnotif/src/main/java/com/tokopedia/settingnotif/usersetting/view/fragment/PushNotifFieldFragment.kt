package com.tokopedia.settingnotif.usersetting.view.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.SellerSection
import com.tokopedia.settingnotif.usersetting.util.inflateView
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldAdapter
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.VisitableSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationPushNotif
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingViewModel
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment
import com.tokopedia.unifycomponents.BottomSheetUnify

class PushNotifFieldFragment : SettingFieldFragment() {

    private val _adapter by lazy { adapter as SettingFieldAdapter }

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
        when(item.itemId) {
            R.id.mn_information -> showInformationDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (isNotificationEnabled() == true) {
            _adapter.removeNotificationPermission()
        }
    }

    override fun onSuccessGetUserSetting(data: UserSettingViewModel) {
        val pinnedData = arrayListOf<VisitableSettings>()
        /*
        * showing pinned message
        * if notification permission turn off
        * */
        if (isNotificationEnabled() == false) {
            pinnedData.add(activationPushNotif())
        }
        /*
        * showing seller sub menu card
        * is user has a shop
        * */
        if (userSession.hasShop()) {
            pinnedData.add(SellerSection())
        }
        pinnedData.addAll(data.data)
        data.data = pinnedData.toList()
        super.onSuccessGetUserSetting(data)
    }

    private fun showInformationDialog() {
        val customDialogView = context.inflateView(R.layout.dialog_push_notif_information)
        val informationSheet = BottomSheetUnify().apply {
            setTitle(screenName)
            setChild(customDialogView)
            setCloseClickListener { dismiss() }
        }
        informationSheet.show(childFragmentManager, screenName)
    }

    override fun getScreenName() = getString(R.string.settingnotif_dialog_info_title)
    override fun getNotificationType() = PUSH_NOTIF_TYPE

}