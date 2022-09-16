package com.tokopedia.chat_service.view.activity.base

import android.content.Context
import android.os.Bundle
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.chat_service.R
import com.tokopedia.chat_service.di.ChatServiceComponent
import com.tokopedia.chat_service.di.ChatServiceContextModule
import com.tokopedia.chat_service.di.DaggerChatServiceComponent
import com.tokopedia.chat_service.view.fragment.factory.ChatServiceFragmentFactory

abstract class BaseChatServiceActivity: BaseSimpleActivity(), HasComponent<ChatServiceComponent> {

    private var chatServiceComponent: ChatServiceComponent? = null
    protected var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.fragmentFactory = ChatServiceFragmentFactory()
        super.onCreate(savedInstanceState)
    }

    override fun getParentViewResourceID(): Int {
        return R.id.fragmentContainer
    }

    override fun getLayoutRes(): Int {
        return R.layout.base_activity_chat_service
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
}
