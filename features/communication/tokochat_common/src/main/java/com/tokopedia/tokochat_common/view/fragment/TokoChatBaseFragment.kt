package com.tokopedia.tokochat_common.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.header.HeaderUnify
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatBaseFragmentBinding
import com.tokopedia.tokochat_common.view.activity.TokoChatBaseActivity
import com.tokopedia.tokochat_common.view.adapter.TokoChatBaseAdapter
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Use [com.tokopedia.tokochat_common.view.customview.layout.BaseTokoChatFragmentLayout]
 * inside your layout
 */
abstract class TokoChatBaseFragment<viewBinding : ViewBinding> : BaseDaggerFragment() {

    protected var binding: viewBinding? by autoClearedNullable()
    protected var baseBinding: TokochatBaseFragmentBinding? by autoClearedNullable()
    abstract var adapter: TokoChatBaseAdapter

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
        additionalSetup()
        initViews(view, savedInstanceState)
        initObservers()
    }

    abstract fun getViewBindingInflate(container: ViewGroup?): viewBinding
    abstract fun additionalSetup()
    abstract fun initObservers()

    protected open fun initViews(view: View, savedInstanceState: Bundle?) {
        setupChatRoomRecyclerView()
    }

    private fun setupChatRoomRecyclerView() {
        baseBinding?.tokochatChatroomRv?.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL, false
        )
        baseBinding?.tokochatChatroomRv?.setHasFixedSize(true)
        baseBinding?.tokochatChatroomRv?.itemAnimator = null
        (baseBinding?.tokochatChatroomRv?.layoutManager as LinearLayoutManager).stackFromEnd = false
        (baseBinding?.tokochatChatroomRv?.layoutManager as LinearLayoutManager).reverseLayout = true
        baseBinding?.tokochatChatroomRv?.adapter = adapter
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
}
