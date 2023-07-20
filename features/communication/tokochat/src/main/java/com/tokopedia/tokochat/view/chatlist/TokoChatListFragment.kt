package com.tokopedia.tokochat.view.chatlist

import android.os.Bundle
import android.util.Log
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
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokochat.common.util.TokoChatValueUtil
import com.tokopedia.tokochat.common.view.chatlist.TokoChatListBaseFragment
import com.tokopedia.tokochat.common.view.chatlist.adapter.TokoChatListBaseAdapter
import com.tokopedia.tokochat.common.view.chatlist.listener.TokoChatListItemListener
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.tokochat.databinding.TokochatChatlistFragmentBinding
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class TokoChatListFragment @Inject constructor(
    private val viewModel: TokoChatListViewModel
) :
    TokoChatListBaseFragment<TokochatChatlistFragmentBinding>(),
    TokoChatListItemListener {

    private var chatListJob: Job? = null

    override var adapter: TokoChatListBaseAdapter = TokoChatListBaseAdapter(
        itemListener = this
    )

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
                viewModel.getChatListFlow().collectLatest {
                    when (it) {
                        is Success -> {
                            removeInitialShimmering()
                            addOrUpdateChatItem(it.data)
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

    private fun loadChatListData(size: Int) {
        toggleSwipeRefreshState(true)
        viewModel.loadNextPageChatList(size) { isSuccess ->
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
        if (baseBinding?.tokochatListLayoutSwipeRefresh?.isRefreshing != value) {
            baseBinding?.tokochatListLayoutSwipeRefresh?.isRefreshing = value
        }
    }

    override fun initObservers() {
        viewModel.error.observe(viewLifecycleOwner) {
            Log.d("TOKOCHAT-LIST", it.first.stackTraceToString())
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
        chatListJob?.cancel()
        initChatListData()
    }

    private fun addOrUpdateChatItem(items: List<TokoChatListItemUiModel>) {
        items.forEach {
            val position = adapter.getChatListPosition(it)
            if (position >= Int.ZERO) {
                updateChatItem(position, it)
            } else {
                addNewChatItem(it)
            }
        }
    }

    private fun addNewChatItem(item: TokoChatListItemUiModel) {
        adapter.addItem(item)
        baseBinding?.tokochatListRv?.post {
            adapter.notifyItemInserted(adapter.lastIndex)
        }
    }

    private fun updateChatItem(position: Int, item: TokoChatListItemUiModel) {
        adapter.updateChatListAt(position, item)
        notifyWhenAllowed(position)
    }

    override fun onLoadMore() {
        viewModel.loadNextPageChatList {}
    }

    private fun notifyWhenAllowed(position: Int) {
        try {
            baseBinding?.tokochatListRv?.post {
                adapter.notifyItemChanged(position)
            }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
        }
    }

    override fun onClickChatItem(position: Int, element: TokoChatListItemUiModel) {
        context?.let {
            val source = "?${ApplinkConst.TokoChat.PARAM_SOURCE}=${TokoChatValueUtil.getSource(element.serviceType)}"
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
