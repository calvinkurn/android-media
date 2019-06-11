package com.tokopedia.settingnotif.usersetting.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.fragment.SettingFieldFragment
import com.tokopedia.settingnotif.usersetting.view.fragment.SettingTypeFragment

class UserNotificationSettingActivity : BaseSimpleActivity(),
        SettingTypeFragment.SettingTypeContract {

    override fun getNewFragment(): Fragment {
        return SettingTypeFragment()
    }

    override fun openSettingField(field: String) {
        val settingFieldFragment = SettingFieldFragment()

        supportFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.parent_view, settingFieldFragment, null)
                .commit()
    }

}
