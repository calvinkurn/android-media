package com.tokopedia.tokochat.view.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalCommunication
import com.tokopedia.tokochat.analytics.TokoChatAnalytics
import com.tokopedia.tokochat.common.util.TokoChatTimeUtil.getRelativeTime
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.getSource
import com.tokopedia.tokochat.common.view.chatlist.TokoChatListBaseFragment
import com.tokopedia.tokochat.common.view.chatlist.adapter.TokoChatListBaseAdapter
import com.tokopedia.tokochat.common.view.chatlist.listener.TokoChatListItemListener
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListEmptyUiModel
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.tokochat.config.util.TokoChatErrorLogger
import com.tokopedia.tokochat.databinding.TokochatChatlistFragmentBinding
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoChatListFragment @Inject constructor(
    private val viewModel: TokoChatListViewModel,
    private var userSession: UserSessionInterface
) :
    TokoChatListBaseFragment<TokochatChatlistFragmentBinding>(),
    TokoChatListItemListener {

    private var chatListJob: Job? = null

    override var adapter: TokoChatListBaseAdapter = TokoChatListBaseAdapter(
        itemListener = this
    )

    private var tokoChatAnalytics = TokoChatAnalytics()

    override fun getScreenName(): String = TAG

    override fun initInjector() {}

    override fun getViewBindingInflate(container: ViewGroup?): TokochatChatlistFragmentBinding {
        return TokochatChatlistFragmentBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChatListData()
        initListeners()
    }

    private fun initChatListData() {
        toggleRecyclerViewLayout(true)
        addInitialShimmering()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                var isCompleted = false
                viewModel.getChatListFlow()?.collectLatest {
                    when (it) {
                        is Success -> {
                            setChatListData(it.data)
                            if (!isCompleted) {
                                loadChatListData(it.data.size)
                                isCompleted = true
                            }
                        }
                        is Fail -> {
                            showErrorLayout()
                        }
                    }
                }
            }
        }
    }

    private fun setChatListData(newData: List<TokoChatListItemUiModel>) {
        removeInitialShimmeringAndEmptyView()
        if (newData.isEmpty()) {
            addEmptyChatItem()
        } else {
            baseBinding?.tokochatListRv?.post {
                adapter.setItemsAndAnimateChanges(newData)
            }
        }
    }

    private fun loadChatListData(size: Int) {
        toggleSwipeRefreshState(true)
        viewModel.loadNextPageChatList(size, false) { isSuccess ->
            if (!isSuccess) {
                showErrorLayout()
            }
            toggleSwipeRefreshState(false)
        }
    }

    private fun showErrorLayout() {
        adapter.clearAllItems()
        toggleRecyclerViewLayout(false)
        showGlobalErrorLayout {
            resetChatList()
        }
    }

    private fun toggleSwipeRefreshState(value: Boolean) {
        baseBinding?.tokochatListLayoutSwipeRefresh?.isRefreshing = value
    }

    override fun initObservers() {
        viewModel.chatListPair.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                tokoChatAnalytics.viewDriverChatList(it)
            }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            TokoChatErrorLogger.logExceptionToServerLogger(
                TokoChatErrorLogger.PAGE.TOKOCHAT,
                it.first,
                TokoChatErrorLogger.ErrorType.ERROR_PAGE,
                userSession.deviceId.orEmpty(),
                it.second
            )
        }
    }

    private fun initListeners() {
        baseBinding?.tokochatListLayoutSwipeRefresh?.setOnRefreshListener {
            resetChatList()
        }
    }

    private fun resetChatList() {
        endlessRecyclerViewScrollListener?.resetState()
        adapter.clearAllItemsAndAnimateChanges()
        lifecycleScope.launch {
            chatListJob?.cancelAndJoin()
            initChatListData()
        }
    }

    private fun addEmptyChatItem() {
        adapter.addItem(TokoChatListEmptyUiModel())
        baseBinding?.tokochatListRv?.post {
            adapter.notifyItemInserted(adapter.lastIndex)
        }
    }

    override fun onLoadMore() {
        viewModel.loadNextPageChatList(isLoadMore = true) {}
    }

    override fun onClickChatItem(element: TokoChatListItemUiModel) {
        val serviceName = getSource(element.serviceType)
        tokoChatAnalytics.clickDriverChatList(
            gojekOrderId = element.orderId,
            serviceTypeName = serviceName,
            counter = element.counter,
            chatDuration = getRelativeTime(element.createAt)
        )
        context?.let {
            val source = "?${ApplinkConst.TokoChat.PARAM_SOURCE}=$serviceName"
            val paramOrderIdGojek = "&${ApplinkConst.TokoChat.ORDER_ID_GOJEK}=${element.orderId}"
            val applink = "${ApplinkConstInternalCommunication.TOKO_CHAT}$source$paramOrderIdGojek"
            val intent = RouteManager.getIntent(it, applink)
            startActivity(intent)
        }
    }

    companion object {
        private const val TAG = "TokoChatListFragment"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
            bundle: Bundle
        ): TokoChatListFragment {
            val oldInstance = fragmentManager.findFragmentByTag(TAG) as? TokoChatListFragment
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                TokoChatListFragment::class.java.name
            ).apply {
                arguments = bundle
            } as TokoChatListFragment
        }
    }
}
