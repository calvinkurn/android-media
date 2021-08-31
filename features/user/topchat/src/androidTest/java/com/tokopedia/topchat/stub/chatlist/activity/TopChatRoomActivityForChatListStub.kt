package com.tokopedia.topchat.stub.chatlist.activity

import android.net.Uri
import android.os.Bundle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.topchat.chatroom.di.ChatComponent
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.stub.chatlist.fragment.TopChatRoomFragmentForChatListStub
import com.tokopedia.topchat.stub.chatroom.di.ChatRoomFakePresenterModule
import com.tokopedia.topchat.stub.chatroom.di.DaggerChatComponentStub
import com.tokopedia.topchat.stub.chatroom.view.activity.TopChatRoomActivityStub
import com.tokopedia.topchat.stub.common.di.DaggerFakeBaseAppComponent
import com.tokopedia.topchat.stub.common.di.module.FakeAppModule

class TopChatRoomActivityForChatListStub: TopChatRoomActivityStub() {

    override fun initializeChatComponent(): ChatComponent {
        val baseComponent = DaggerFakeBaseAppComponent.builder()
            .fakeAppModule(FakeAppModule(applicationContext))
            .build()
        return DaggerChatComponentStub.builder()
            .fakeBaseAppComponent(baseComponent)
            .chatRoomContextModule(ChatRoomContextModule(this))
            .chatRoomFakePresenterModule(ChatRoomFakePresenterModule(isFromChatRoomTest = false))
            .build()
    }

    override fun scanPathQuery(data: Uri?) {
        intent.putExtra(ApplinkConst.Chat.MESSAGE_ID, "1624012028")
    }

    override fun createChatRoomFragment(bundle: Bundle): BaseChatFragment {
        return TopChatRoomFragmentForChatListStub.createInstance(bundle)
    }
}