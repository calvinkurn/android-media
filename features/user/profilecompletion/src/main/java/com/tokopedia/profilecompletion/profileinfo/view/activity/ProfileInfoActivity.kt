package com.tokopedia.profilecompletion.profileinfo.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule
import com.tokopedia.profilecompletion.profileinfo.view.fragment.ProfileInfoFragment

class ProfileInfoActivity: BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun getNewFragment(): Fragment {
	return ProfileInfoFragment.createInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	supportActionBar?.elevation = 0F
    }

    override fun getComponent(): ProfileCompletionSettingComponent =
	DaggerProfileCompletionSettingComponent.builder()
	    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
	    .profileCompletionSettingModule(ProfileCompletionSettingModule(this))
	    .build()
}