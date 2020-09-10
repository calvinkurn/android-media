package com.tokopedia.topchat.stub.chatlist.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topchat.chatlist.activity.ChatListActivity
import com.tokopedia.topchat.chatlist.pojo.ChatListPojo
import com.tokopedia.topchat.stub.chatlist.fragment.ChatTabListFragmentStub
import com.tokopedia.topchat.stub.chatlist.usecase.GetChatNotificationUseCaseStub
import com.tokopedia.topchat.stub.common.UserSessionStub

class ChatListActivityStub : ChatListActivity() {

    lateinit var userSessionInterface: UserSessionStub
    lateinit var chatListUseCase: GraphqlUseCase<ChatListPojo>
    lateinit var chatNotificationUseCase: GetChatNotificationUseCaseStub

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userSessionInterface = UserSessionStub(applicationContext)
    }

    override fun inflateFragment() {
        // Do not inflate fragment immediately
    }

    fun setupTestFragment(
            chatListUseCase: GraphqlUseCase<ChatListPojo>,
            chatNotificationUseCase: GetChatNotificationUseCaseStub
    ) {
        this.chatListUseCase = chatListUseCase
        this.chatNotificationUseCase = chatNotificationUseCase
        val newFragment = newFragment ?: return
        supportFragmentManager.beginTransaction()
                .replace(parentViewResourceID, newFragment, tagFragment)
                .commit()
    }

    override fun getNewFragment(): Fragment? {
        return ChatTabListFragmentStub.create(
                userSessionInterface,
                chatListUseCase,
                chatNotificationUseCase
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {

    }


}