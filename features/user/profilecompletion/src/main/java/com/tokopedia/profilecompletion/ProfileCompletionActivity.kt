package com.tokopedia.profilecompletion

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.profilecompletion.changegender.view.ChangeGenderFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent

/**
 * @author by nisie on 22/04/19.
 * For navigate: use {@link ApplinkConstInternalGlobal.PROFILE_COMPLETION}
 */
class ProfileCompletionActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {


    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ChangeGenderFragment.createInstance(bundle)
    }

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    companion object{
        val MODE_GENDER = "start_gender"
    }
}