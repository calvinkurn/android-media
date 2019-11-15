package com.tokopedia.topchat.chatsetting.view.fragment

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.topchat.chatsetting.di.ChatSettingComponent
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactoryImpl
import com.tokopedia.topchat.chatsetting.viewmodel.ChatSettingViewModel
import javax.inject.Inject

class ChatSettingFragment : BaseListFragment<Visitable<*>, ChatSettingTypeFactory>(), LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(ChatSettingViewModel::class.java) }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(ChatSettingComponent::class.java).inject(this)
    }

    override fun getAdapterTypeFactory(): ChatSettingTypeFactory {
        return ChatSettingTypeFactoryImpl()
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {
        if (page != defaultInitialPage) return
        viewModel.loadChatSettings()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    companion object {
        const val SCREEN_NAME = "chat-setting"
    }
}