package com.tokopedia.topchat.stub.chattemplate.di

import android.app.Activity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.topchat.chattemplate.di.ActivityComponentFactory
import com.tokopedia.topchat.chattemplate.di.DaggerTemplateChatComponent
import com.tokopedia.topchat.chattemplate.di.TemplateChatComponent
import com.tokopedia.topchat.chattemplate.di.TemplateChatModule
import com.tokopedia.topchat.common.chat.api.ChatTemplateApi
import com.tokopedia.topchat.stub.chattemplate.usecase.api.ChatTemplateApiStub
import retrofit2.Retrofit

class FakeTemplateActivityComponentFactory: ActivityComponentFactory() {

    var chatTemplateApiStub: ChatTemplateApiStub = ChatTemplateApiStub()

    override fun createActivityComponent(activity: Activity): TemplateChatComponent {
        return DaggerTemplateChatComponent.builder()
            .baseAppComponent((activity.application as BaseMainApplication).baseAppComponent)
            .templateChatModule(object : TemplateChatModule() {
                override fun provideChatApiKt(retrofit: Retrofit): ChatTemplateApi {
                    return chatTemplateApiStub
                }
            }).build()
    }
}