package com.tokopedia.topchat.stub.chatroom.view.activity

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.view.fragment.BaseChatFragment
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.di.ChatComponent
import com.tokopedia.topchat.chatroom.view.activity.TopChatRoomActivity
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.stub.chatroom.view.fragment.TopChatRoomFragmentStub

class TopChatRoomActivityStub : TopChatRoomActivity() {

    override fun inflateFragment() {
        super.inflateFragment()
        supportFragmentManager.executePendingTransactions()
    }

    override fun initializeChatComponent(): ChatComponent {
        return TopchatRoomTest.chatComponentStub!!
    }

    override fun createChatRoomFragment(bundle: Bundle): BaseChatFragment {
        return TopChatRoomFragmentStub.createInstance(bundle)
    }

    override fun getTagFragment(): String {
        return TAG_STUB_FRAGMENT
    }

    fun getTotalItemInChat(): Int {
        return findViewById<RecyclerView>(R.id.recycler_view_chatroom).layoutManager?.itemCount
            ?: throw IllegalStateException("No recyclerview found")
    }

    companion object {
        const val TAG_STUB_FRAGMENT = "TAG_STUB_FRAGMENT"
    }
}
