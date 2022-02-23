package com.tokopedia.profilecompletion.changebiousername.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule
import com.tokopedia.profilecompletion.changebiousername.view.ChangeBioUsernameFragment



class ChangeBioUsernameActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {
    override fun getNewFragment(): Fragment {
        return ChangeBioUsernameFragment()
    }

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .profileCompletionSettingModule(ProfileCompletionSettingModule(this))
            .build()
    }
}