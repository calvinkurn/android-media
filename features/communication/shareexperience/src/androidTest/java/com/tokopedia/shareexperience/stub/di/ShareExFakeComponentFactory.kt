package com.tokopedia.shareexperience.stub.di

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.shareexperience.data.di.component.ShareExComponent
import com.tokopedia.shareexperience.data.di.component.ShareExComponentFactory
import com.tokopedia.shareexperience.stub.di.base.DaggerShareExFakeBaseAppComponent
import com.tokopedia.shareexperience.stub.di.base.ShareExFakeAppModule
import com.tokopedia.shareexperience.stub.di.base.ShareExFakeBaseAppComponent

class ShareExFakeComponentFactory: ShareExComponentFactory {

    val shareExComponentFactory: ShareExComponentStub
    private val baseComponent: ShareExFakeBaseAppComponent

    init {
        val context = ApplicationProvider.getApplicationContext<Context>().applicationContext
        baseComponent = DaggerShareExFakeBaseAppComponent.builder()
            .shareExFakeAppModule(ShareExFakeAppModule(context))
            .build()
        shareExComponentFactory = DaggerShareExComponentStub.builder()
            .shareExFakeBaseAppComponent(baseComponent)
            .build()
    }
    override fun createShareExComponent(application: Application): ShareExComponent {
        return shareExComponentFactory
    }
}
