package com.tokopedia.topchat.chatsetting.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.topchat.chatsetting.data.ChatSetting
import com.tokopedia.topchat.chatsetting.di.ChatSettingComponent
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactoryImpl
import com.tokopedia.topchat.chatsetting.view.adapter.viewholder.ChatSettingViewHolder
import com.tokopedia.topchat.chatsetting.viewmodel.ChatSettingViewModel
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChatSettingFragment : BaseListFragment<Visitable<*>, ChatSettingTypeFactory>(),
        LifecycleOwner, ChatSettingViewHolder.ChatSettingListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(ChatSettingViewModel::class.java) }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(ChatSettingComponent::class.java).inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initArguments(arguments)
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.chatSettings.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Success -> successLoadChatSetting(response.data)
            }
        })
    }

    private fun successLoadChatSetting(data: List<ChatSetting>) {
        val filteredSettings = viewModel.filterSettings(context, data)
        renderList(filteredSettings)
    }

    override fun getAdapterTypeFactory(): ChatSettingTypeFactory {
        return ChatSettingTypeFactoryImpl(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {
        if (page != defaultInitialPage) return
        viewModel.loadChatSettings()
    }

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun isTabSeller(): Boolean {
        return viewModel.isSeller
    }

    companion object {
        const val SCREEN_NAME = "chat-setting"

        fun create(bundle: Bundle?): ChatSettingFragment {
            return ChatSettingFragment().apply {
                arguments = bundle
            }
        }
    }
}