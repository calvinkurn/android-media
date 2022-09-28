package com.tokopedia.tokochat_common.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.tokochat_common.R

abstract class TokoChatBaseActivity<T>: BaseSimpleActivity(), HasComponent<T> {

    protected var tokoChatComponent: T? = null
    protected var bundle: Bundle? = null

    protected abstract fun setupFragmentFactory()

    override fun getNewFragment(): Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setupFragmentFactory()
        super.onCreate(savedInstanceState)
    }

    override fun getParentViewResourceID(): Int {
        return R.id.tokochat_layout_chatroom_fragment
    }

    override fun getLayoutRes(): Int {
        return R.layout.tokochat_base_activity
    }
}
