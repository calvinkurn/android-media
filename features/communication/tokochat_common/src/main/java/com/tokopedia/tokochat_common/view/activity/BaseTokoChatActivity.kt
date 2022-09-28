package com.tokopedia.tokochat_common.view.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.BaseActivityTokoChatBinding

abstract class BaseTokoChatActivity<T>: BaseSimpleActivity(), HasComponent<T> {

    protected var tokoChatComponent: T? = null
    protected var bundle: Bundle? = null

    protected var viewBinding: BaseActivityTokoChatBinding? = null

    protected abstract fun setupFragmentFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        setupFragmentFactory()
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    override fun getParentViewResourceID(): Int {
        return R.id.tokochat_chatroom_fragment
    }

    override fun getLayoutRes(): Int {
        return R.layout.base_activity_toko_chat
    }

    override fun getToolbarResourceID(): Int {
        return R.id.partial_tokochat_toolbar
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

    private fun setupViewBinding() {
        viewBinding = BaseActivityTokoChatBinding.inflate(layoutInflater)
    }

    protected fun getChatHeaderLayout() = R.layout.header_toko_chat

    protected open fun setupToolbar() {}

    fun getToolbar(): HeaderUnify? {
        return viewBinding?.partialTokochatToolbar as? HeaderUnify
    }
}
