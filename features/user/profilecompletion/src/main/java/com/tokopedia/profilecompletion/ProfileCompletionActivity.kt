package com.tokopedia.profilecompletion

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.profilecompletion.changegender.view.ChangeGenderFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule

/**
 * @author by nisie on 22/04/19.
 * For navigate: use {@link ApplinkConstInternalUserPlatform.PROFILE_COMPLETION}
 */
class ProfileCompletionActivity : BaseSimpleActivity(),
    HasComponent<ProfileCompletionSettingComponent> {


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
	    .profileCompletionSettingModule(ProfileCompletionSettingModule(this))
	    .build()
    }

    companion object {
	val MODE_GENDER = "start_gender"
    }
}