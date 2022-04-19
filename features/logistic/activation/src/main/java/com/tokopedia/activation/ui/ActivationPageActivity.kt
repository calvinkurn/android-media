package com.tokopedia.activation.ui

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.activation.di.ActivationPageComponent
import com.tokopedia.activation.di.DaggerActivationPageComponent

class ActivationPageActivity : BaseSimpleActivity(), HasComponent<ActivationPageComponent> {


    override fun getNewFragment(): Fragment? {
        return ActivationPageFragment()
    }

    override fun getComponent(): ActivationPageComponent {
        return DaggerActivationPageComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }
}