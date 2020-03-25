package com.tokopedia.settingnotif.usersetting.view.fragment

import android.os.Bundle
import android.view.*
import androidx.core.app.NotificationManagerCompat
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.NotificationActivation
import com.tokopedia.settingnotif.usersetting.domain.pojo.SellerSection
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldAdapter
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify

typealias VisitableSettings = Visitable<SettingFieldTypeFactory>

class PushNotifFieldFragment : SettingFieldFragment() {

    private val _adapter by lazy { adapter as SettingFieldAdapter }

    override fun getScreenName(): String {
        return getString(R.string.settingnotif_dialog_info_title)
    }

    override fun getNotificationType(): String {
        return getString(R.string.settingnotif_dialog_info_title)
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
        val newData = arrayListOf<VisitableSettings>()
        if (isNotificationEnabled() == false) {
            newData.add(NotificationActivation())
        }
        newData.add(SellerSection())
        newData.addAll(data.data)
        data.data = newData.toList()
        super.onSuccessGetUserSetting(data)
    }

    private fun isNotificationEnabled(): Boolean? {
        return context?.let {
            NotificationManagerCompat
                    .from(it)
                    .areNotificationsEnabled()
        }
    }

    private fun showInformationDialog() {
        val customDialogView = View.inflate(context, R.layout.dialog_push_notif_information, null)
        val informationSheet = BottomSheetUnify().apply {
            setTitle(getString(R.string.settingnotif_dialog_info_title))
            setChild(customDialogView)
            setCloseClickListener { dismiss() }
        }
        informationSheet.show(childFragmentManager, TAG_INFORMATION)
    }

    companion object {
        private const val TAG_INFORMATION = "information"
    }

}