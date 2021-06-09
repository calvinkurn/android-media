package com.tokopedia.inbox.view.activity.base.chat

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.inbox.fake.InboxChatFakeDependency
import com.tokopedia.inbox.fake.di.chat.DaggerFakeChatListComponent
import com.tokopedia.inbox.view.activity.base.InboxTest
import com.tokopedia.topchat.chatlist.di.ChatListContextModule

open class InboxChatTest : InboxTest() {

    protected val inboxChatDep = InboxChatFakeDependency()

    override fun before() {
        super.before()
        setupChatListDaggerComponent()
        chatListComponent!!.injectMembers(inboxChatDep)
        inboxChatDep.init()
    }

    override fun onBuildUri(uriBuilder: Uri.Builder) {
        uriBuilder.appendQueryParameter(
            ApplinkConst.Inbox.PARAM_PAGE,
            ApplinkConst.Inbox.VALUE_PAGE_CHAT
        )
    }


    private fun setupChatListDaggerComponent() {
        chatListComponent = DaggerFakeChatListComponent.builder()
            .fakeBaseAppComponent(baseComponent)
            .chatListContextModule(ChatListContextModule(context))
            .build()
    }

}