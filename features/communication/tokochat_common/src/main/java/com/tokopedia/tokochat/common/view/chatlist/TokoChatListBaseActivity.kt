package com.tokopedia.tokochat.common.view.chatlist

import android.content.Context
import android.os.Bundle
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatListBaseActivityBinding
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.setBackIconUnify

abstract class TokoChatListBaseActivity<T>: BaseSimpleActivity(), HasComponent<T> {

    protected var tokoChatComponent: T? = null
    protected var bundle: Bundle? = null

    private var viewBinding: TokochatListBaseActivityBinding? = null

    protected abstract fun setupFragmentFactory()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupFragmentFactory()
        super.onCreate(savedInstanceState)
        setupViewBinding()
        setupChatListToolbar()
    }

    private fun setupViewBinding() {
        viewBinding = TokochatListBaseActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)
    }

    private fun setupChatListToolbar() {
        viewBinding?.tokochatListToolbar?.apply {
            isShowBackButton = true
            title = getString(R.string.title_chatlist)
            setSupportActionBar(this)
            setBackIconUnify()
            contentInsetStartWithNavigation = Int.ZERO
            contentInsetEndWithActions = Int.ZERO
        }
    }

    override fun getParentViewResourceID(): Int {
        return R.id.tokochat_layout_chatlist_fragment
    }

    override fun getLayoutRes(): Int {
        return R.layout.tokochat_list_base_activity
    }

}
