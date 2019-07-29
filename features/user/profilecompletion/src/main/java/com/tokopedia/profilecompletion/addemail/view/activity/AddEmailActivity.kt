package com.tokopedia.profilecompletion.addemail.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.profilecompletion.addemail.view.fragment.AddEmailFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent


/**
 * @author by nisie on 22/04/19.
 * For navigate: use {@link ApplinkConstInternalGlobal.ADD_EMAIL}
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
        return DaggerProfileCompletionSettingComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

}