package com.tokopedia.privacycenter.main.di

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.privacycenter.common.di.ActivityComponentFactory
import com.tokopedia.privacycenter.common.di.PrivacyCenterComponent

class FakeActivityComponentFactory : ActivityComponentFactory() {

    override fun createComponent(application: Application): PrivacyCenterComponent {
        return DaggerFakePrivacyCenterComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent).build()
    }

}
