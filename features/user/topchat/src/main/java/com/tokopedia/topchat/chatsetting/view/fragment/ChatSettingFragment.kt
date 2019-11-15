package com.tokopedia.topchat.chatsetting.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.di.ChatSettingComponent

class ChatSettingFragment : BaseDaggerFragment() {

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(ChatSettingComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat_setting, container, false)
    }

    companion object {
        const val SCREEN_NAME = "chat-setting"
    }
}