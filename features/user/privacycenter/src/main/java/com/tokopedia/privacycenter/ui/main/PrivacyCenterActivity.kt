package com.tokopedia.privacycenter.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.privacycenter.di.ActivityComponentFactory
import com.tokopedia.privacycenter.di.PrivacyCenterComponent

class PrivacyCenterActivity : BaseSimpleActivity(), HasComponent<PrivacyCenterComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbar.hide()
    }

    override fun getNewFragment(): Fragment = PrivacyCenterFragment.newInstance()

    override fun getComponent(): PrivacyCenterComponent {
        return ActivityComponentFactory.instance.createComponent(application)
    }
}
