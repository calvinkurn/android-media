package com.tokopedia.shareexperience.data.di.component

import android.app.Application
import com.tokopedia.abstraction.base.app.BaseMainApplication

class ShareExComponentFactoryImpl : ShareExComponentFactory {
    override fun createShareExComponent(application: Application): ShareExComponent {
        return DaggerShareExComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .build()
    }
}
