package com.tokopedia.accountprofile.settingprofile.addphone.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.accountprofile.settingprofile.addphone.view.fragment.NewAddPhoneFragment
import com.tokopedia.accountprofile.di.ActivityComponentFactory
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent

class NewAddPhoneActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    override fun getNewFragment(): Fragment =
        NewAddPhoneFragment.newInstance()

    override fun getComponent(): ProfileCompletionSettingComponent =
        ActivityComponentFactory.instance.createProfileCompletionComponent(
            this,
            application as BaseMainApplication
        )
}
