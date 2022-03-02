package com.tokopedia.profilecompletion.changebiousername.view

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingModule
import com.tokopedia.profilecompletion.changebiousername.view.ChangeBioUsernameFragment


/**
 * How to route to this activity, you need to append param:
 * 1.
 * PARAM:  ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PARAM
 * VALUE:  - ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO
 *         - ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME

 * 2.
 * PARAM:  ApplinkConstInternalUserPlatform.MIN_CHAR_EDIT_INFO_PARAM
 * VALUE: Integer Value
 * DEFAULT VALUE: - 4  (page value: ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME)
 *                - 0 (page value: ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO)

 * 3.
 * PARAM:  ApplinkConstInternalUserPlatform.MAX_CHAR_EDIT_INFO_PARAM
 * VALUE: Integer Value
 * DEFAULT VALUE: - 20  (page value: ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_USERNAME)
 *                - 150 (page value: ApplinkConstInternalUserPlatform.PAGE_EDIT_INFO_PROFILE_BIO)

 * Example:
 * Uri.parse(ApplinkConstInternalUserPlatform.EDIT_PROFILE_INFO).buildUpon().apply {
 * ApplinkConstInternalUserPlatform.apply {
 * appendQueryParameter(PAGE_EDIT_INFO_PARAM, PAGE_EDIT_INFO_PROFILE_BIO),
 * appendQueryParameter(ApplinkConstInternalUserPlatform.MAX_CHAR_EDIT_INFO_PARAM, 20)
 * appendQueryParameter(ApplinkConstInternalUserPlatform.MIN_CHAR_EDIT_INFO_PARAM, 4) }}.toString()
 */

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