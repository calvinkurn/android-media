package com.tokopedia.profilecompletion.settingprofile.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule
import com.tokopedia.profilecompletion.settingprofile.view.fragment.SettingProfileFragment

/**
 * Created by Ade Fulki on 2019-07-02.
 * ade.hadian@tokopedia.com
 * For navigate: use {@link ApplinkConstInternalGlobal.SETTING_PROFILE}
 */

class SettingProfileActivity: BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun getComponent(): ProfileCompletionSettingComponent =
            DaggerProfileCompletionSettingComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .profileCompletionSettingModule(ProfileCompletionSettingModule(this))
                    .build()

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return SettingProfileFragment.createInstance(bundle)
    }
}