package com.tokopedia.accountprofile.settingprofile.addemail.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.accountprofile.settingprofile.addemail.view.fragment.AddEmailFragment
import com.tokopedia.accountprofile.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent
import com.tokopedia.accountprofile.di.ProfileCompletionSettingModule


/**
 * @author by nisie on 22/04/19.
 * For navigate: use {@link ApplinkConstInternalUserPlatform.ADD_EMAIL}
 */
class AddEmailActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        intent.extras?.let {
            bundle.putAll(it)
        }
        return AddEmailFragment.createInstance(bundle)
    }

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

}
