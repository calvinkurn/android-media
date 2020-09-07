package com.tokopedia.settingnotif.usersetting.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.settingnotif.usersetting.const.Unify.Neutral_N0
import com.tokopedia.settingnotif.usersetting.view.fragment.SettingTypeFragment
import com.tokopedia.settingnotif.usersetting.view.dataview.SettingTypeDataView

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
            if (it.getQueryParameter(PUSH_NOTIFICATION_PAGE) != null) {
                val isSellerApp = GlobalConfig.isSellerApp()
                openPushNotificationFiled(isSellerApp)

                isHasPushNotificationParam = true
            }
        }
    }

    override fun onBackPressed() {
        if (isHasPushNotificationParam) {
            finish()
            return
        }

        super.onBackPressed()
    }

    private fun bindView() {
        fragmentContainer = findViewById(parentViewResourceID)
    }

    private fun setupView() {
        val color = ContextCompat.getColor(this, Neutral_N0)
        fragmentContainer?.setBackgroundColor(color)
    }

    override fun getNewFragment(): Fragment {
        return SettingTypeFragment()
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

    fun openPushNotificationFiled(isSeller: Boolean = true) {
        openSettingField(SettingTypeDataView.createPushNotificationType(isSeller))
    }

    override fun getParentViewResourceID() = com.tokopedia.abstraction.R.id.parent_view

    override fun getLayoutRes() = com.tokopedia.abstraction.R.layout.activity_base_simple

    companion object {
        private const val PUSH_NOTIFICATION_PAGE = "push_notification"
    }

}
