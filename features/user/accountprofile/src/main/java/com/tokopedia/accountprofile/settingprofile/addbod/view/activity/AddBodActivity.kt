package com.tokopedia.accountprofile.settingprofile.addbod.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.accountprofile.settingprofile.addbod.view.fragment.AddBodFragment
import com.tokopedia.accountprofile.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent
import com.tokopedia.accountprofile.di.ProfileCompletionSettingModule

/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 * For navigate: use {@link ApplinkConstInternalUserPlatform.ADD_BOD}
 */

class AddBodActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            val bodTitle =
                intent.extras?.getString(ApplinkConstInternalUserPlatform.PARAM_BOD_TITLE)
            updateTitle(bodTitle)
            bundle.putAll(intent.extras)
        }
        return AddBodFragment.createInstance(bundle)
    }
}
