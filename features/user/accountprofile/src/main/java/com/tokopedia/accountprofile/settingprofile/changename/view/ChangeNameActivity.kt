package com.tokopedia.accountprofile.settingprofile.changename.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.accountprofile.settingprofile.changename.data.analytics.ChangeNameTracker
import com.tokopedia.accountprofile.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent
import com.tokopedia.accountprofile.di.ProfileCompletionSettingModule

/**
 * created by rival 23/10/19
 */
class ChangeNameActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        var oldName = ""
        var chances = ""

        try {
            oldName =
                intent.data?.getQueryParameter(ApplinkConstInternalUserPlatform.PARAM_FULL_NAME)
                    .toString()
            chances =
                intent.data?.getQueryParameter(ApplinkConstInternalUserPlatform.PARAM_CHANCE_CHANGE_NAME)
                    .toString()
        } finally {
            bundle.putString(ApplinkConstInternalUserPlatform.PARAM_FULL_NAME, oldName)
            bundle.putString(ApplinkConstInternalUserPlatform.PARAM_CHANCE_CHANGE_NAME, chances)
        }

        return ChangeNameFragment.createInstance(bundle)
    }

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        ChangeNameTracker().back()
    }

}
