package com.tokopedia.tokochat.stub.di

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.tokopedia.tokochat.config.di.module.TokoChatConfigContextModule
import com.tokopedia.tokochat.di.TokoChatActivityComponentFactory
import com.tokopedia.tokochat.di.TokoChatComponent
import com.tokopedia.tokochat.stub.di.base.DaggerFakeBaseAppComponent
import com.tokopedia.tokochat.stub.di.base.FakeAppModule
import com.tokopedia.tokochat.stub.di.base.FakeBaseAppComponent

class TokoChatFakeActivityComponentFactory : TokoChatActivityComponentFactory() {

    val tokoChatComponent: TokoChatComponentStub
    val baseComponent: FakeBaseAppComponent

    init {
        val context = ApplicationProvider.getApplicationContext<Context>().applicationContext
        baseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(context))
            .build()
        tokoChatComponent = DaggerTokoChatComponentStub.builder()
            .fakeBaseAppComponent(baseComponent)
            .tokoChatConfigContextModule(TokoChatConfigContextModule(context))
            .tokoChatCourierConversationModule(
                TokoChatCourierConversationModule(context as Application)
            )
            .build()
    }

    override fun createTokoChatComponent(application: Application): TokoChatComponent {
        return tokoChatComponent
    }
}
