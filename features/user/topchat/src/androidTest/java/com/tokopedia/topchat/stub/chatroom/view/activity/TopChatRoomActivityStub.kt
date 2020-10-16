package com.tokopedia.topchat.stub.chatroom.view.activity

import android.os.Bundle
import com.tokopedia.chat_common.BaseChatFragment
import com.tokopedia.topchat.chatroom.domain.usecase.GetChatUseCase
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.stub.chatroom.usecase.GetChatUseCaseStub
import com.tokopedia.topchat.stub.chatroom.view.fragment.TopChatRoomFragmentStub

class TopChatRoomActivityStub : TopChatRoomActivity() {

    private lateinit var getChatUseCase: GetChatUseCase

    override fun inflateFragment() {
        // Don't inflate fragment immediately
    }

    fun setupTestFragment(getChatUseCase: GetChatUseCase) {
        this.getChatUseCase = getChatUseCase
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, tagFragment)
                .commit()
    }

    override fun createChatRoomFragment(bundle: Bundle): BaseChatFragment {
        return TopChatRoomFragmentStub.createInstance(
                bundle,
                getChatUseCase
        )
    }
}