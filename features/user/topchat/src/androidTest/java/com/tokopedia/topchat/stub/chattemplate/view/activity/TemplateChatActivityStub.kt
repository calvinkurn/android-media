package com.tokopedia.topchat.stub.chattemplate.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity
import com.tokopedia.topchat.stub.chattemplate.view.fragment.TemplateChatFragmentStub

open class TemplateChatActivityStub: TemplateChatActivity() {
    override fun inflateFragment() {
        super.inflateFragment()
        supportFragmentManager.executePendingTransactions()
    }

    override fun getNewFragment(): Fragment? {
        return createInstance(intent.extras)
    }

    override fun getTagFragment(): String {
        return TAG_STUB_FRAGMENT
    }

    private fun createInstance(extras: Bundle?): TemplateChatFragmentStub {
        val fragment = TemplateChatFragmentStub()
        fragment.arguments = extras
        return fragment
    }

    companion object {
        const val TAG_STUB_FRAGMENT = "TAG_STUB_FRAGMENT_TEMPLATE"
    }
}