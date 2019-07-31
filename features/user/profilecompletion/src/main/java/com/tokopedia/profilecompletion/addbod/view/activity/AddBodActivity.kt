package com.tokopedia.profilecompletion.addbod.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.profilecompletion.addbod.view.fragment.AddBodFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent

/**
 * Created by Ade Fulki on 2019-07-16.
 * ade.hadian@tokopedia.com
 * For navigate: use {@link ApplinkConstInternalGlobal.REQUEST_ADD_BOD}
 */

class AddBodActivity: BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return AddBodFragment.createInstance(bundle)
    }
}