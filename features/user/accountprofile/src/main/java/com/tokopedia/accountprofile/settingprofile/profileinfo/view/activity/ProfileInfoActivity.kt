package com.tokopedia.accountprofile.settingprofile.profileinfo.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.accountprofile.di.ActivityComponentFactory
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent
import com.tokopedia.accountprofile.settingprofile.profileinfo.view.fragment.ProfileInfoFragment

open class ProfileInfoActivity : BaseSimpleActivity(),
    HasComponent<ProfileCompletionSettingComponent> {

    override fun getNewFragment(): Fragment {
        return ProfileInfoFragment.createInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0F
    }

    override fun getComponent(): ProfileCompletionSettingComponent =
        ActivityComponentFactory.instance.createProfileCompletionComponent(
            this,
            application as BaseMainApplication
        )

    companion object {
        val TAG = ProfileInfoActivity::class.java.name
    }
}
