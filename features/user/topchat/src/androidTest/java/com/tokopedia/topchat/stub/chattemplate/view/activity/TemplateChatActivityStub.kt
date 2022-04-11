package com.tokopedia.topchat.stub.chattemplate.view.activity

import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity

open class TemplateChatActivityStub: TemplateChatActivity() {
    override fun inflateFragment() {
        super.inflateFragment()
        supportFragmentManager.executePendingTransactions()
    }

    override fun getTagFragment(): String {
        return TAG_STUB_FRAGMENT
    }

    companion object {
        const val TAG_STUB_FRAGMENT = "TAG_STUB_FRAGMENT_TEMPLATE"
    }
}