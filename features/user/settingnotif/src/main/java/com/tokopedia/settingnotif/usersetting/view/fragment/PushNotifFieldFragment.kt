package com.tokopedia.settingnotif.usersetting.view.fragment

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.SellerSection
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldAdapter
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.VisitableSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationPushNotif
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingViewModel
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment
import com.tokopedia.settingnotif.usersetting.view.fragment.dialog.InformationDialog.showInformationDialog

class PushNotifFieldFragment : SettingFieldFragment() {

    private val pinnedItems = arrayListOf<VisitableSettings>()
    private var temporaryList = mutableListOf<Visitable<*>>()

    private val _adapter by lazy(LazyThreadSafetyMode.NONE) {
        adapter as SettingFieldAdapter
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
        when(item.itemId) {
            R.id.mn_information -> {
                showInformationDialog(screenName)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (temporaryList.isNotEmpty()) {
            itemValidation()
        }
    }

    private fun itemValidation() {
        if (isNotificationEnabled()) {
            _adapter.removePinnedActivation()
            _adapter.enableSwitchComponent(temporaryList)
        } else {
            _adapter.addPinnedActivation()
            _adapter.disableSwitchComponent()
        }
    }

    private fun addPinnedItem(data: UserSettingViewModel) {
        // showing pinned message if
        // notification permission turned off
        if (!isNotificationEnabled()) {
            pinnedItems.add(activationPushNotif())
        }

        // showing seller sub menu card
        // is user has a shop
        if (userSession.hasShop()) {
            pinnedItems.add(SellerSection())
        }

        // store pinned data
        pinnedItems.addAll(data.data)
    }

    override fun onSuccessGetUserSetting(data: UserSettingViewModel) {
        addPinnedItem(data)

        // replace the data with adding
        // all of pinned items
        data.data = pinnedItems.toList()
        super.onSuccessGetUserSetting(data)
    }

    override fun renderList(list: MutableList<Visitable<*>>) {
        super.renderList(list)

        // make a temporary data
        temporaryList.clear()
        temporaryList.addAll(list)

        itemValidation()
    }

    override fun getScreenName() = getString(R.string.settingnotif_dialog_info_title)
    override fun getNotificationType() = TYPE_PUSH_NOTIF

}