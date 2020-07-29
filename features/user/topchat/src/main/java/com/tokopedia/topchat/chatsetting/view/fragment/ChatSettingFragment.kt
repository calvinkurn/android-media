package com.tokopedia.topchat.chatsetting.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.sellermigration.SellerMigrationFeatureName
import com.tokopedia.seller_migration_common.presentation.activity.SellerMigrationActivity
import com.tokopedia.topchat.chatsetting.analytic.ChatSettingAnalytic
import com.tokopedia.topchat.chatsetting.data.ChatSetting
import com.tokopedia.topchat.chatsetting.di.ChatSettingComponent
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactoryImpl
import com.tokopedia.topchat.chatsetting.view.adapter.viewholder.ChatSettingViewHolder
import com.tokopedia.topchat.chatsetting.view.widget.ChatSettingItemDecoration
import com.tokopedia.topchat.chatsetting.viewmodel.ChatSettingViewModel
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChatSettingFragment : BaseListFragment<Visitable<*>, ChatSettingTypeFactory>(),
        LifecycleOwner, ChatSettingViewHolder.ChatSettingListener {

    @Inject
    lateinit var analytic: ChatSettingAnalytic

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
        setupRecyclerView(view)
    }

    private fun setupObserver() {
        viewModel.chatSettings.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Success -> successLoadChatSetting(response.data)
            }
        })
    }

    private fun setupRecyclerView(view: View) {
        val list = getRecyclerView(view)
        val decoration = ChatSettingItemDecoration(context)
        removeAllItemDecoration(list)
        list.addItemDecoration(decoration)
    }

    private fun removeAllItemDecoration(list: RecyclerView?) {
        if (list == null) return
        for (i in 0 until list.itemDecorationCount) {
            list.removeItemDecorationAt(i)
        }
    }

    private fun successLoadChatSetting(data: List<ChatSetting>) {
        val filteredSettings = viewModel.filterSettings(::isSupportAppLink, data)
        renderList(filteredSettings)
    }

    private fun isSupportAppLink(chatSetting: ChatSetting): Boolean {
        return RouteManager.isSupportApplink(context, chatSetting.link)
    }

    override fun getAdapterTypeFactory(): ChatSettingTypeFactory {
        return ChatSettingTypeFactoryImpl(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {}

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun isTabSeller(): Boolean {
        return viewModel.isSeller
    }

    override fun eventClickChatSetting(element: ChatSetting) {
        analytic.eventClickChatSetting(element)
    }

    override fun goToSellerMigrationPage() {
        context?.run {
            val intent = SellerMigrationActivity.createIntent(this, SellerMigrationFeatureName.FEATURE_TEMPLATE_CHAT, SCREEN_NAME, ApplinkConst.CHAT_TEMPLATE)
            startActivity(intent)
        }
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