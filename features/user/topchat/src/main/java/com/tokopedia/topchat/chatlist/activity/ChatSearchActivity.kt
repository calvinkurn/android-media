package com.tokopedia.topchat.chatlist.activity

import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.graphics.drawable.DrawableCompat
import androidx.appcompat.widget.Toolbar
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.fragment.ChatSearchFragment

/**
 * @author by steven on 14/08/19.
 * For navigate: use {@link ApplinkConstInternalMarketplace.CHAT_SEARCH}
 */
class ChatSearchActivity : BaseSimpleActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_search)
        setToolbar()
    }

    private fun setToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.chat_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.run {
            setBackgroundDrawable(ColorDrawable(resources.getColor(com.tokopedia.chat_common.R.color.white)))
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)

            val upArrow = MethodChecker.getDrawable(applicationContext, com.tokopedia.chat_common.R.drawable.ic_action_back)
            if (upArrow != null) {
                upArrow.setColorFilter(
                        MethodChecker.getColor(this@ChatSearchActivity, com.tokopedia.chat_common.R.color.grey_700),
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

    }

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        intent.extras?.let {
            bundle.putAll(it)
        }
        return ChatSearchFragment.createFragment()
    }
}
