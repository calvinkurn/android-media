package com.tokopedia.profilecompletion.addphone.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.profilecompletion.addphone.view.fragment.NewAddPhoneFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule

class NewAddPhoneActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    override fun getNewFragment(): Fragment =
        NewAddPhoneFragment.newInstance()

    override fun getComponent(): ProfileCompletionSettingComponent =
        DaggerProfileCompletionSettingComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .profileCompletionSettingModule(ProfileCompletionSettingModule(this))
            .build()
}
