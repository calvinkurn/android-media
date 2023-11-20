package com.tokopedia.tokochat.common.view.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokochat.common.view.chatlist.adapter.TokoChatListBaseAdapter
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListShimmerUiModel
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatEndlessScrollListener
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatListBaseFragmentBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

abstract class TokoChatListBaseFragment<viewBinding : ViewBinding> : BaseDaggerFragment() {

    protected var binding: viewBinding? by autoClearedNullable()
    protected var baseBinding: TokochatListBaseFragmentBinding? by autoClearedNullable()
    abstract var adapter: TokoChatListBaseAdapter

    protected var endlessRecyclerViewScrollListener: TokoChatEndlessScrollListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBindingInflate(container)
        binding?.let {
            baseBinding = TokochatListBaseFragmentBinding.bind(it.root)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view, savedInstanceState)
        initObservers()
    }

    protected open fun initViews(view: View, savedInstanceState: Bundle?) {
        setupChatRoomRecyclerView()
        setupRecyclerViewLoadMore()
    }

    private fun setupChatRoomRecyclerView() {
        baseBinding?.tokochatListRv?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        baseBinding?.tokochatListRv?.setHasFixedSize(true)
        baseBinding?.tokochatListRv?.itemAnimator = null
        baseBinding?.tokochatListRv?.adapter = adapter
    }

    private fun setupRecyclerViewLoadMore() {
        if (endlessRecyclerViewScrollListener == null) {
            baseBinding?.tokochatListRv?.layoutManager?.apply {
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
            baseBinding?.tokochatListRv?.addOnScrollListener(it)
        }
    }

    protected fun isRecyclerViewLoadingMore(): Boolean {
        return endlessRecyclerViewScrollListener?.getLoadingStatus() == true
    }

    protected fun showGlobalErrorLayout(onActionClick: () -> Unit) {
        val errorType = getErrorType()
        baseBinding?.tokochatIncludeGlobalError?.tokochatGlobalError?.setType(errorType)
        baseBinding?.tokochatIncludeGlobalError?.tokochatGlobalError?.setActionClickListener {
            baseBinding?.tokochatIncludeGlobalError?.tokochatLayoutGlobalError?.hide()
            onActionClick()
        }
        if (errorType == GlobalError.SERVER_ERROR) {
            baseBinding?.tokochatIncludeGlobalError?.tokochatGlobalError?.errorAction?.apply {
                text = getString(R.string.tokochat_list_error_back_to_home)
                setOnClickListener {
                    goToHome()
                }
            }
        }
        baseBinding?.tokochatIncludeGlobalError?.tokochatLayoutGlobalError?.show()
    }

    private fun goToHome() {
        context?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.HOME)
            startActivity(intent)
        }
    }

    protected fun toggleRecyclerViewLayout(shouldShow: Boolean) {
        baseBinding?.tokochatListRv?.showWithCondition(shouldShow)
    }

    abstract fun getErrorType(): Int

    protected fun addInitialShimmering() {
        adapter.clearAllItems()
        for (i in 0..4) { // 5 shimmers from figma
            adapter.addItem(TokoChatListShimmerUiModel())
        }
        adapter.notifyItemInserted(adapter.itemCount)
    }

    abstract fun getViewBindingInflate(container: ViewGroup?): viewBinding
    abstract fun initObservers()
    abstract fun onLoadMore()
}
