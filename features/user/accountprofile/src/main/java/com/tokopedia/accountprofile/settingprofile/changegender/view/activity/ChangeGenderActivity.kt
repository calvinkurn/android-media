package com.tokopedia.accountprofile.settingprofile.changegender.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.accountprofile.settingprofile.changegender.view.ChangeGenderFragment
import com.tokopedia.accountprofile.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent
import com.tokopedia.accountprofile.di.ProfileCompletionSettingModule


/**
 * @author by nisie on 22/04/19.
 * For navigate: use {@link ApplinkConstInternalUserPlatform.CHANGE_GENDER}
 */
class ChangeGenderActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ChangeGenderFragment.createInstance(bundle)
    }

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

}
