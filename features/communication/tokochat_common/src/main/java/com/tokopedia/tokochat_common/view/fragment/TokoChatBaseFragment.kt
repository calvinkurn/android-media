package com.tokopedia.tokochat_common.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.databinding.TokochatBaseFragmentBinding
import com.tokopedia.tokochat_common.util.TokoChatNetworkUtil
import com.tokopedia.tokochat_common.util.TokoChatViewUtil.FOUR_DP
import com.tokopedia.tokochat_common.view.activity.TokoChatBaseActivity
import com.tokopedia.tokochat_common.view.adapter.TokoChatBaseAdapter
import com.tokopedia.tokochat_common.view.adapter.viewholder.decoration.VerticalSpaceItemDecoration
import com.tokopedia.tokochat_common.view.customview.bottomsheet.TokoChatErrorBottomSheet
import com.tokopedia.tokochat_common.view.listener.TokoChatEndlessScrollListener
import com.tokopedia.tokochat_common.view.uimodel.TokoChatLoadingUiModel
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Use [com.tokopedia.tokochat_common.view.customview.layout.BaseTokoChatFragmentLayout]
 * inside your layout
 */
abstract class TokoChatBaseFragment<viewBinding : ViewBinding> : BaseDaggerFragment() {

    protected var binding: viewBinding? by autoClearedNullable()
    protected var baseBinding: TokochatBaseFragmentBinding? by autoClearedNullable()
    abstract var adapter: TokoChatBaseAdapter

    private var endlessRecyclerViewScrollListener: TokoChatEndlessScrollListener? = null
    private val shimmerUiModel = TokoChatLoadingUiModel()
    private val itemDecoration = VerticalSpaceItemDecoration(FOUR_DP.toPx())

    protected val errorBottomSheet = TokoChatErrorBottomSheet()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBindingInflate(container)
        binding?.let {
            baseBinding = TokochatBaseFragmentBinding.bind(it.root)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view, savedInstanceState)
        initObservers()
    }

    abstract fun getViewBindingInflate(container: ViewGroup?): viewBinding
    abstract fun initObservers()
    abstract fun onLoadMore()

    protected open fun initViews(view: View, savedInstanceState: Bundle?) {
        setupChatRoomRecyclerView()
        setupRecyclerViewLoadMore()
        addInitialShimmering()
    }

    private fun setupChatRoomRecyclerView() {
        baseBinding?.tokochatChatroomRv?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        baseBinding?.tokochatChatroomRv?.setHasFixedSize(true)
        baseBinding?.tokochatChatroomRv?.itemAnimator = null
        (baseBinding?.tokochatChatroomRv?.layoutManager as LinearLayoutManager).stackFromEnd = false
        (baseBinding?.tokochatChatroomRv?.layoutManager as LinearLayoutManager).reverseLayout = true
        baseBinding?.tokochatChatroomRv?.adapter = adapter
        baseBinding?.tokochatChatroomRv?.addItemDecoration(itemDecoration)
    }

    private fun setupRecyclerViewLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            baseBinding?.tokochatChatroomRv?.layoutManager?.apply {
                endlessRecyclerViewScrollListener = object : TokoChatEndlessScrollListener(this) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int) {
                        if (!isRecyclerViewLoadingMore()) {
                            onLoadMore()
                        }
                    }
                }
            }
        }
        endlessRecyclerViewScrollListener?.let {
            baseBinding?.tokochatChatroomRv?.addOnScrollListener(it)
        }
    }

    protected fun isRecyclerViewLoadingMore(): Boolean {
        return endlessRecyclerViewScrollListener?.getLoadingStatus() == true
    }

    protected fun changeLoadMoreStatus(status: Boolean) {
        endlessRecyclerViewScrollListener?.changeLoadingStatus(status)
    }

    protected fun resetRecyclerViewScrollState() {
        endlessRecyclerViewScrollListener?.resetState()
    }

    private fun addInitialShimmering() {
        adapter.clearAllItems()
        adapter.addItem(shimmerUiModel)
        adapter.notifyItemInserted(adapter.itemCount)
    }

    protected fun removeShimmering() {
        hideShimmeringHeader()
        val shimmerIndex = adapter.getItems().indexOf(shimmerUiModel)
        if (shimmerIndex > RecyclerView.NO_POSITION) {
            adapter.removeItem(shimmerUiModel)
            adapter.notifyItemRemoved(shimmerIndex)
        }
    }

    protected fun getTokoChatHeader(): HeaderUnify? {
        return (activity as? TokoChatBaseActivity<*>)?.getHeaderUnify()
    }

    protected fun showInterlocutorTypingStatus() {
        (activity as? TokoChatBaseActivity<*>)?.showInterlocutorTyping()
    }

    protected fun hideInterlocutorTypingStatus() {
        (activity as? TokoChatBaseActivity<*>)?.hideInterlocutorTyping()
    }

    protected fun showHeader() {
        (activity as? TokoChatBaseActivity<*>)?.showHeader()
    }

    protected fun hideShimmeringHeader() {
        (activity as? TokoChatBaseActivity<*>)?.hideHeaderShimmering()
    }

    protected fun scrollToBottom() {
        if (adapter.itemCount > 0) {
            baseBinding?.tokochatChatroomRv?.smoothScrollToPosition(Int.ZERO)
        }
    }

    protected fun showGlobalErrorLayout(onActionClick: () -> Unit) {
        val errorType = getErrorType()
        baseBinding?.tokochatIncludeGlobalError?.tokochatGlobalError?.setType(errorType)
        baseBinding?.tokochatIncludeGlobalError?.tokochatGlobalError?.setActionClickListener {
            baseBinding?.tokochatIncludeGlobalError?.tokochatLayoutGlobalError?.hide()
            addInitialShimmering()
            onActionClick()
        }
        baseBinding?.tokochatIncludeGlobalError?.tokochatLayoutGlobalError?.show()
    }

    protected fun getErrorType(): Int {
        return if (isConnectedToNetwork()) {
            GlobalError.SERVER_ERROR
        } else {
            GlobalError.NO_CONNECTION
        }
    }

    protected fun isConnectedToNetwork(): Boolean {
        return if (context != null) {
            TokoChatNetworkUtil.isNetworkAvailable(requireContext())
        } else {
            false
        }
    }
}
