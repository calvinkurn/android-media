package com.tokopedia.profilecompletion.changebiousername.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule
import com.tokopedia.profilecompletion.changebiousername.view.ChangeBioUsernameFragment
import com.tokopedia.profilecompletion.di.ActivityComponentFactory


/**
 * How to route to this activity, you need to append param:
 *
 * PARAM:  ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PARAM
 * VALUE:  - ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO
 *         - ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME
 */

class ChangeBioUsernameActivity : BaseSimpleActivity(),
    HasComponent<ProfileCompletionSettingComponent> {
    override fun getNewFragment(): Fragment {
        return ChangeBioUsernameFragment()
    }

    override fun getComponent(): ProfileCompletionSettingComponent {
        return ActivityComponentFactory.instance.createProfileCompletionComponent(
            this,
            application as BaseMainApplication
        )
    }
}