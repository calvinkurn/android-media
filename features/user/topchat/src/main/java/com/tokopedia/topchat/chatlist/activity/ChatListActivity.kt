package com.tokopedia.topchat.chatlist.activity

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.fragment.ChatTabListFragment


class ChatListActivity : BaseSimpleActivity(), ChatTabListFragment.Listener {

    override fun getLayoutRes(): Int = R.layout.activity_chat_tab_list
    override fun getParentViewResourceID(): Int = R.id.fragmentContainer
    override fun getToolbarResourceID(): Int = R.id.toolbar
    override fun getNewFragment(): Fragment? = ChatTabListFragment.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        useLightNotificationBar()
        initWindowBackground()
        initTopchatToolbar()
    }

    override fun getActivityToolbar(): Toolbar {
        return toolbar
    }

    private fun useLightNotificationBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
    }

    private fun initWindowBackground() {
        window.decorView.setBackgroundColor(Color.WHITE)
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
