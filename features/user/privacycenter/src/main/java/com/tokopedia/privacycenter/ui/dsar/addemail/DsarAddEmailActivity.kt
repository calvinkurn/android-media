package com.tokopedia.privacycenter.ui.dsar.addemail

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.privacycenter.di.DaggerPrivacyCenterComponent
import com.tokopedia.privacycenter.di.PrivacyCenterComponent

class DsarAddEmailActivity : BaseSimpleActivity(), HasComponent<PrivacyCenterComponent> {

    override fun getNewFragment(): Fragment {
        return DsarAddEmailFragment.newInstance()
    }

    override fun getComponent(): PrivacyCenterComponent {
        return DaggerPrivacyCenterComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
    }
}
