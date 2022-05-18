package com.tokopedia.topchat.stub.chattemplate.view.activity

import com.tokopedia.topchat.chattemplate.view.activity.EditTemplateChatActivity

open class EditTemplateChatActivityStub: EditTemplateChatActivity() {
    override fun inflateFragment() {
        super.inflateFragment()
        supportFragmentManager.executePendingTransactions()
    }

    override fun getTagFragment(): String {
        return TAG_STUB_FRAGMENT
    }

    companion object {
        const val TAG_STUB_FRAGMENT = "TAG_STUB_EDIT_FRAGMENT_TEMPLATE"
    }
}