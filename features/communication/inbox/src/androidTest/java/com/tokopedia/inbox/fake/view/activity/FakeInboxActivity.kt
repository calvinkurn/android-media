package com.tokopedia.inbox.fake.view.activity

import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.inbox.di.InboxComponent
import com.tokopedia.inbox.fake.view.navigator.FakeInboxFragmentFactory
import com.tokopedia.inbox.view.activity.InboxActivity
import com.tokopedia.inbox.view.activity.base.InboxTest
import com.tokopedia.inbox.view.navigator.InboxFragmentFactory

class FakeInboxActivity : InboxActivity() {

    var page = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        parseApplinkParam()
        super.onCreate(savedInstanceState)
        supportFragmentManager.executePendingTransactions()
    }

    private fun parseApplinkParam() {
        val data = intent?.data ?: return
        page = data.getQueryParameter(ApplinkConst.Inbox.PARAM_PAGE) ?: ""
    }

    override fun createDaggerComponent(): InboxComponent {
        return InboxTest.inboxComponent!!
    }

    override fun createFragmentFactory(): InboxFragmentFactory {
        return FakeInboxFragmentFactory(page)
    }

    override fun setupToolbarLifecycle() {
        // prevent getting notification from NavToolbar
    }
}