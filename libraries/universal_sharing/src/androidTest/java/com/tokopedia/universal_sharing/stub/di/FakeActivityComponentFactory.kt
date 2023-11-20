package com.tokopedia.universal_sharing.stub.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.universal_sharing.di.ActivityComponentFactory
import com.tokopedia.universal_sharing.di.UniversalShareComponent
import com.tokopedia.universal_sharing.stub.di.base.DaggerUniversalSharingFakeBaseAppComponent
import com.tokopedia.universal_sharing.stub.di.base.UniversalSharingFakeAppModule

class FakeActivityComponentFactory : ActivityComponentFactory() {

    private val baseAppComponent: BaseAppComponent
    val universalSharingComponent: UniversalSharingComponentStub

    init {
        val context = ApplicationProvider.getApplicationContext<Context>().applicationContext
        baseAppComponent = DaggerUniversalSharingFakeBaseAppComponent.builder()
            .universalSharingFakeAppModule(UniversalSharingFakeAppModule(context))
            .build()
        universalSharingComponent = DaggerUniversalSharingComponentStub.builder()
            .universalSharingFakeBaseAppComponent(baseAppComponent)
            .build()
    }

    override fun createActivityComponent(context: Context): UniversalShareComponent {
        return universalSharingComponent
    }

}
