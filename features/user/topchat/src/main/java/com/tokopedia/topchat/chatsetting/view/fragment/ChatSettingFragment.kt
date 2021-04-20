package com.tokopedia.topchat.chatsetting.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.config.GlobalConfig
import com.tokopedia.topchat.chatsetting.analytic.ChatSettingAnalytic
import com.tokopedia.topchat.chatsetting.data.uimodel.ItemChatSettingUiModel
import com.tokopedia.topchat.chatsetting.di.ChatSettingComponent
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingListAdapter
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactory
import com.tokopedia.topchat.chatsetting.view.adapter.ChatSettingTypeFactoryImpl
import com.tokopedia.topchat.chatsetting.view.adapter.decoration.ChatSettingDividerItemDecoration
import com.tokopedia.topchat.chatsetting.view.adapter.viewholder.ChatSettingViewHolder
import com.tokopedia.topchat.chatsetting.viewmodel.ChatSettingViewModel
import com.tokopedia.topchat.chattemplate.view.activity.TemplateChatActivity.PARAM_IS_SELLER
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ChatSettingFragment : BaseListFragment<Visitable<*>, ChatSettingTypeFactory>(),
        LifecycleOwner, ChatSettingViewHolder.ChatSettingListener {

    @Inject
    lateinit var analytic: ChatSettingAnalytic

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var rVadapter: ChatSettingListAdapter? = null
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(ChatSettingViewModel::class.java) }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(ChatSettingComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getChatSetting()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        setupObserver()
        setupRecyclerView(view)
    }

    private fun initArguments() {
        viewModel.isSeller = arguments?.getBoolean(PARAM_IS_SELLER, false) ?: false
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, ChatSettingTypeFactory> {
        rVadapter = ChatSettingListAdapter(adapterTypeFactory)
        return rVadapter as ChatSettingListAdapter
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
        val decoration = ChatSettingDividerItemDecoration(context)
        removeAllItemDecoration(list)
        list?.addItemDecoration(decoration)
    }

    private fun removeAllItemDecoration(list: RecyclerView?) {
        if (list == null) return
        for (i in 0 until list.itemDecorationCount) {
            list.removeItemDecorationAt(i)
        }
    }

    private fun successLoadChatSetting(data: List<Visitable<ChatSettingTypeFactory>>) {
        renderList(data)
    }

    override fun getAdapterTypeFactory(): ChatSettingTypeFactory {
        return ChatSettingTypeFactoryImpl(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun loadData(page: Int) {}

    override fun isLoadMoreEnabledByDefault(): Boolean {
        return false
    }

    override fun isSeller(): Boolean {
        return viewModel.isSeller && GlobalConfig.isSellerApp()
    }

    override fun eventClickChatSetting(element: ItemChatSettingUiModel) {
        analytic.eventClickChatSetting(element)
    }

    override fun isNextItemDivider(adapterPosition: Int): Boolean {
        return rVadapter?.isNextItemDivider(adapterPosition) ?: false
    }

    override fun isPreviousItemTitle(adapterPosition: Int): Boolean {
        return rVadapter?.isPreviousItemTitle(adapterPosition) ?: false
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