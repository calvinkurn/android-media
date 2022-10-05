package com.tokopedia.tokochat_common.view.activity

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatBaseActivityBinding

abstract class TokoChatBaseActivity<T>: BaseSimpleActivity(), HasComponent<T> {

    protected var tokoChatComponent: T? = null
    protected var bundle: Bundle? = null

    protected var viewBinding: TokochatBaseActivityBinding? = null

    protected abstract fun setupFragmentFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        setupFragmentFactory()
        super.onCreate(savedInstanceState)
        setupViewBinding()
        setupTokoChatHeader()
    }

    override fun getParentViewResourceID(): Int {
        return R.id.tokochat_layout_chatroom_fragment
    }

    override fun getLayoutRes(): Int {
        return R.layout.tokochat_base_activity
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

    private fun setupViewBinding() {
        viewBinding = TokochatBaseActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)
    }

    protected fun getChatHeaderLayout() = R.layout.item_tokochat_header

    protected open fun setupTokoChatHeader() {}

    fun getHeaderUnify(): HeaderUnify? {
        return viewBinding?.tokochatToolbar
    }
}
