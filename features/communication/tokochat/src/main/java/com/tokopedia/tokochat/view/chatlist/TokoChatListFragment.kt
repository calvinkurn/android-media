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
import com.tokopedia.tokochat.analytics.TokoChatAnalytics
import com.tokopedia.tokochat.analytics.TokoChatAnalyticsConstants
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil.getSource
import com.tokopedia.tokochat.common.util.TokoChatNetworkUtil
import com.tokopedia.tokochat.common.util.TokoChatTimeUtil.getRelativeTime
import com.tokopedia.tokochat.common.view.chatlist.TokoChatListBaseFragment
import com.tokopedia.tokochat.common.view.chatlist.adapter.TokoChatListBaseAdapter
import com.tokopedia.tokochat.common.view.chatlist.listener.TokoChatListItemListener
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListEmptyUiModel
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListLoaderUiModel
import com.tokopedia.tokochat.config.util.TokoChatErrorLogger
import com.tokopedia.tokochat.databinding.TokochatChatlistFragmentBinding
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
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

    private var tokoChatAnalytics = TokoChatAnalytics()

    override fun getScreenName(): String = TAG

    override fun initInjector() {
        // no-op
    }

    override fun getViewBindingInflate(container: ViewGroup?): TokochatChatlistFragmentBinding {
        return TokochatChatlistFragmentBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initChatListData() {
        toggleRecyclerViewLayout(true)
        addInitialShimmering()
        viewModel.processAction(TokoChatListAction.RefreshPage)
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

    private fun showErrorLayout() {
        adapter.clearAllItems()
        showGlobalErrorLayout {
            resetChatList()
        }
    }

    private fun toggleSwipeRefreshState(value: Boolean) {
        baseBinding?.tokochatListLayoutSwipeRefresh?.isRefreshing = value
    }

    override fun initObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    observeTrackerUiState()
                }
                launch {
                    observeChatListUiState()
                }
                launch {
                    observeNavigationUiState()
                }
                launch {
                    observeErrorUiState()
                }
                initChatListData() // reset page data from remote
            }
        }

        // Setup flow observer
        viewModel.setupViewModelObserver()
    }

    private suspend fun observeChatListUiState() {
        viewModel.chatListUiState.collectLatest {
            toggleSwipeRefreshState(false)
            if (!it.isLoading) {
                when {
                    (it.errorMessage != null) -> showErrorLayout()
                    (it.page > 0) -> { // 0 means haven't load anything
                        setChatListData(it.chatItemList, it.page == 1)
                        endlessRecyclerViewScrollListener?.setHasNextPage(it.hasNextPage)
                        removeLoader()
                    }
                }
            }
        }
    }

    private suspend fun observeNavigationUiState() {
        viewModel.navigationUiState.collectLatest {
            if (it.applink.isNotBlank() && context != null) {
                val intent = RouteManager.getIntent(context, it.applink)
                startActivity(intent)
            }
        }
    }

    private suspend fun observeErrorUiState() {
        viewModel.errorUiState.collectLatest {
            if (it.error != null) {
                TokoChatErrorLogger.logExceptionToServerLogger(
                    TokoChatErrorLogger.PAGE.TOKOCHAT_LIST,
                    it.error.first,
                    TokoChatErrorLogger.ErrorType.ERROR_PAGE,
                    userSession.deviceId.orEmpty(),
                    it.error.second
                )
            }
        }
    }

    private suspend fun observeTrackerUiState() {
        viewModel.chatListTrackerUiState.collectLatest {
            tokoChatAnalytics.viewDriverChatList(
                listChatPair = it,
                role = TokoChatAnalyticsConstants.BUYER
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
        initChatListData()
    }

    private fun addEmptyChatItem() {
        adapter.addItem(TokoChatListEmptyUiModel())
        baseBinding?.tokochatListRv?.post {
            adapter.notifyItemInserted(adapter.lastIndex)
        }
    }

    override fun onLoadMore() {
        if (adapter.getLoaderPosition() < 0) {
            val loaderUiModel = TokoChatListLoaderUiModel()
            adapter.addItem(loaderUiModel)
            baseBinding?.tokochatListRv?.post {
                adapter.notifyItemInserted(adapter.lastIndex)
                viewModel.processAction(TokoChatListAction.LoadNextPage)
            }
        }
    }

    private fun removeLoader() {
        baseBinding?.tokochatListRv?.post {
            val loaderPosition = adapter.getLoaderPosition()
            if (loaderPosition >= 0) {
                adapter.removeItemAt(loaderPosition)
                adapter.notifyItemRemoved(loaderPosition)
            }
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
            viewModel.processAction(TokoChatListAction.NavigateToPage(applink))
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
