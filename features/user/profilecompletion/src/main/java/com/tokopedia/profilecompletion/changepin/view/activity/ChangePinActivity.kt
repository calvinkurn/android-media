package com.tokopedia.profilecompletion.changepin.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.profilecompletion.changepin.view.fragment.ChangePinFragment
import com.tokopedia.profilecompletion.di.DaggerProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent

//
// Created by Yoris Prayogo on 2019-10-22.
//

class ChangePinActivity : BaseSimpleActivity(), HasComponent<ProfileCompletionSettingComponent> {
    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ChangePinFragment.createInstance(bundle)
    }

    override fun getComponent(): ProfileCompletionSettingComponent {
        return DaggerProfileCompletionSettingComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }
}