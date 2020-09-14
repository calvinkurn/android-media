package com.tokopedia.topchat.stub.chatroom.view.fragment

import android.os.Bundle
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.topchat.chatroom.di.ChatRoomContextModule
import com.tokopedia.topchat.chatroom.view.fragment.TopChatRoomFragment
import com.tokopedia.topchat.stub.chatroom.view.di.ChatModuleStub
import com.tokopedia.topchat.stub.chatroom.view.di.ChatNetworkModuleStub
import com.tokopedia.topchat.stub.chatroom.view.di.DaggerChatComponentStub

class TopChatRoomFragmentStub : TopChatRoomFragment() {

    override fun initInjector() {
        DaggerChatComponentStub
                .builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .chatModuleStub(createChatModuleStub())
                .chatNetworkModuleStub(createChatNetworkStub())
                .chatRoomContextModule(ChatRoomContextModule(context!!))
                .build()
                .inject(this)
    }

    private fun createChatModuleStub(): ChatModuleStub {
        return ChatModuleStub()
    }

    private fun createChatNetworkStub(): ChatNetworkModuleStub {
        return ChatNetworkModuleStub()
    }

    companion object {
        fun createInstance(bundle: Bundle): BaseChatFragment {
            return TopChatRoomFragmentStub().apply {
                arguments = bundle
            }
        }
    }
}