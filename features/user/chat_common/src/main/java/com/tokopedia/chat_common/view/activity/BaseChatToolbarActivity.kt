package com.tokopedia.chat_common

import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.chat_common.view.fragment.BaseChatActivityListener
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel

/**
 * @author by nisie on 23/11/18.
 */
abstract class BaseChatToolbarActivity : BaseChatActivity() {

    companion object {
        private val TAG_FRAGMENT = "ChatToolbarActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
    }

    protected abstract fun setupToolbar()

    open fun getChatHeaderLayout(): Int = R.layout.header_chat

    override fun getNewFragment(): Fragment {
        return Fragment()
    }

    fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun getTagFragment(): String {
        return TAG_FRAGMENT
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(tagFragment)
        if (fragment != null && fragment is BaseChatActivityListener && fragment.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }
}