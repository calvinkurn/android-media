package com.tokopedia.profilecompletion.addphone.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.profilecompletion.addphone.view.fragment.AddPhoneFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionComponent


/**
 * @author by nisie on 22/04/19.
 * For navigate: use {@link ApplinkConstInternalGlobal.ADD_PHONE}
 */
class AddPhoneActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionComponent> {

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return AddPhoneFragment.createInstance(bundle)
    }

    override fun getComponent(): ProfileCompletionComponent {
        return DaggerProfileCompletionComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

}