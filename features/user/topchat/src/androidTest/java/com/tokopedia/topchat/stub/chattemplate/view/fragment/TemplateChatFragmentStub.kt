package com.tokopedia.topchat.stub.chattemplate.view.fragment

import com.tokopedia.topchat.chattemplate.activity.base.BaseChatTemplateTest
import com.tokopedia.topchat.chattemplate.di.TemplateChatComponent
import com.tokopedia.topchat.chattemplate.view.fragment.TemplateChatFragment

class TemplateChatFragmentStub: TemplateChatFragment() {

    override fun initializeComponent(): TemplateChatComponent {
        return BaseChatTemplateTest.chatTemplateComponentStub!!
    }
}