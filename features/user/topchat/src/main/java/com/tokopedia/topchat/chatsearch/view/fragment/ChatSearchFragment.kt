package com.tokopedia.topchat.chatsearch.view.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.topchat.chatsearch.di.ChatSearchComponent
import com.tokopedia.topchat.chatsearch.view.activity.ChatSearchActivity
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactoryImpl
import com.tokopedia.topchat.chatsearch.viewmodel.ChatSearchViewModel
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

/**
 * @author : Steven 2019-08-06
 */
class ChatSearchFragment : BaseListFragment<Visitable<*>, ChatSearchTypeFactory>(),
        ChatSearchActivity.Listener, LifecycleOwner {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelFragmentProvider.get(ChatSearchViewModel::class.java) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onSearchQueryChanged(query: String) {
        viewModel.onSearchQueryChanged(query)
    }

    override fun loadData(page: Int) {

    }

    override fun getAdapterTypeFactory(): ChatSearchTypeFactory {
        return ChatSearchTypeFactoryImpl()
    }

    override fun onItemClicked(t: Visitable<*>) {}

    override fun getScreenName() = SCREEN_NAME

    override fun initInjector() {
        getComponent(ChatSearchComponent::class.java).inject(this)
    }

    companion object {
        const val OPEN_DETAIL_MESSAGE = 1324
        const val SCREEN_NAME = "chat search"
        private const val CHAT_TAB_TITLE = "chat_tab_title"

        fun createFragment(): ChatSearchFragment {
            return ChatSearchFragment()
        }

    }
}