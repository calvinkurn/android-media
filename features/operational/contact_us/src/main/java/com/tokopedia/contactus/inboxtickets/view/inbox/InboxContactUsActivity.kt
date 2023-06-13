package com.tokopedia.contactus.inboxtickets.view.inbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.contactus.R

class InboxContactUsActivity : BaseSimpleActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, InboxContactUsActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun getNewFragment() = InboxContactUsFragment.newInstance()
    override fun getLayoutRes() = R.layout.contact_us_activity_inbox
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
}
