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

    protected open fun setupToolbar() {
        val mInflater = LayoutInflater.from(this)
        val mCustomView = mInflater.inflate(getChatHeaderLayout(), null)
        toolbar.removeAllViews()
        toolbar.addView(mCustomView)
        setSupportActionBar(toolbar)

        supportActionBar?.run {
            setBackgroundDrawable(ColorDrawable(resources.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N0)))
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)

            val upArrow = MethodChecker.getDrawable(applicationContext, R.drawable.ic_action_back)
            if (upArrow != null) {
                upArrow.setColorFilter(
                        MethodChecker.getColor(this@BaseChatToolbarActivity, com.tokopedia.unifyprinciples.R.color.Unify_N500),
                        PorterDuff.Mode.SRC_ATOP
                )
                this.setHomeAsUpIndicator(upArrow)
            }
        }

        title = ""
        toolbar.contentInsetStartWithNavigation = 0
        toolbar.contentInsetEndWithActions = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.elevation = 10f
        }

        intent.getParcelableExtra<ChatRoomHeaderViewModel>(ApplinkConst.Chat.PARAM_HEADER)?.let {

            ImageHandler.loadImageCircle2(this@BaseChatToolbarActivity, findViewById<ImageView>(R.id.user_avatar), it.image)
            (findViewById<TextView>(R.id.title)).text = it.name
            (findViewById<TextView>(R.id.label)).visibility = View.GONE
            (findViewById<TextView>(R.id.subtitle)).visibility = View.GONE
        }
    }

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