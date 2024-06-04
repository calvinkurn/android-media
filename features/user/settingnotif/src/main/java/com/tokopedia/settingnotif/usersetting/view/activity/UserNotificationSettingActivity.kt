package com.tokopedia.settingnotif.usersetting.view.activity

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.notifsetting.NotifSettingType
import com.tokopedia.config.GlobalConfig
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.const.Unify.Unify_Background
import com.tokopedia.settingnotif.usersetting.view.dataview.SettingTypeDataView
import com.tokopedia.settingnotif.usersetting.view.fragment.SellerFieldFragment
import com.tokopedia.settingnotif.usersetting.view.fragment.SettingTypeFragment
import com.tokopedia.abstraction.R as abstractionR

typealias ParentActivity = UserNotificationSettingActivity

class UserNotificationSettingActivity : BaseSimpleActivity(),
    SettingTypeFragment.SettingTypeContract {

    private var fragmentContainer: FrameLayout? = null

    /*
    * check the query data on the userSetting appLink.
    * used for onBackPressed()
    * */
    private var isHasPushNotificationParam = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        setupView()

        intent?.data?.let {
            handleAppLink(it)
        }
    }

    override fun onBackPressed() {
        if (isHasPushNotificationParam) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun getNewFragment(): Fragment {
        return getNotificationFragment()
    }

    override fun openSettingField(settingType: SettingTypeDataView) {
        val fragment = supportFragmentManager
            .findFragmentByTag(getString(settingType.name))
            ?: settingType.createNewFragmentInstance()

        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(parentViewResourceID, fragment, getString(settingType.name))
            .commit()
    }

    override fun getParentViewResourceID() = abstractionR.id.parent_view

    override fun getLayoutRes() = abstractionR.layout.activity_base_simple

    @SuppressLint("PII Data Exposure")
    private fun handleAppLink(uri: Uri) {
        when (uri.getQueryParameter(TYPE)) {
            NotifSettingType.Email.value -> openEmailSetting()
            NotifSettingType.Sms.value -> openSmsSetting()
            NotifSettingType.PushNotification.value -> {
                openPushNotificationFiled(GlobalConfig.isSellerApp())
            }

            else -> {
                val isPushNotif = uri.getQueryParameter(PUSH_NOTIFICATION_PAGE) != null
                if (isPushNotif) {
                    openPushNotificationFiled(GlobalConfig.isSellerApp())
                    isHasPushNotificationParam = true
                }
            }
        }
    }

    private fun bindView() {
        fragmentContainer = findViewById(parentViewResourceID)
    }

    private fun setupView() {
        val color = ContextCompat.getColor(this, Unify_Background)
        fragmentContainer?.setBackgroundColor(color)
    }

    fun openPushNotificationFiled(isSeller: Boolean = true) {
        openSettingField(SettingTypeDataView.createPushNotificationType(isSeller))
    }

    private fun openSmsSetting() {
        openSettingField(SettingTypeDataView.createSmsType())
    }

    private fun openEmailSetting() {
        openSettingField(SettingTypeDataView.createEmailType())
    }

    private fun getNotificationFragment(): Fragment {
        val openSellerSetting = intent.extras?.getBoolean(EXTRA_OPEN_SELLER_NOTIF) ?: false

        return if (openSellerSetting) {
            val dataView = SettingTypeDataView(
                icon = R.drawable.ic_notifsetting_notification,
                name = R.string.settingnotif_dialog_info_title,
                fragment = SellerFieldFragment::class.java
            )
            dataView.createNewFragmentInstance()
        } else {
            SettingTypeFragment.createInstance()
        }
    }

    companion object {
        const val TYPE = "type"
        const val PUSH_NOTIFICATION_PAGE = "push_notification"
        private const val EXTRA_OPEN_SELLER_NOTIF = "extra_open_seller_notif"
    }
}
