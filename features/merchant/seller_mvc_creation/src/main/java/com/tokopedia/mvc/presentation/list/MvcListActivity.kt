package com.tokopedia.mvc.presentation.list

import android.content.Context
import android.content.Intent
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.mvc.R
import com.tokopedia.mvc.presentation.list.fragment.MvcListFragment

class MvcListActivity : BaseSimpleActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, MvcListActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun getNewFragment() = MvcListFragment()
    override fun getLayoutRes() = R.layout.smvc_activity_common
    override fun getParentViewResourceID() = R.id.container
}
