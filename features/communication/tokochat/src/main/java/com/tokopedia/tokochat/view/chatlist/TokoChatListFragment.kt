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
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.removeObservers
import com.tokopedia.tokochat.analytics.TokoChatAnalytics
import com.tokopedia.tokochat.common.util.TokoChatNetworkUtil
import com.tokopedia.tokochat.common.util.TokoChatTimeUtil.getRelativeTime
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.BATCH_LIMIT
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.getSource
import com.tokopedia.tokochat.common.view.chatlist.TokoChatListBaseFragment
import com.tokopedia.tokochat.common.view.chatlist.adapter.TokoChatListBaseAdapter
import com.tokopedia.tokochat.common.view.chatlist.listener.TokoChatListItemListener
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListEmptyUiModel
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListLoaderUiModel
import com.tokopedia.tokochat.config.util.TokoChatErrorLogger
import com.tokopedia.tokochat.databinding.TokochatChatlistFragmentBinding
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoChatListFragment @Inject constructor(
    private val viewModel: TokoChatListViewModel,
    private var userSession: UserSessionInterface,
    private var networkUtil: TokoChatNetworkUtil
) :
    TokoChatListBaseFragment<TokochatChatlistFragmentBinding>(),
    TokoChatListItemListener {

    override var adapter: TokoChatListBaseAdapter = TokoChatListBaseAdapter(
        itemListener = this
    )

    private var chatListJob: Job? = null
    private var tokoChatAnalytics = TokoChatAnalytics()
    private var isFirstLoad = true

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
        chatListJob = lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                isFirstLoad = true
                loadChatListData(BATCH_LIMIT)
                viewModel.setupChatListSource()
                observerChatList()
            }
        }
    }

    private fun observerChatList() {
        removeObservers(viewModel.chatList)
        viewModel.chatList.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    setChatListData(it.data, isFirstLoad)
                    if (isFirstLoad) {
                        isFirstLoad = false
                        if (it.data.size > BATCH_LIMIT) {
                            loadChatListData(it.data.size - BATCH_LIMIT)
                        }
                    }
                }
                is Fail -> {
                    showErrorLayout()
                }
            }
        }
    }

    private fun setChatListData(
        newData: List<TokoChatListItemUiModel>,
        isFirstLoad: Boolean
    ) {
        if (isFirstLoad) {
            setFirstLoadChatListData(newData)
        } else {
            updateChatListData(newData)
        }
    }

    private fun setFirstLoadChatListData(newData: List<TokoChatListItemUiModel>) {
        // Clear all and add data accordingly
        adapter.clearAllItems()
        if (newData.isEmpty()) {
            addEmptyChatItem()
        } else {
            baseBinding?.tokochatListRv?.post {
                adapter.addOrUpdateChatListData(newData)
            }
        }
    }

    private fun updateChatListData(newData: List<TokoChatListItemUiModel>) {
        // Add data to list if not first load
        baseBinding?.tokochatListRv?.post {
            val emptyPosition = adapter.getEmptyViewPosition()
            if (emptyPosition >= Int.ZERO) {
                adapter.clearAllItems()
            }
            adapter.addOrUpdateChatListData(newData)
        }
    }

    private fun loadChatListData(size: Int) {
        viewModel.loadNextPageChatList(size, false) { (isSuccess, _) ->
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
                TokoChatErrorLogger.PAGE.TOKOCHAT_LIST,
                it.first,
                TokoChatErrorLogger.ErrorType.ERROR_PAGE,
                userSession.deviceId.orEmpty(),
                it.second
            )
        }
    }

    private fun initListeners() {
        baseBinding?.tokochatListLayoutSwipeRefresh?.setOnRefreshListener {
            toggleSwipeRefreshState(true)
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
        if (!adapter.isLoaderExist()) {
            val loaderUiModel = TokoChatListLoaderUiModel()
            adapter.addItem(loaderUiModel)
            baseBinding?.tokochatListRv?.post {
                adapter.notifyItemInserted(adapter.lastIndex)
                viewModel.loadNextPageChatList(isLoadMore = true) { (_, newSize) ->
                    removeLoader(loaderUiModel)
                    toggleSwipeRefreshState(false)
                    endlessRecyclerViewScrollListener?.setHasNextPage(
                        (newSize ?: Int.ZERO) > Int.ZERO
                    )
                }
            }
        }
    }

    private fun removeLoader(loaderUiModel: TokoChatListLoaderUiModel) {
        adapter.removeItem(loaderUiModel)
        baseBinding?.tokochatListRv?.post {
            adapter.notifyItemRemoved(adapter.lastIndex)
        }
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

    private fun isConnectedToNetwork(): Boolean {
        return if (context != null) {
            networkUtil.isNetworkAvailable(requireContext())
        } else {
            false
        }
    }

    override fun getErrorType(): Int {
        return if (isConnectedToNetwork()) {
            GlobalError.SERVER_ERROR
        } else {
            GlobalError.NO_CONNECTION
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
