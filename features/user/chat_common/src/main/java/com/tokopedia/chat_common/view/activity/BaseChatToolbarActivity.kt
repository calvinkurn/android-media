package com.tokopedia.chat_common

import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import com.tokopedia.abstraction.common.utils.view.MethodChecker

import com.tokopedia.chat_common.R

/**
 * @author by nisie on 23/11/18.
 */
class BaseChatToolbarActivity : BaseChatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()

    }

    protected fun setupToolbar() {
        val mInflater = LayoutInflater.from(this)
        val mCustomView = mInflater.inflate(R.layout.header_chat, null)
        toolbar.addView(mCustomView)
        setSupportActionBar(toolbar)

        supportActionBar?.run {
            setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            setHomeButtonEnabled(true)

            val upArrow = MethodChecker.getDrawable(applicationContext, R.drawable.ic_action_back)
        if (upArrow != null) {
            upArrow.setColorFilter(
                    MethodChecker.getColor(this, R.color.grey_700),
                    PorterDuff.Mode.SRC_ATOP
            )
            supportActionBar!!.setHomeAsUpIndicator(upArrow)
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}