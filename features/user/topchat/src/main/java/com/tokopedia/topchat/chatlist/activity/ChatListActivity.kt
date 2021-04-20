package com.tokopedia.topchat.chatlist.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.fragment.ChatTabListFragment


open class ChatListActivity : BaseSimpleActivity(), ChatTabListFragment.Listener {

    override fun getLayoutRes(): Int = R.layout.activity_chat_tab_list
    override fun getParentViewResourceID(): Int = R.id.fragmentContainer
    override fun getToolbarResourceID(): Int = R.id.toolbar
    override fun getNewFragment(): Fragment? = ChatTabListFragment.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        initWindowBackground()
        initTopchatToolbar()
    }

    override fun getActivityToolbar(): Toolbar {
        return toolbar
    }

    private fun initWindowBackground() {
        window.decorView.setBackgroundColor(MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0))
    }

    private fun initTopchatToolbar() {
        supportActionBar?.setBackgroundDrawable(null)
    }

    companion object {
        const val BUYER_ANALYTICS_LABEL = "buyer"
        const val SELLER_ANALYTICS_LABEL = "seller"
        const val TAG = "ChatListActivity"
    }
}
