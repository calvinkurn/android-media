package com.tokopedia.privacycenter

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.privacycenter.di.DaggerPrivacyCenterComponent
import com.tokopedia.privacycenter.di.PrivacyCenterComponent

class PrivacyCenterActivity : BaseSimpleActivity(), HasComponent<PrivacyCenterComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0f
    }

    override fun getNewFragment(): Fragment =
        PrivacyCenterFragment.newInstance()

    override fun getComponent(): PrivacyCenterComponent {
        return DaggerPrivacyCenterComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }
}
