package com.tokopedia.privacycenter.main.di

import android.app.Application
import android.content.Context
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.common.network.coroutines.repository.RestRepository
import com.tokopedia.privacycenter.di.ActivityComponentFactory
import com.tokopedia.privacycenter.di.PrivacyCenterComponent
import com.tokopedia.privacycenter.di.PrivacyCenterModule
import okhttp3.Interceptor

class FakeActivityComponentFactory : ActivityComponentFactory() {

    override fun createComponent(application: Application): PrivacyCenterComponent {
        return DaggerFakePrivacyCenterComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .privacyCenterModule(object : PrivacyCenterModule() {
                override fun provideRestRepository(
                    interceptors: MutableList<Interceptor>,
                    context: Context
                ): RestRepository {
                    return FakeRestRepo()
                }
            }).build()
    }
}
