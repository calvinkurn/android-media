package com.tokopedia.topchat.stub.chattemplate.di

import android.app.Activity
import android.content.Context
import com.tokopedia.topchat.chattemplate.di.ActivityComponentFactory
import com.tokopedia.topchat.chattemplate.di.TemplateChatComponent
import com.tokopedia.topchat.stub.common.di.DaggerFakeBaseAppComponent
import com.tokopedia.topchat.stub.common.di.module.FakeAppModule

class FakeTemplateActivityComponentFactory: ActivityComponentFactory() {

    override fun createActivityComponent(activity: Activity): TemplateChatComponent {
        if (templateChatComponent == null) {
            createTemplateChatComponent(activity.applicationContext)
        }
        return templateChatComponent!!
    }

    fun createTemplateChatComponent(context: Context) {
        val baseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(context))
            .build()
        templateChatComponent = DaggerTemplateChatComponentStub.builder()
            .fakeBaseAppComponent(baseComponent)
            .build()
    }

    companion object {
        var templateChatComponent: TemplateChatComponentStub? = null
    }
}
