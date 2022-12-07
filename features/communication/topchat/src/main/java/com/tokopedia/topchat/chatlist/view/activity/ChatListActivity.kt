package com.tokopedia.topchat.chatlist.view.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.di.ChatListComponent
import com.tokopedia.topchat.chatlist.di.ChatListContextModule
import com.tokopedia.topchat.chatlist.di.DaggerChatListComponent
import com.tokopedia.topchat.chatlist.view.fragment.ChatTabListFragment

open class ChatListActivity : BaseSimpleActivity(), HasComponent<ChatListComponent>,
    ChatTabListFragment.Listener {

    override fun getLayoutRes(): Int = R.layout.activity_chat_tab_list
    override fun getParentViewResourceID(): Int = R.id.fragmentContainer
    override fun getToolbarResourceID(): Int = R.id.toolbar
    override fun getNewFragment(): Fragment? = ChatTabListFragment.create()

    private var chatListComponent: ChatListComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        initWindowBackground()
        initTopchatToolbar()
    }

    override fun getActivityToolbar(): Toolbar {
        return toolbar
    }

    private fun initWindowBackground() {
        window.decorView.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background))
    }

    private fun initTopchatToolbar() {
        supportActionBar?.setBackgroundDrawable(null)
    }

    override fun getComponent(): ChatListComponent {
        return chatListComponent?: initializeChatListComponent()
    }

    protected open fun initializeChatListComponent(): ChatListComponent {
        return DaggerChatListComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .chatListContextModule(ChatListContextModule(this))
            .build().also {
                chatListComponent = it
            }
    }

    companion object {
        const val BUYER_ANALYTICS_LABEL = "buyer"
        const val SELLER_ANALYTICS_LABEL = "seller"
        const val TAG = "ChatListActivity"
    }
}
