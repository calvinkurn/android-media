package com.tokopedia.topchat.stub.chattemplate.view.fragment

import com.tokopedia.topchat.chattemplate.activity.base.BaseEditTemplateTest
import com.tokopedia.topchat.chattemplate.di.TemplateChatComponent
import com.tokopedia.topchat.chattemplate.view.fragment.EditTemplateChatFragment

class EditTemplateChatFragmentStub: EditTemplateChatFragment() {

    override fun initializeComponent(): TemplateChatComponent {
        return BaseEditTemplateTest.chatTemplateComponentStub!!
    }
}