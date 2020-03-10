package com.tokopedia.sellerhome.settings.view.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.sellerhome.settings.view.fragment.MenuSettingFragment

class MenuSettingActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? =
        MenuSettingFragment.createInstance()

}