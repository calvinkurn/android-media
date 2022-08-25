package com.tokopedia.chat_service.view.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gojek.conversations.logging.ConversationsLogger
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.chat_service.R
import com.tokopedia.chat_service.di.ChatServiceComponent
import com.tokopedia.chat_service.di.ChatServiceContextModule
import com.tokopedia.chat_service.di.DaggerChatServiceComponent
import com.tokopedia.chat_service.view.fragment.ChatServiceFragment
import com.tokopedia.chat_service.view.fragment.factory.ChatServiceFragmentFactory

open class ChatServiceActivity : BaseSimpleActivity(),
    HasComponent<ChatServiceComponent>,
    ConversationsLogger.ILog
{
    private var chatServiceComponent: ChatServiceComponent? = null
    private var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = ChatServiceFragmentFactory()
        super.onCreate(savedInstanceState)
    }

    override fun getParentViewResourceID(): Int {
        return R.id.fragmentContainer
    }

    override fun getLayoutRes(): Int {
        return R.layout.activity_chat_service
    }


    override fun getNewFragment(): Fragment {
        return ChatServiceFragment.getFragment(
            supportFragmentManager,
            classLoader,
            bundle ?: Bundle()
        )
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    protected open fun initializeChatServiceComponent(): ChatServiceComponent {
        return DaggerChatServiceComponent.builder()
            .baseAppComponent((application as BaseMainApplication).baseAppComponent)
            .chatServiceContextModule(ChatServiceContextModule(this))
            .build().also {
                chatServiceComponent = it
            }
    }


    override fun getComponent(): ChatServiceComponent {
        return chatServiceComponent?: initializeChatServiceComponent()
    }

    override fun d(tag: String, message: String) {

    }

    override fun e(tag: String, error: String, e: Throwable) {

    }

    override fun v(tag: String, message: String) {

    }

    override fun w(tag: String, message: String) {

    }
}