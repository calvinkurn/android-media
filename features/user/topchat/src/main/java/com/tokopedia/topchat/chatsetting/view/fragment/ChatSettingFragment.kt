package com.tokopedia.topchat.chatsetting.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatsetting.di.ChatSettingComponent
import com.tokopedia.topchat.chatsetting.viewmodel.ChatSettingViewModel
import javax.inject.Inject

class ChatSettingFragment : BaseDaggerFragment(), LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelFragmentProvider.get(ChatSettingViewModel::class.java) }

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