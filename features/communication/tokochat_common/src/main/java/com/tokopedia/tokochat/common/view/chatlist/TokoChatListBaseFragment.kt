package com.tokopedia.tokochat.common.view.chatlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokochat_common.databinding.TokochatListBaseFragmentBinding
import com.tokopedia.tokochat.common.view.chatlist.adapter.TokoChatListBaseAdapter
import com.tokopedia.tokochat.common.view.chatroom.listener.TokoChatEndlessScrollListener
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

    abstract fun getViewBindingInflate(container: ViewGroup?): viewBinding
    abstract fun initObservers()
    abstract fun onLoadMore()
}
