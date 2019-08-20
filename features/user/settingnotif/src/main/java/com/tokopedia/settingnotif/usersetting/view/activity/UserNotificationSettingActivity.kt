package com.tokopedia.settingnotif.usersetting.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.widget.FrameLayout
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.fragment.SettingTypeFragment
import com.tokopedia.settingnotif.usersetting.view.viewmodel.SettingType

class UserNotificationSettingActivity : BaseSimpleActivity(),
        SettingTypeFragment.SettingTypeContract {

    var fragmentContainer: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindView()
        setupView()
    }

    private fun bindView() {
        fragmentContainer = findViewById(R.id.parent_view)
    }

    private fun setupView() {
        val color = ContextCompat.getColor(this, R.color.white)
        fragmentContainer?.setBackgroundColor(color)
    }

    override fun getNewFragment(): Fragment {
        return SettingTypeFragment()
    }

    override fun openSettingField(settingType: SettingType) {
        // Phase 1
        if (settingType.isPushNotificationFieldFragment()) {
            goToOldPushNotificationSettingPage()
        } else {
            goToNewSettingPage(settingType)
        }
    }

    private fun goToOldPushNotificationSettingPage() {
        RouteManager.route(this, ApplinkConstInternalMarketplace.USER_PUSH_NOTIFICATION_SETTING)
    }

    private fun goToNewSettingPage(settingType: SettingType) {
        val fragment = supportFragmentManager.findFragmentByTag(settingType.name)
                ?: settingType.createNewFragmentInstance()

        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.parent_view, fragment, settingType.name)
                .commit()
    }

}
