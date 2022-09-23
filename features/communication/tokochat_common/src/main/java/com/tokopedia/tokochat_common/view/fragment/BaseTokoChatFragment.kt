package com.tokopedia.tokochat_common.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokochat_common.databinding.BaseFragmentTokoChatBinding
import com.tokopedia.tokochat_common.view.adapter.BaseTokoChatAdapter
import com.tokopedia.utils.lifecycle.autoClearedNullable

/**
 * Use [com.tokopedia.tokochat_common.view.customview.layout.BaseTokoChatFragmentLayout]
 * inside your layout
 */
abstract class BaseTokoChatFragment<viewBinding : ViewBinding> : BaseDaggerFragment() {

    protected var binding: viewBinding? by autoClearedNullable()
    protected var baseBinding: BaseFragmentTokoChatBinding? by autoClearedNullable()
    abstract var adapter: BaseTokoChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBindingInflate(container)
        binding?.let {
            baseBinding = BaseFragmentTokoChatBinding.bind(it.root)
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        additionalSetup()
        initViews()
        initObservers()
    }

    abstract fun getViewBindingInflate(container: ViewGroup?): viewBinding
    abstract fun additionalSetup()
    abstract fun initObservers()

    protected open fun initViews() {
        setupChatRoomRecyclerView()
    }

    private fun setupChatRoomRecyclerView() {
        baseBinding?.tokochatChatroomRv?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        baseBinding?.tokochatChatroomRv?.adapter = adapter
    }
}
